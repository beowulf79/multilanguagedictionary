package testClass.copy.verbs;

/**
 * 
 * 
 * @author      Christian Verdelli
 * @version     %I%, %G%
 * @since       1.0
 * 
 */

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import net.verza.jdict.sleepycat.SleepyEnvironment;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.DatabaseException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.log4j.Logger;


public class SleepyVerbsDatabase {

	// Secondary DBs used for indexes
	private static SleepyEnvironment sleepyEnv;
	private Database dict;
	private static SecondaryConfig sConf, SecConfig, secX2ManyConfig;
	private Map<String, SecondaryDatabase> index_map;
	private Logger log;
	private String lClass, databaseName;
	private InfoClass[] indexes_names;
	private final Integer MAX_INDEXES = 1000;

	/**
	 * 
	 * @param language       nome del database primario 
	 * @param indexes         nomi dei database secondari
	 */
	public SleepyVerbsDatabase(String _language_and_type)
			throws DatabaseException, FileNotFoundException {

		log = Logger.getLogger("testClass.copy.verbs");
		log.trace("called class " + this.getClass().getName()
				+ " with argument language name " + _language_and_type);

		databaseName = _language_and_type;

		indexes_names = new InfoClass[MAX_INDEXES]; // should never reach this number 
		indexDiscover();

		sleepyEnv = SleepyEnvironment.getInstance();// Instance to the Singleton
		// class the manage the
		// environment
		sleepyEnv.setNewEnvironmentPath("/tmp/");

		// Set the Berkeley DB config for opening all stores.
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);

		log.debug("databases found in the environment "
				+ sleepyEnv.getEnvironment().getDatabaseNames());

		dict = sleepyEnv.getEnvironment().openDatabase(null, databaseName,
				dbConfig);

		log.debug("opened database " + dict.getDatabaseName());

		index_map = new HashMap<String, SecondaryDatabase>();
		this.dynamicKeyCreatorFactory(indexes_names);

	}

	/**
	 *  
	 * effettua una scansione del file di properties nella sezione
	 * class.fields associata alla classe lClass; memorizza nel array di stringhe
	 * indexes_names gli attributi class_attribute che definiscono l'attributo key_type=subindex
	 */
	private void indexDiscover() {

		LanguageConfigurationClassDescriptor sub = LanguagesConfiguration
				.getNuovo().get(this.databaseName);

		String nickname = sub.getLanguageNickname();
		log.trace("found nickname " + nickname);

		this.lClass = sub.getClassQualifiedName();
		log.debug("	class to load " + this.lClass);

		// load  indexes
		log.trace("looking for  indexes");
		int i = 0;

		List lista = sub.getFields();
		for (Iterator it = lista.iterator(); it.hasNext();) {
			LanguageFieldConfigurationClassDescritor tmp = (LanguageFieldConfigurationClassDescritor) it
					.next();
			if ("subindex".equals(tmp.getKey_type())) {

				log.debug(" attribute to index " + tmp.getAttributeName()
						+ " index class creator "
						+ tmp.getIndex_class_creator());

				this.indexes_names[i] = new InfoClass(tmp.getAttributeName(),
						tmp.getIndex_class_creator(), tmp.getMultivalue());

			}
		}

		//resize indexes_names lenght to the real number of indexes
		InfoClass[] tmp = new InfoClass[i];
		for (int counter = 0; counter < i; counter++) {
			tmp[counter] = indexes_names[counter];
		}

		indexes_names = tmp;
		log.trace("indexes class creator has been resized to "
				+ indexes_names.length + " indexes");

	}

	/**
	 * 
	 * Per ogni indice contenuto nell'array secondaryDatabaseName instanzia
	 * un'instanza della classe dynamicKeyCreator e la inserisce come valore nel hashmap
	 * index_map avente come chiave la stringa composta dall'identificativo del linguaggio
	 * più il nome dell'indice 
	 * 
	 * 
	 * @param database       nome del database primario 
	 * @param indexes         nomi dei database secondari
	 */
	private void dynamicKeyCreatorFactory(InfoClass _indexinfo[]) {

		log
				.trace("called method SecondaryDatabaseDiscover with _indexinfo size "
						+ _indexinfo.length);

		sConf = new SecondaryConfig();

		log.trace("entering loop to build SecondaryDatabase HashMap");
		try {
			for (int i = 0; i < _indexinfo.length; i++) {

				String dbname = databaseName + _indexinfo[i].getIndexName();

				log.debug("class to load "
						+ _indexinfo[i].getIndexClassCreatorName());
				Class<?> Class2Load = Class.forName(_indexinfo[i]
						.getIndexClassCreatorName());

				/**
				 * if the attribute is multivalue must use a class that implements SecondaryMultiKeyCreator
				 * otherwise a class that implements SecondaryKeyCreator
				 */
				if (_indexinfo[i].getIsMultivalue()) {
					Class<?>[] c_arr = new Class[] { String.class,
							String.class, String.class };

					Constructor<?> constr = Class2Load.getConstructor(c_arr);

					// SecondaryConfig for x-to-many database
					secX2ManyConfig = new SecondaryConfig();
					secX2ManyConfig.setAllowCreate(true);
					secX2ManyConfig.setTransactional(true);
					secX2ManyConfig.setAllowPopulate(true);
					secX2ManyConfig.setSortedDuplicates(true);
					MultiValueIndexKeyCreator obj = (MultiValueIndexKeyCreator) constr
							.newInstance(new Object[] { this.databaseName,
									this.lClass,
									this.indexes_names[i].getIndexName() });

					secX2ManyConfig.setMultiKeyCreator(obj);
					log.debug("instantied class " + obj.getClass().getName());
					sConf = secX2ManyConfig;

				} else {
					Class<?>[] c_arr = new Class[] { String.class,
							String.class, String.class };

					Constructor<?> constr = Class2Load.getConstructor(c_arr);

					SecConfig = new SecondaryConfig();
					SecConfig.setTransactional(true);
					SecConfig.setAllowCreate(true); // Create the database if it does not already exist.
					SecConfig.setAllowPopulate(true); // Allow autopopulate
					SecConfig.setSortedDuplicates(true); // Need to allow duplicates for
					// our secondary database

					IndexKeyCreator obj = (IndexKeyCreator) constr.newInstance(
							this.databaseName, this.lClass,
							this.indexes_names[i].getIndexName());
					SecConfig.setKeyCreator(obj);
					log.debug("instantied class " + obj.getClass().getName());
					sConf = SecConfig;
				}

				SecondaryDatabase tmp = sleepyEnv.getEnvironment()
						.openSecondaryDatabase(null, dbname, dict, sConf);

				log.info("opened database " + tmp.getDatabaseName());
				log.debug("put into the hashmap  key (index_name)" + dbname
						+ " with value (secondaryDatabaseName)  "
						+ tmp.getDatabaseName());

				index_map.put(dbname, tmp);
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

	}

	public Database getDatabase() {
		return dict;
	}

	public SecondaryDatabase getIndex(String _index) {
		String indexname = this.databaseName + _index;
		log.trace("returning index " + indexname);
		return index_map.get(indexname);
	}

	/**
	 * Close all databases and the environment.
	 */
	public void close() throws DatabaseException {

		dict.close();

	}

	public String toString() {
		String s = new String();
		try {
			s = "language " + this.databaseName + " class loaded "
					+ this.lClass + " sleepy db name "
					+ this.dict.getDatabaseName();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		return s;
	}

}
