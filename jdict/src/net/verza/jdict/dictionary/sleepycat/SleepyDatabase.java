package net.verza.jdict.dictionary.sleepycat;

/**
 * 
 * 
 * @author      Christian Verdelli
 * @version     %I%, %G%
 * @since       1.0
 * 
 */

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.verza.jdict.dictionary.sleepycat.indexes.IndexKeyCreator;
import net.verza.jdict.dictionary.sleepycat.indexes.MultiValueIndexKeyCreator;
import net.verza.jdict.model.SearchableObject;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguageFieldConfigurationClassDescritor;
import net.verza.jdict.properties.LanguagesConfiguration;
import net.verza.jdict.utils.Utility;

import org.apache.log4j.Logger;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;

public class SleepyDatabase {

    // Secondary DBs used for indexes
    private static SleepyEnvironment sleepyEnv;
    private Database dict;
    private DatabaseConfig dbConfig;
    private StoredClassCatalog classCatalog;
    private static SecondaryConfig sConf, SecConfig, secX2ManyConfig;
    private Map<String, SecondaryDatabase> index_map;
    private Logger log;
    private String lClass, database;
    private InfoClass[] indexes_names;
    private final Integer MAX_INDEXES = 1000;

    /**
     * 
     * @param language
     *                nome del database primario
     * @param indexes
     *                nomi dei database secondari
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws SecurityException
     */
    public SleepyDatabase(String _language_and_type) throws DatabaseException,
	    FileNotFoundException, SecurityException, IllegalArgumentException,
	    ClassNotFoundException, NoSuchMethodException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException {

	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName()
		+ " with argument language name " + _language_and_type);

	database = _language_and_type;
	indexes_names = new InfoClass[MAX_INDEXES]; // should never reach this
	// number
	indexDiscover();

	sleepyEnv = SleepyEnvironment.getInstance();// Instance to the Singleton

	// Set the Berkeley DB config for opening all stores.
	dbConfig = new DatabaseConfig();
	dbConfig.setTransactional(false);
	dbConfig.setAllowCreate(true);

	log.debug("databases found in the environment "
		+ sleepyEnv.getEnvironment("default").getDatabaseNames());

	index_map = new HashMap<String, SecondaryDatabase>();

	this.open();

    }

    public void open() throws DatabaseException, FileNotFoundException,
	    SecurityException, IllegalArgumentException,
	    ClassNotFoundException, NoSuchMethodException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException {
	dict = sleepyEnv.getEnvironment("default").openDatabase(null, database,
		dbConfig);
	log.info("successfully openened database " + dict.getDatabaseName());

	// open class catalog Database
	log.debug("opening users class catalog database");
	// class catalog does not have duplicates
	dbConfig.setSortedDuplicates(false);
	Database classCatalogDatabase = sleepyEnv.getEnvironment("default")
		.openDatabase(null, "class_catalog", dbConfig);

	log.info("successfully openened class catalog database");
	// Instantiate the class catalog
	classCatalog = new StoredClassCatalog(classCatalogDatabase);

	this.dynamicKeyCreatorFactory();

    }

    /**
     * 
     * effettua una scansione del file di properties nella sezione class.fields
     * associata alla classe lClass; memorizza nel array di stringhe
     * indexes_names gli attributi class_attribute che definiscono l'attributo
     * key_type=subindex
     */
    private void indexDiscover() {

	LanguageConfigurationClassDescriptor sub = LanguagesConfiguration
		.getLanguageConfigurationBlock().get(this.database);

	this.lClass = sub.getClassQualifiedName();
	log.debug("set lClass to " + this.lClass);

	// load indexes
	int i = 0;
	List<LanguageFieldConfigurationClassDescritor> lista = sub.getFields();
	for (Iterator<LanguageFieldConfigurationClassDescritor> it = lista
		.iterator(); it.hasNext();) {
	    LanguageFieldConfigurationClassDescritor tmp = (LanguageFieldConfigurationClassDescritor) it
		    .next();
	    if ("subindex".equals(tmp.getKey_type())) {

		log.debug(" attribute to index " + tmp.getAttributeName()
			+ " index class creator "
			+ tmp.getIndex_class_creator());

		this.indexes_names[i++] = new InfoClass(tmp.getAttributeName(),
			tmp.getIndex_class_creator(), tmp.getMultivalue());

	    }
	}

	// resize indexes_names lenght to the real number of indexes
	InfoClass[] tmp = new InfoClass[i];
	for (int counter = 0; counter < i; counter++) {
	    tmp[counter] = indexes_names[counter];
	}

	indexes_names = tmp;
	log.debug("indexes class creator has been resized to "
		+ indexes_names.length + " indexes");

    }

    /**
     * 
     * Per ogni indice contenuto nell'array secondaryDatabaseName instanzia
     * un'instanza della classe dynamicKeyCreator e la inserisce come valore nel
     * hashmap index_map avente come chiave la stringa composta
     * dall'identificativo del linguaggio più il nome dell'indice
     * 
     * 
     * @param database
     *                nome del database primario
     * @param indexes
     *                nomi dei database secondari
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IllegalArgumentException
     * @throws DatabaseException
     */
    private void dynamicKeyCreatorFactory() throws ClassNotFoundException,
	    SecurityException, NoSuchMethodException, IllegalArgumentException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException, DatabaseException {
	log.trace("called method");

	sConf = new SecondaryConfig();
	log.debug("entering loop to build SecondaryDatabase HashMap");

	for (int i = 0; i < this.indexes_names.length; i++) {
	    String dbname = database + this.indexes_names[i].getIndexName();
	    log.debug("class to load "
		    + this.indexes_names[i].getIndexClassCreatorName());
	    Class<?> Class2Load = Class.forName(this.indexes_names[i]
		    .getIndexClassCreatorName());

	    log.debug("HERE");

	    /**
	     * if the attribute is multivalue must use a class that implements
	     * SecondaryMultiKeyCreator otherwise a class that implements
	     * SecondaryKeyCreator
	     */
	    if (this.indexes_names[i].getIsMultivalue()) {
		Class<?>[] c_arr = new Class[] { String.class, String.class,
			String.class };
		log.debug("HERE M2");
		Constructor<?> constr = Class2Load.getConstructor(c_arr);

		// SecondaryConfig for x-to-many database
		secX2ManyConfig = new SecondaryConfig();
		secX2ManyConfig.setAllowCreate(true);
		secX2ManyConfig.setTransactional(false);
		secX2ManyConfig.setAllowPopulate(true);
		secX2ManyConfig.setSortedDuplicates(true);
		MultiValueIndexKeyCreator obj = (MultiValueIndexKeyCreator) constr
			.newInstance(new Object[] { this.database, this.lClass,
				this.indexes_names[i].getIndexName() });
		obj.setBinding(getEntryBinding());
		secX2ManyConfig.setMultiKeyCreator(obj);
		log.debug("instantied class " + obj.getClass().getName());
		sConf = secX2ManyConfig;

	    } else {
		Class<?>[] c_arr = new Class[] { String.class, String.class,
			String.class };
		Constructor<?> constr = Class2Load.getConstructor(c_arr);
		SecConfig = new SecondaryConfig();
		SecConfig.setTransactional(false);
		SecConfig.setAllowCreate(true); // Create the database if it
		// does not already exist.
		SecConfig.setAllowPopulate(true); // Allow autopopulate
		SecConfig.setSortedDuplicates(true); // Need to allow
		// duplicates for
		// our secondary database
		IndexKeyCreator obj = (IndexKeyCreator) constr.newInstance(
			this.database, this.lClass, this.indexes_names[i]
				.getIndexName());
		obj.setBinding(getEntryBinding());
		SecConfig.setKeyCreator(obj);
		log.debug("instantied class " + obj.getClass().getName());
		sConf = SecConfig;
	    }

	    SecondaryDatabase tmp = sleepyEnv.getEnvironment("default")
		    .openSecondaryDatabase(null, dbname, dict, sConf);

	    log.info("opened secondary database " + tmp.getDatabaseName());
	    log.debug("put into the hashmap  key (index_name) " + dbname
		    + " with value (secondaryDatabaseName)  "
		    + tmp.getDatabaseName());

	    index_map.put(dbname, tmp);
	}

    }

    public Database getDatabase() {
	return dict;
    }

    public SecondaryDatabase getIndex(String _index) {
	String indexname = this.database + _index;
	log.debug("returning index " + indexname);
	return index_map.get(indexname);
    }

    public void flushDatabase() throws DatabaseException {

	// iterate through secondary database map and empty each of them
	Iterator<String> it = this.index_map.keySet().iterator();
	while (it.hasNext()) {
	    String key = (String) it.next();
	    log.info("flushing  secondary database " + key);
	    sleepyEnv.getEnvironment(key).truncateDatabase(null, key, false);

	}
	log.info("flushing database");
	sleepyEnv.getEnvironment(this.database).truncateDatabase(null,
		this.database, true);
    }

    /**
     * Close all databases and the environment.
     */
    public void close() throws DatabaseException {

	// iterate through secondary database map and close each of them
	Iterator<String> it = this.index_map.keySet().iterator();
	while (it.hasNext()) {
	    String key = (String) it.next();
	    SecondaryDatabase secdb = this.index_map.get(key);
	    log.debug("closing  secondary database " + key);
	    secdb.closeCursors();
	    secdb.close();
	}

	// close primary database
	dict.closeCursors();
	dict.close();

    }

    public Integer getFreeId() throws DatabaseException,
	    UnsupportedEncodingException {
	log.trace("called function getFreeId");
	DatabaseEntry key = new DatabaseEntry();
	DatabaseEntry data = new DatabaseEntry();
	Cursor cursor = dict.openCursor(null, null);
	OperationStatus op = cursor.getLast(key, data, LockMode.DEFAULT);
	if (op == OperationStatus.SUCCESS) {
	    log.debug("search returned id" + new String(key.getData()));
	    Integer id = Utility.byteArrayToInt(key.getData()) + 1;
	    log.debug("first available id is" + id);
	    return id;
	}

	log.error("free id not found");
	return null;
    }

    public String toString() {
	String s = new String();
	try {
	    s = "language " + this.database + " class loaded " + this.lClass
		    + " sleepy db name " + this.dict.getDatabaseName();
	} catch (DatabaseException e) {
	    e.printStackTrace();
	}

	return s;
    }

    public EntryBinding getEntryBinding() {
	log.trace("called method getbinding");
	EntryBinding binding = new com.sleepycat.bind.serial.SerialBinding(
		classCatalog, SearchableObject.class);
	return binding;
    }

}
