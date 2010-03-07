package net.verza.jdict.dictionary.sleepycat;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.OperationStatus;

public class SleepyCategoryDatabaseWriter {

    private SleepyCategoryDatabase database;
    private static Logger log;

    public SleepyCategoryDatabaseWriter(SleepyCategoryDatabase _db) {
	database = _db;
	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());
    }

    public int writeData(String category_id, String category_String)
	    throws DatabaseException, UnsupportedEncodingException {

	DatabaseEntry keyEntry = new DatabaseEntry();
	DatabaseEntry dataEntry = new DatabaseEntry();
	keyEntry = new DatabaseEntry(category_id.getBytes("UTF-8"));
	dataEntry = new DatabaseEntry(category_String.getBytes("UTF-8"));
	log.debug("writing into category db key " + category_id + " value "
		+ category_String);

	if (database.getCategoryDatabase().put(null, keyEntry, dataEntry)
		.equals(OperationStatus.SUCCESS)) {
	    log
		    .info("successFully put into db "
			    + database.getCategoryDatabase().getDatabaseName()
			    + "using key :" + category_id + " value "
			    + category_String);
	    log.info("successFully put into DB Key :" + category_id + " Value "
		    + category_String);
	} else {
	    log.error("error writing into category db using Key :"
		    + category_id + " Value " + category_String);
	    return -1;
	}

	return 0;
    }

}
