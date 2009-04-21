package net.verza.jdict.sleepycat.datastore;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.FileNotFoundException;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.DatabaseException;
import net.verza.jdict.sleepycat.datastore.indexes.SleepyCategoryDatabaseIndexKeyCreator;

/**
 * SampleDatabase defines the storage containers, indices and foreign keys for
 * the sample database.
 * 
 * @author Mark Hayes
 */
public class SleepyCategoryDatabase {

	// Primary Database
	private static final String CLASS_CATALOG = "java_class_catalog";
	private static final String CATEGORY_TABLE_STORE = "categoryTable";
	private static final String CATEGORY_STRING = "category_string";

	private StoredClassCatalog javaCatalog;

	private SleepyEnvironment env;

	// This holds the serialized class
	private Database catalogDb;

	private Database categoryDB;

	// this Secondary database is used as index to perform
	// lookup based on the Category String
	private SecondaryDatabase categoryName_SecondaryDatabse;

	/**
	 * Open all storage containers, indices, and catalogs.
	 */
	public SleepyCategoryDatabase(SleepyEnvironment env)
			throws DatabaseException, FileNotFoundException {

		// Set the Berkeley DB config for opening all stores.
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);

		categoryDB = env.getEnvironment().openDatabase(null,
				CATEGORY_TABLE_STORE, dbConfig);

		SecondaryConfig SecConfig = new SecondaryConfig();
		SecConfig.setTransactional(true);
		SecConfig.setAllowCreate(true); // Create the database if it does not
										// already exist.
		SecConfig.setAllowPopulate(true); // Allow autopopulate
		SecConfig.setSortedDuplicates(true);

		SleepyCategoryDatabaseIndexKeyCreator categoryName_Index = new SleepyCategoryDatabaseIndexKeyCreator(
				this);
		SecConfig.setKeyCreator(categoryName_Index);
		categoryName_SecondaryDatabse = env.getEnvironment()
				.openSecondaryDatabase(null, CATEGORY_STRING, categoryDB,
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
	public final Database getCategoryDatabase() {
		return this.categoryDB;
	}

	public final SecondaryDatabase getCategoryName_SecondaryDatabase() {
		return this.categoryName_SecondaryDatabse;
	}

	/**
	 * Close all databases and the environment.
	 */
	public void close() throws DatabaseException {
		categoryDB.close();
		javaCatalog.close();
		env.close();
	}
}
