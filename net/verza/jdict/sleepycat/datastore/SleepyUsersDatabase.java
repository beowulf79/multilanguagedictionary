package net.verza.jdict.sleepycat.datastore;

import java.io.FileNotFoundException;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import org.apache.log4j.Logger;

public class SleepyUsersDatabase {

	private static final String USERS_STORE = "users";
	private Logger log;
	private SleepyEnvironment sleepyEnv;
	private Database users_table;

	/**
	 * Open all storage containers, indices, and catalogs.
	 */
	public SleepyUsersDatabase() throws DatabaseException,
			FileNotFoundException {

		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName());

		
		sleepyEnv = SleepyEnvironment.getInstance(); // Instance to the Singleton class the manage the environment

		// Set the Berkeley DB config for opening all stores.
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);
		
		

		users_table = sleepyEnv.getEnvironment().openDatabase(null,
				USERS_STORE, dbConfig);

	}

	/**
	 * return handle to this database
	 */
	public final Database getUsersDatabase() {
		log.trace("returning users database");
		return this.users_table;
	}

	/**
	 * Close database
	 */
	public void close() throws DatabaseException {
		log.trace("closing users database");
		users_table.close();

	}
}
