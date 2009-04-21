/**
 * 
 */
package net.verza.jdict.sleepycat.datastore;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import org.apache.log4j.Logger;

import net.verza.jdict.properties.PropertiesLoader;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;

import com.sleepycat.je.DatabaseException;

/**
 * @author christianverdelli
 *
 */
public class SleepyFactory {

	
	private HashMap<String,SleepyDatabase> db;
	private Logger log;
	private static SleepyFactory singleton = null;
	
	
	/**
	 * scansione il file di property ed instanzia una classe SleepyVerbsDatabase
	 * per ogni language trovato e la memorizza nel hashmap avente chiave
	 * language.nickname
	 */
	public SleepyFactory()	{

		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName());
		db = new HashMap<String,SleepyDatabase>();
		this.buildDatabaseHashMap();

	}
	

	/**
	 * scansione il file di property ed instanzia una classe SleepyVerbsDatabase
	 * per ogni language trovato e la memorizza nel hashmap avente chiave
	 * language.nickname
	 */
	private void buildDatabaseHashMap()	{
		List<SubnodeConfiguration> prop = PropertiesLoader.getHierarchicalProperty("language");
		log.debug("number of languages found in property file: " + ( prop).size());
		
		try {
			
			for (Iterator<SubnodeConfiguration> it = prop.iterator(); it.hasNext();) {
				HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
				
				String nickname = sub.getString("nickname");
				String type = sub.getString("type");
				
				if(sub.getBoolean("enabled")) 
						db.put(nickname+type,  new SleepyDatabase(nickname+type));
				
				
				

			}
			
		}catch (FileNotFoundException e) {
				e.printStackTrace();
		}catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	
	/*
	 * return singleton Object
	 */
	public static SleepyFactory getInstance() {
		if (singleton == null)
			singleton = new SleepyFactory();
		return singleton;
	}
	
	
	public void test()	{
		try {
			
			Iterator<String> myVeryOwnIterator = db.keySet().iterator();
			while(myVeryOwnIterator.hasNext()) {
				String s = (String)myVeryOwnIterator.next();
			    System.out.println("key (language string): "+s+
			    								" value (database name): "+db.get(s).getDatabase().getDatabaseName());
			}
		}catch (DatabaseException e) {
			e.printStackTrace();
		}


	}
	
		
	/**
	 * restituisce il database associato al linguaggio
	 * @param la stringa associata al linguaggio corrispondente alla direttive language.nickname
	 */
	public SleepyDatabase getDatabase(String language)	{
		log.debug("returning database "+language);
		return (SleepyDatabase)db.get(language);
	}
	

}
