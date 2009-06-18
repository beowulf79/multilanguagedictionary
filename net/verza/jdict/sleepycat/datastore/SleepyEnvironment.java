
package net.verza.jdict.sleepycat.datastore;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import net.verza.jdict.properties.PropertiesLoader;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import org.apache.log4j.Logger;

/**
 * 
 */
public class SleepyEnvironment {

	private HashMap<String,Environment> envMap;
	private static SleepyEnvironment singleton = null;
	private Logger log;

	
	private EnvironmentConfig envConfig;

	/**
	 * Open the environment
	 */
	public SleepyEnvironment() throws DatabaseException, FileNotFoundException {

		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class "+this.getClass().getName());
		/*
		 * Open the Berkeley DB environment in transactional mode.
		 */
		envMap = new HashMap<String,Environment>();
		
		envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);
		envConfig.setConfigParam("java.util.logging.ConsoleHandler.on", "false" );
		envConfig.setConfigParam("java.util.logging.FileHandler.on", "true" );
		envConfig.setConfigParam("java.util.logging.level", "INFO" );
		
		
		this.buildEnvironmentHashMap();

	}
	
	
	private void buildEnvironmentHashMap()	throws DatabaseException,
		FileNotFoundException {
		
		log.trace("opening default environment");
		envMap.put("default", 
				new Environment(new File(PropertiesLoader.
						getProperty("database_settings.default.environmentPath")), envConfig)
		);
		
		log.trace("opening users environment");
		envMap.put("users", 
				new Environment(new File(PropertiesLoader.
						getProperty("database_settings.users.environmentPath")), envConfig)
		);
		
		log.trace("opening class_catalog environment");
		envMap.put("class_catalog", 
				new Environment(new File(PropertiesLoader.
						getProperty("database_settings.class_catalog.environmentPath")), envConfig)
		);
	}
	
	
	
	/*
	 * return singleton Object
	 */
	public static SleepyEnvironment getInstance() throws DatabaseException,
			FileNotFoundException {
		if (singleton == null)
			singleton = new SleepyEnvironment();
		
		return singleton;
	}

	
	/**
	 * Return the storage environment
	 */
	public final Environment getEnvironment(String dbName) {
		log.trace("called function getEnvironment with argument "+dbName
				);
		if( (dbName == null)	|| 
				(dbName.equals("default")) ||
					(!this.envMap.containsKey(dbName))  ){
				log.debug("returning default environment "+this.envMap.get("default").toString());
				return this.envMap.get("default");
		}
		
		log.debug("returning environMent "+this.envMap.get(dbName).toString());
		return this.envMap.get(dbName);
	}

	
}
