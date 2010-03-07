package net.verza.jdict.dictionary.sleepycat;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.FileNotFoundException;

import net.verza.jdict.dictionary.sleepycat.indexes.SleepySectionDatabaseIndexKeyCreator;

import org.apache.log4j.Logger;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;

public class SleepySectionDatabase {

    // Primary Database

    private static final String SECTION_TABLE_STORE = "sectionTable";
    private static final String SECTION_SECONDARY_TABLE = "section_string";
    private SleepyEnvironment env;
    private DatabaseConfig dbConfig;
    private SecondaryConfig SecConfig;
    private Database databaseSection;
    private SecondaryDatabase sectionName_SecondaryDatabse;
    private Logger log;

    /**
     * Open all storage containers, indices, and catalogs.
     */
    public SleepySectionDatabase() throws DatabaseException,
	    FileNotFoundException {

	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());

	// Set the Berkeley DB config for opening all stores.
	dbConfig = new DatabaseConfig();
	dbConfig.setTransactional(false);
	dbConfig.setAllowCreate(true);

	env = SleepyEnvironment.getInstance(); // Instance to the Singleton
	// class the manage the
	// environment

	SecConfig = new SecondaryConfig();
	SecConfig.setTransactional(false);
	SecConfig.setAllowCreate(true); // Create the database if it does not
	// already exist.
	SecConfig.setAllowPopulate(true); // Allow auto populate
	SecConfig.setSortedDuplicates(true);

	SleepySectionDatabaseIndexKeyCreator sectionName_Index = new SleepySectionDatabaseIndexKeyCreator(
		this);
	SecConfig.setKeyCreator(sectionName_Index);

	this.open();

    }

    /**
     * Return the part storage container.
     */
    public final Database getSectionDatabase() {
	return this.databaseSection;
    }

    public final SecondaryDatabase getSectionName_SecondaryDatabase() {
	return this.sectionName_SecondaryDatabse;
    }

    public void open() throws DatabaseException {

	this.databaseSection = env.getEnvironment("default").openDatabase(null,
		SECTION_TABLE_STORE, dbConfig);

	this.sectionName_SecondaryDatabse = env.getEnvironment("default")
		.openSecondaryDatabase(null, SECTION_SECONDARY_TABLE,
			databaseSection, SecConfig);
	log.debug("section database opened");

    }

    public void close() throws DatabaseException {

	this.sectionName_SecondaryDatabse.close();
	this.databaseSection.close();
	log.debug("section database closed");

    }

    public void flushDatabase() throws DatabaseException {
	log.info("flushing section database");
	env.getEnvironment(SECTION_SECONDARY_TABLE).truncateDatabase(null,
		SECTION_SECONDARY_TABLE, false);

    }

}
