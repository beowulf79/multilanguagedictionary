package net.verza.jdict.sleepycat.datastore;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import org.apache.log4j.Logger;
import java.io.UnsupportedEncodingException;

public class SleepyCategoryDatabaseReader {

	private SleepyCategoryDatabase dbHandler;
	private CursorConfig config;
	private Cursor cursor;
	private int cursorEntriesCounter;
	private String[] key;
	private String[] data;
	private Logger log;

	public SleepyCategoryDatabaseReader(SleepyCategoryDatabase _db)
			throws DatabaseException, UnsupportedEncodingException {

		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName());
		this.dbHandler = _db;
		this.config = new CursorConfig();
		this.config.setReadUncommitted(true);
		this.cursor = this.dbHandler.getCategoryDatabase().openCursor(null, null);
		this.countDatabaseEntry();
		this.key = new String[cursorEntriesCounter + 1];
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
		log.debug("looking category database using key " + key);

		searchKey = new DatabaseEntry(key.getBytes("UTF-8"));

		// looking for the key on the primary database (category ID)
		log.debug("using category index as key");
		if (this.dbHandler.getCategoryDatabase().get(null, searchKey, foundData, LockMode.DEFAULT) 
				== OperationStatus.SUCCESS) {
			log.info("found category by Index: "
					+ new String(foundData.getData(), "UTF-8"));
			return new String(foundData.getData(), "UTF-8");
		}

		// looking for the key on the secondary database index (category Name)
		log.debug("using category name as key");
		if (this.dbHandler.getCategoryName_SecondaryDatabase().get(null, searchKey, foundKey,
				foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			log.info("found category by Name: "
					+ new String(foundKey.getData(), "UTF-8"));
			return new String(foundKey.getData(), "UTF-8");
		}

		log.warn("category not found");
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
			key[0] = (new String(foundKey.getData(), "UTF-8"));
			data[0] = (new String(foundData.getData(), "UTF-8"));

			int count = 1;
			while ((cursor.getNext(foundKey, foundData, LockMode.DEFAULT)) == OperationStatus.SUCCESS) {
				// log.debug("foundData.getData()" +new
				// String(foundData.getData(), "UTF-8"));
				// log.debug("foundKey.getData()" +new
				// String(foundKey.getData(), "UTF-8"));
				key[count] = (new String(foundKey.getData(), "UTF-8"));
				data[count] = (new String(foundData.getData(), "UTF-8"));
				count++;
			}
		} else
			log.error("error positioning the cursor on the category database");

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
		log.trace("retuning all the key");
		return this.key;
	}

	public String[] getData() {
		log.trace("retuning all the data");
		return this.data;
	}

	public void closeCursors() throws DatabaseException {
		this.cursor.close();

	}

	public void openCursors()	throws DatabaseException	{
		this.cursor = this.dbHandler.getCategoryDatabase().openCursor(null, null);
	}

}
