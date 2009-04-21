
package net.verza.jdict.sleepycat.datastore;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import net.verza.jdict.Configuration;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import org.apache.log4j.Logger;

/**
 * 
 */
public class SleepyEnvironment {

	private Environment env;
	private static SleepyEnvironment singleton = null;
	private Logger log;
	private String EnviromentPath = Configuration.ENVIROMENT_HOME;
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
		log.info("Opening environment in: " + EnviromentPath);
		envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);
		env = new Environment(new File(EnviromentPath), envConfig);

	}
	
	
	public void setNewEnvironmentPath(String _path)	 throws DatabaseException {
		log.debug("resetting path to "+_path);
		this.EnviromentPath = _path;
		env = new Environment(new File(EnviromentPath), envConfig);
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
	public final Environment getEnvironment() {
		return env;
	}

	/**
	 * Close all databases and the environment.
	 */
	public void close() throws DatabaseException {
		env.close();
	}
}
