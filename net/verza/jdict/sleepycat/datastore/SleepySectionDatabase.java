package net.verza.jdict.sleepycat.datastore;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.FileNotFoundException;

import net.verza.jdict.sleepycat.datastore.indexes.SleepySectionDatabaseIndexKeyCreator;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.DatabaseException;

public class SleepySectionDatabase {

	// Primary Database
	private static final String CLASS_CATALOG = "java_class_catalog";
	private static final String SECTION_TABLE_STORE = "sectionTable";
	private static final String SECTION_STRING = "section_string";

	private StoredClassCatalog javaCatalog;

	private SleepyEnvironment env;

	// This holds the serialized class
	private Database catalogDb;

	private Database sectionDB;

	// this Secondary database is used as index to perform
	// lookup based on the Section String
	private SecondaryDatabase sectionName_SecondaryDatabse;

	/**
	 * Open all storage containers, indices, and catalogs.
	 */
	public SleepySectionDatabase(SleepyEnvironment env)
			throws DatabaseException, FileNotFoundException {

		// Set the Berkeley DB config for opening all stores.
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);

		sectionDB = env.getEnvironment().openDatabase(null,
				SECTION_TABLE_STORE, dbConfig);

		SecondaryConfig SecConfig = new SecondaryConfig();
		SecConfig.setTransactional(true);
		SecConfig.setAllowCreate(true); // Create the database if it does not
										// already exist.
		SecConfig.setAllowPopulate(true); // Allow auto populate
		SecConfig.setSortedDuplicates(true);

		SleepySectionDatabaseIndexKeyCreator sectionName_Index = new SleepySectionDatabaseIndexKeyCreator(
				this);
		SecConfig.setKeyCreator(sectionName_Index);
		sectionName_SecondaryDatabse = env.getEnvironment()
				.openSecondaryDatabase(null, SECTION_STRING, sectionDB,
						SecConfig);
		/*
		 * Create the Serial class catalog. This holds the serialized class
		 * format for all database records of serial format.
		 */
		catalogDb = env.getEnvironment().openDatabase(null, CLASS_CATALOG,
				dbConfig);
		javaCatalog = new StoredClassCatalog(catalogDb);

	}

	/**
	 * Return the class catalog.
	 */
	public final StoredClassCatalog getClassCatalog() {

		return javaCatalog;
	}

	/**
	 * Return the part storage container.
	 */
	public final Database getSectionDatabase() {
		return this.sectionDB;
	}

	public final SecondaryDatabase getSectionName_SecondaryDatabase() {
		return this.sectionName_SecondaryDatabse;
	}

	/**
	 * Close all databases and the environment.
	 */
	public void close() throws DatabaseException {
		sectionDB.close();
		javaCatalog.close();
		env.close();
	}
}
