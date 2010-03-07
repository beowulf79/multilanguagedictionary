package net.verza.jdict.dictionary.sleepycat;

import java.io.FileNotFoundException;

import net.verza.jdict.model.UserProfile;

import org.apache.log4j.Logger;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;

public class SleepyUsersDatabase {

    private static final String USERS_STORE = "users";
    private Logger log;
    private SleepyEnvironment sleepyEnv;
    private Database users_table;
    private DatabaseConfig dbConfig;
    private StoredClassCatalog classCatalog;

    /**
     * Open all storage containers, indices, and catalogs.
     */
    public SleepyUsersDatabase() throws DatabaseException,
	    FileNotFoundException {

	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());

	sleepyEnv = SleepyEnvironment.getInstance(); // Instance to the
	// Singleton class the
	// manage the
	// environment

	// Set the Berkeley DB config for opening all stores.
	dbConfig = new DatabaseConfig();
	dbConfig.setTransactional(false);
	dbConfig.setAllowCreate(true);

	open();

    }

    public void open() throws DatabaseException {
	log.trace("called function open");

	users_table = sleepyEnv.getEnvironment("users").openDatabase(null,
		USERS_STORE, dbConfig);
	log.debug(USERS_STORE + " database open");

	// open class catalog Database
	log.debug("opening users class catalog database");
	// class catalog does not have duplicates
	dbConfig.setSortedDuplicates(false);
	Database classCatalogDatabase = sleepyEnv.getEnvironment("users")
		.openDatabase(null, "class_catalog", dbConfig);

	// Instantiate the class catalog
	classCatalog = new StoredClassCatalog(classCatalogDatabase);

    }

    /**
     * return handle to this database
     */
    public final Database getUsersDatabase() {
	log.trace("called function getUsersDatabase");
	return this.users_table;
    }

    /**
     * Close database
     */
    public void close() throws DatabaseException {
	log.trace("called function close");
	users_table.close();

    }

    public StoredClassCatalog getClassCatalog() {
	return classCatalog;
    }

    public EntryBinding getEntryBinding() {
	EntryBinding binding = new com.sleepycat.bind.serial.SerialBinding(
		classCatalog, UserProfile.class);
	return binding;
    }

}
