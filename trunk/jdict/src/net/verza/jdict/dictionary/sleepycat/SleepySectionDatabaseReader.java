package net.verza.jdict.dictionary.sleepycat;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class SleepySectionDatabaseReader {

    private SleepySectionDatabase dbHandler;
    // private Database database;
    // private SecondaryDatabase sectionName_Secondary_index_Database;
    private CursorConfig config;
    private Cursor cursor;
    private int cursorEntriesCounter;
    private String[] keys;
    private String[] data;
    private Logger log;

    public SleepySectionDatabaseReader(SleepySectionDatabase db)
	    throws DatabaseException, UnsupportedEncodingException {

	this.log = Logger.getLogger("dictionary");
	this.log.trace("called class " + this.getClass().getName());
	this.dbHandler = db;
	this.config = new CursorConfig();
	this.config.setReadUncommitted(true);
	this.openCursors();
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
	log.debug("looking section database using primary key " + key);
	if (this.dbHandler.getSectionDatabase().get(null, searchKey, foundData,
		LockMode.DEFAULT) == OperationStatus.SUCCESS) {
	    log.debug("found section : "
		    + new String(foundData.getData(), "UTF-8") + " using key "
		    + key);
	    return new String(foundData.getData(), "UTF-8");
	}
	log.debug("section not found using key: " + key);

	// looking for the key on the secondary database index (section Name)
	log.debug("looking section database using index and key: " + key);
	if (this.dbHandler.getSectionName_SecondaryDatabase().get(null,
		searchKey, foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
	    log.debug("found section : "
		    + new String(foundKey.getData(), "UTF-8"));
	    return new String(foundKey.getData(), "UTF-8");
	}
	log.debug("section not found using index and key: " + key);

	return null;
    }

    public void initialize() throws DatabaseException,
	    UnsupportedEncodingException {

	DatabaseEntry foundKey = new DatabaseEntry();
	DatabaseEntry foundData = new DatabaseEntry();
	this.countDatabaseEntry();
	this.keys = new String[cursorEntriesCounter + 1];
	this.data = new String[cursorEntriesCounter + 1];
	// Position the cursor on the first entry
	if ((cursor.getFirst(foundKey, foundData, LockMode.DEFAULT)) == OperationStatus.SUCCESS) {
	    keys[0] = (new String(foundKey.getData(), "UTF-8"));
	    data[0] = (new String(foundData.getData(), "UTF-8"));

	    int count = 1;
	    while ((cursor.getNext(foundKey, foundData, LockMode.DEFAULT)) == OperationStatus.SUCCESS) {
		keys[count] = (new String(foundKey.getData(), "UTF-8"));
		data[count] = (new String(foundData.getData(), "UTF-8"));
		log.debug("adding to section array key "
			+ new String(foundKey.getData(), "UTF-8")
			+ " and value "
			+ new String(foundData.getData(), "UTF-8"));
		count++;
	    }

	} else
	    log
		    .error("error positioning the cursor on the section database; is it empty?");

    }

    private void countDatabaseEntry() throws DatabaseException {

	int count = 0;
	DatabaseEntry keyEntry = new DatabaseEntry();
	DatabaseEntry dataEntry = new DatabaseEntry();
	cursor.getFirst(keyEntry, dataEntry, LockMode.READ_UNCOMMITTED);
	while (cursor.getNext(keyEntry, dataEntry, LockMode.READ_UNCOMMITTED) == OperationStatus.SUCCESS) {
	    count++;
	}
	this.cursorEntriesCounter = count;
	log.info("section database size is: " + count);
    }

    public String[] getKey() {
	return this.keys;
    }

    public String[] getData() {
	return this.data;
    }

    public void closeCursors() throws DatabaseException {
	this.cursor.close();

    }

    public void openCursors() throws DatabaseException {
	this.cursor = this.dbHandler.getSectionDatabase()
		.openCursor(null, null);
    }

}
