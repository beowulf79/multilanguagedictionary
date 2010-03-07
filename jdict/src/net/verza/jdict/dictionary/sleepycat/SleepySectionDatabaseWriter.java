package net.verza.jdict.dictionary.sleepycat;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.OperationStatus;

public class SleepySectionDatabaseWriter {

    private SleepySectionDatabase database;
    private static Logger log;

    public SleepySectionDatabaseWriter(SleepySectionDatabase _db) {
	database = _db;
	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());
    }

    public int writeData(String section_id, String section_String)
	    throws DatabaseException, UnsupportedEncodingException {

	DatabaseEntry keyEntry = new DatabaseEntry();
	DatabaseEntry dataEntry = new DatabaseEntry();
	keyEntry = new DatabaseEntry(section_id.getBytes("UTF-8"));
	dataEntry = new DatabaseEntry(section_String.getBytes("UTF-8"));
	log.debug("writing into section db key " + section_id + " value "
		+ section_String);

	if (database.getSectionDatabase().put(null, keyEntry, dataEntry)
		.equals(OperationStatus.SUCCESS)) {
	    log.info("successFully put into db "
		    + database.getSectionDatabase().getDatabaseName()
		    + "using key :" + section_id + " value " + section_String);
	} else {
	    log.error("error writing into section db using Key :" + section_id
		    + " Value " + section_String);
	    return -1;
	}

	return 0;
    }

}
