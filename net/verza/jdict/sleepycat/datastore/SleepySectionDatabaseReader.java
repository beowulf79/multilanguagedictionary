package net.verza.jdict.sleepycat.datastore;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryDatabase;
import org.apache.log4j.Logger;
import java.io.UnsupportedEncodingException;

public class SleepySectionDatabaseReader {

	private SleepySectionDatabase dbHandler;
	private Database sectionDB;
	private SecondaryDatabase sectionName_Secondary_index_DB;
	private CursorConfig config;
	private Cursor cursor;
	private int cursorEntriesCounter;
	private String[] keys;
	private String[] data;
	private Logger log;

	public SleepySectionDatabaseReader(SleepySectionDatabase db)
			throws DatabaseException, UnsupportedEncodingException {

		this.log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		this.log.trace("called class " + this.getClass().getName());
		this.dbHandler = db;
		this.sectionDB = dbHandler.getSectionDatabase();
		this.sectionName_Secondary_index_DB = dbHandler
				.getSectionName_SecondaryDatabase();
		this.config = new CursorConfig();
		this.config.setReadUncommitted(true);
		this.cursor = sectionDB.openCursor(null, null);
		this.countDatabaseEntry();
		this.keys = new String[cursorEntriesCounter + 1];
		this.data = new String[cursorEntriesCounter + 1];
		this.initialize();

	}

	public int getCount() {
		return cursorEntriesCounter;
	}

	public String searchCategory(String key) throws DatabaseException,
			UnsupportedEncodingException {

		DatabaseEntry searchKey = null;
		DatabaseEntry foundKey = new DatabaseEntry();
		DatabaseEntry foundData = new DatabaseEntry();

		searchKey = new DatabaseEntry(key.getBytes("UTF-8"));

		// looking for the key on the primary database (section ID)
		log.trace("looking section database using key " + key);
		if (sectionDB.get(null, searchKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			log.info("found section by Index: "
					+ new String(foundData.getData(), "UTF-8"));
			return new String(foundData.getData(), "UTF-8");
		}

		// looking for the key on the secondary database index (section Name)
		log.trace("using section name as key");
		if (sectionName_Secondary_index_DB.get(null, searchKey, foundKey,
				foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			log.info("found section by Name: "
					+ new String(foundKey.getData(), "UTF-8"));
			return new String(foundKey.getData(), "UTF-8");
		}

		log.warn("section not found");
		return null;
	}

	private void initialize() throws DatabaseException,
			UnsupportedEncodingException {

		DatabaseEntry foundKey = new DatabaseEntry();
		DatabaseEntry foundData = new DatabaseEntry();

		// Position the cursor on the first entry
		if ((cursor.getFirst(foundKey, foundData, LockMode.DEFAULT)) == OperationStatus.SUCCESS) {
			// log.debug("foundData.getData()" +new String(foundData.getData(),
			// "UTF-8"));
			// log.debug("foundKey.getData()" +new String(foundKey.getData(),
			// "UTF-8"));
			keys[0] = (new String(foundKey.getData(), "UTF-8"));
			data[0] = (new String(foundData.getData(), "UTF-8"));

			int count = 1;
			while ((cursor.getNext(foundKey, foundData, LockMode.DEFAULT)) == OperationStatus.SUCCESS) {
				// log.debug("foundData.getData()" +new
				// String(foundData.getData(), "UTF-8"));
				// log.debug("foundKey.getData()" +new
				// String(foundKey.getData(), "UTF-8"));
				keys[count] = (new String(foundKey.getData(), "UTF-8"));
				data[count] = (new String(foundData.getData(), "UTF-8"));
				count++;
			}
		} else
			log.error("error positioning the cursor on the section database");

	}

	private void countDatabaseEntry() throws DatabaseException {

		int count;

		count = 0;
		DatabaseEntry keyEntry = new DatabaseEntry();
		DatabaseEntry dataEntry = new DatabaseEntry();
		cursor.getFirst(keyEntry, dataEntry, LockMode.READ_UNCOMMITTED);
		while (cursor.getNext(keyEntry, dataEntry, LockMode.READ_UNCOMMITTED) == OperationStatus.SUCCESS) {
			count++;
		}
		log.debug("setting database size to " + count);
		this.cursorEntriesCounter = count;

	}

	public String[] getKey() {
		log.trace("returning all the keys");
		return this.keys;
	}

	public String[] getData() {
		log.trace("returning all the data");
		return this.data;
	}

}
