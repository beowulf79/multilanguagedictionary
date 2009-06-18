package net.verza.jdict.sleepycat.datastore;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.DatabaseException;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;

public class SleepyCategoryDatabaseWriter {

	private SleepyCategoryDatabase database;
	private static Logger log;
	
	public SleepyCategoryDatabaseWriter(SleepyCategoryDatabase _db) {
		database = _db;
		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName());
	}

	public int writeData(String category_id, String category_String)
			throws DatabaseException, UnsupportedEncodingException {

		DatabaseEntry keyEntry = new DatabaseEntry();
		DatabaseEntry dataEntry = new DatabaseEntry();
		keyEntry = new DatabaseEntry(category_id.getBytes("UTF-8"));
		dataEntry = new DatabaseEntry(category_String.getBytes("UTF-8"));

		if (database.getCategoryDatabase().put(null, keyEntry, dataEntry).equals(
				OperationStatus.SUCCESS)) {
			log.info("successFully put into DB Key :" + category_id
					+ " Value " + category_String);
		} else {
			log.error("error while writing the DB");
			return -1;
		}

		return 0;
	}

}
