package net.verza.jdict.dictionary.sleepycat;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;

public class SleepyClassCatalogDatabase {

    private static Logger log;
    private static final String CLASS_CATALOG = "java_class_catalog";
    private static SleepyClassCatalogDatabase ClassCatalog = null;

    private SleepyEnvironment sleepyEnv;
    private Database catalogDb;
    private StoredClassCatalog javaCatalog;

    /**
     * Open all storage containers, indices, and catalogs.
     */
    public SleepyClassCatalogDatabase() throws DatabaseException,
	    FileNotFoundException {

	log = Logger.getLogger("dictionary");
	sleepyEnv = SleepyEnvironment.getInstance();
	// Instance to the class the manage the environment

	// Set the Berkeley DB config for opening all stores.
	DatabaseConfig dbConfig = new DatabaseConfig();
	dbConfig.setTransactional(false);
	dbConfig.setAllowCreate(true);

	catalogDb = sleepyEnv.getEnvironment("class_catalog").openDatabase(
		null, CLASS_CATALOG, dbConfig);
	javaCatalog = new StoredClassCatalog(catalogDb);

    }

    /*
     * return singleton Object
     */
    public static SleepyClassCatalogDatabase getInstance()
	    throws DatabaseException, FileNotFoundException {
	if (ClassCatalog == null) {
	    ClassCatalog = new SleepyClassCatalogDatabase();
	}

	return ClassCatalog;
    }

    /**
     * Return the class catalog.
     */
    public final StoredClassCatalog getClassCatalog() {

	return javaCatalog;
    }

    /**
     * Close all databases and the environment.
     */
    public void close() throws DatabaseException {
	javaCatalog.close();

    }
}
