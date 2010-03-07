/**
 * 
 */
package net.verza.jdict.dictionary.sleepycat;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.verza.jdict.properties.PropertiesLoader;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

/**
 * @author christianverdelli
 * 
 */
public class SleepyFactory {

    private HashMap<String, SleepyDatabase> db;
    private Logger log;
    private static SleepyFactory singleton = null;

    /**
     * scansione il file di property ed instanzia una classe SleepyVerbsDatabase
     * per ogni language trovato e la memorizza nel hashmap avente chiave
     * language.nickname
     * 
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws DatabaseException
     * @throws FileNotFoundException
     */
    public SleepyFactory() throws SecurityException, IllegalArgumentException,
	    ClassNotFoundException, NoSuchMethodException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException, FileNotFoundException, DatabaseException {

	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());
	db = new HashMap<String, SleepyDatabase>();
	this.buildDatabaseHashMap();

    }

    /**
     * scansione il file di property ed instanzia una classe SleepyVerbsDatabase
     * per ogni language trovato e la memorizza nel hashmap avente chiave
     * language.nickname
     * 
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws DatabaseException
     * @throws FileNotFoundException
     */
    private void buildDatabaseHashMap() throws SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException,
	    FileNotFoundException, DatabaseException {
	List<SubnodeConfiguration> prop = PropertiesLoader
		.getHierarchicalProperty("language");
	log.debug("number of languages found in property file: "
		+ (prop).size());

	for (Iterator<SubnodeConfiguration> it = prop.iterator(); it.hasNext();) {
	    HierarchicalConfiguration sub = (HierarchicalConfiguration) it
		    .next();

	    String nickname = sub.getString("nickname");
	    String type = sub.getString("type");

	    if (sub.getBoolean("enabled"))
		db.put(nickname + type, new SleepyDatabase(nickname + type));

	}

    }

    /*
     * return singleton Object
     */
    public static SleepyFactory getInstance() throws SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException,
	    FileNotFoundException, DatabaseException {
	if (singleton == null)
	    singleton = new SleepyFactory();
	return singleton;
    }

    /**
     * restituisce il database associato al linguaggio
     * 
     * @param la
     *                stringa associata al linguaggio corrispondente alla
     *                direttive language.nickname
     */
    public SleepyDatabase getDatabase(String language) throws DatabaseException {
	log.trace("called function getDatabase with parameter " + language);
	if (db.containsKey(language))
	    return (SleepyDatabase) db.get(language);
	log.warn("language " + language + " not found inside database map");
	throw new DatabaseException("language " + language
		+ " not found inside database map");

    }

}
