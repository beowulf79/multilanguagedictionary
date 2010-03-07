package net.verza.jdict.dictionary.sleepycat;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.FileNotFoundException;

import net.verza.jdict.dictionary.sleepycat.indexes.SleepyCategoryDatabaseIndexKeyCreator;

import org.apache.log4j.Logger;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;

/**
 * 
 * 
 * @author
 */
public class SleepyCategoryDatabase {

    // Primary Database
    private static final String CATEGORY_TABLE_STORE = "categoryTable";
    private static final String CATEGORY_SECONDARY_TABLE = "category_string";
    private SleepyEnvironment env;
    private Database categoryDatabase;
    private DatabaseConfig dbConfig;
    private SecondaryConfig SecConfig;
    private Logger log;

    // this Secondary database is used as index to perform
    // lookup based on the Category String
    private SecondaryDatabase categoryName_SecondaryDatabse;

    /**
     * Open all storage containers, indices, and catalogs.
     */
    public SleepyCategoryDatabase() throws DatabaseException,
	    FileNotFoundException {

	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());

	// Set the Berkeley DB config for opening all stores.
	dbConfig = new DatabaseConfig();
	dbConfig.setTransactional(false);
	dbConfig.setAllowCreate(true);

	SecConfig = new SecondaryConfig();
	SecConfig.setTransactional(false);
	SecConfig.setAllowCreate(true); // Create the database if it
	// does not
	// already exist.
	SecConfig.setAllowPopulate(true); // Allow autopopulate
	SecConfig.setSortedDuplicates(true);

	SleepyCategoryDatabaseIndexKeyCreator categoryName_Index = new SleepyCategoryDatabaseIndexKeyCreator(
		this);
	SecConfig.setKeyCreator(categoryName_Index);

	this.open();

    }

    /**
     * Return the part storage container.
     */
    public final Database getCategoryDatabase() {
	return this.categoryDatabase;
    }

    public final SecondaryDatabase getCategoryName_SecondaryDatabase() {
	return this.categoryName_SecondaryDatabse;
    }

    public void open() throws DatabaseException, FileNotFoundException {
	log.trace("called function open");
	env = SleepyEnvironment.getInstance();
	// Instance to the Singleton class the manage the environment

	categoryDatabase = env.getEnvironment("default").openDatabase(null,
		CATEGORY_TABLE_STORE, dbConfig);

	categoryName_SecondaryDatabse = env.getEnvironment("default")
		.openSecondaryDatabase(null, CATEGORY_SECONDARY_TABLE,
			categoryDatabase, SecConfig);
	log.debug("category database opened");

    }

    public void close() throws DatabaseException {
	this.categoryDatabase.close();
	this.categoryName_SecondaryDatabse.close();
	log.debug("category database closed");
    }

    public void flushDatabase() throws DatabaseException {
	log.info("flushing category database");
	env.getEnvironment(CATEGORY_SECONDARY_TABLE).truncateDatabase(null,
		CATEGORY_SECONDARY_TABLE, false);

    }

}
