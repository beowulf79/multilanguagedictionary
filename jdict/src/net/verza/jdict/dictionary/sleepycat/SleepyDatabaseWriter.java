package net.verza.jdict.dictionary.sleepycat;

import java.io.UnsupportedEncodingException;

import net.verza.jdict.exceptions.DatabaseImportException;
import net.verza.jdict.model.SearchableObject;
import net.verza.jdict.utils.Utility;

import org.apache.log4j.Logger;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.OperationStatus;

/**
 * @author ChristianVerdelli
 * 
 */
public class SleepyDatabaseWriter {

    private SleepyDatabase database;
    private Logger log;
    private EntryBinding dataBinding;

    public SleepyDatabaseWriter() {
	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());
    }

    public SleepyDatabaseWriter(SleepyDatabase db) throws DatabaseException {
	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName() + "wth database "
		+ db.getDatabase().getDatabaseName());
	database = db;
	dataBinding = db.getEntryBinding();

    }

    // public void setDataBinding(EntryBinding eb) {
    // log.debug("setting key binding with object " + eb.hashCode());
    // // this.dataBinding = eb;
    // }

    public void setDatabase(SleepyDatabase _db) throws DatabaseException {

	log.debug("setting db to " + _db.getDatabase().getDatabaseName());
	this.database = _db;
	dataBinding = _db.getEntryBinding();
    }

    /**
     * put a new word in the database without knowing the primary key. This
     * function will ask to the database for the first id (primary key)
     * available and will store the word using the id.
     * 
     * @param data
     *                a SearchableObject to store in the database
     * @return
     * @throws UnsupportedEncodingException
     */
    public void write(SleepyDatabase db, SearchableObject data)
	    throws DatabaseException, UnsupportedEncodingException {
	log.trace("called function write with database "
		+ db.getDatabase().getDatabaseName() + "  value "
		+ data.toString());
	setDatabase(db);
	Integer key = db.getFreeId();
	log.debug("got available id " + key);
	write(key, data);
    }

    public void write(SleepyDatabase db, Integer key, SearchableObject data)
	    throws DatabaseException, UnsupportedEncodingException {
	log.trace("called function write with database "
		+ db.getDatabase().getDatabaseName() + "  value "
		+ data.toString());
	setDatabase(db);
	write(key, data);
    }

    public void write(Integer key, SearchableObject data)
	    throws DatabaseException {

	log.trace("called method write(key,data)");
	DatabaseEntry keyEntry = new DatabaseEntry(Utility.intToByteArray(key));
	DatabaseEntry dataEntry = new DatabaseEntry();
	dataBinding.objectToEntry(data, dataEntry);

	if (database.getDatabase().put(null, keyEntry, dataEntry).equals(
		OperationStatus.SUCCESS)) {
	    log.info("successFully stored into database "
		    + database.getDatabase().getDatabaseName() + "key :" + key
		    + " value " + data.toString() + " into database "
		    + database.getDatabase().getDatabaseName());

	} else {
	    log.error("cannot write data into database "
		    + this.database.getDatabase().getDatabaseName());
	}
	database.getDatabase().getEnvironment().sync();
    }

    public void write(String key, SearchableObject data)
	    throws DatabaseException, UnsupportedEncodingException {
	log.trace("called method write(key,data)");

	DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("UTF-8"));
	DatabaseEntry dataEntry = new DatabaseEntry();
	dataBinding.objectToEntry(data, dataEntry);

	if (database.getDatabase().put(null, keyEntry, dataEntry).equals(
		OperationStatus.SUCCESS))
	    log.info("successFully stored into database "
		    + database.getDatabase().getDatabaseName() + "key :" + key
		    + " value " + data.toString() + " into database "
		    + database.getDatabase().getDatabaseName());
	else {
	    log.error("cannot write data into database "
		    + this.database.getDatabase().getDatabaseName());

	}

	database.getDatabase().getEnvironment().sync();
    }

    public int write(Integer[] keys, SearchableObject[] data)
	    throws DatabaseException, DatabaseImportException {

	int successfullImportedCounter = 0;
	log.trace("key array size " + keys.length + " values array size "
		+ data.length);
	if (keys.length != data.length) {
	    log
		    .error("cannot write into db; keys and values arrays are not of the same size");
	    throw new DatabaseImportException(
		    " keys and values arrays have different size");
	}
	for (int i = 0; i < keys.length; i++) {
	    // when a null key is found it means it has done with
	    // the writing.
	    if (keys[i] == null) {
		log.info("null key found, stop writing");
		return -1;
	    }
	    log.debug("calling write method with key " + keys[i]
		    + "and data size " + data[i].toString());
	    this.write(keys[i], data[i]);
	    successfullImportedCounter++;
	}

	log.info("successfully imported into database "
		+ database.getDatabase().getDatabaseName() + " entries "
		+ keys.length);

	return successfullImportedCounter;
    }

    // public SleepyDatabase getDatabase() {
    // return database;
    // }

}
