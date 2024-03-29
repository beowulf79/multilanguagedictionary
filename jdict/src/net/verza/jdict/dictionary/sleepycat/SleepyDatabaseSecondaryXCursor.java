package net.verza.jdict.dictionary.sleepycat;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.UnsupportedEncodingException;

import net.verza.jdict.exceptions.DataNotFoundException;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryCursor;
import com.sleepycat.je.SecondaryDatabase;

public class SleepyDatabaseSecondaryXCursor {

    private Logger log;
    private SecondaryCursor SecCursor;
    DatabaseEntry searchKey;

    public SleepyDatabaseSecondaryXCursor(SecondaryDatabase database)
	    throws DatabaseException {

	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());
	SecCursor = database.openSecondaryCursor(null, null);

    }

    public int setKey(String key) throws UnsupportedEncodingException {

	log.debug("setting key " + key);
	this.searchKey = new DatabaseEntry(key.getBytes("UTF-8"));
	return 0;
    }

    public SecondaryCursor getSecondaryCursor(DatabaseEntry key)
	    throws DatabaseException, UnsupportedEncodingException,
	    DataNotFoundException {

	DatabaseEntry foundKey = new DatabaseEntry();
	DatabaseEntry foundData = new DatabaseEntry();

	log.debug("key that will be used to position the cursor  "
		+ new String(key.getData(), "UTF-8"));

	if (SecCursor.getSearchKey(key, foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS)
	    log.debug("successfully positioned the cursor "
		    + SecCursor.getDatabase().getDatabaseName()
		    + " with the key " + new String(key.getData(), "UTF-8"));
	else {
	    log.debug("key not found on the secondary database "
		    + SecCursor.getDatabase().getDatabaseName());

	}
	return SecCursor;
    }

    /**
     * Return the number of the entries inside the database To obtain this must
     * go through all the database with a cursor !!!
     */
    public int getDatabaseEntryCounter() throws DatabaseException {

	log.trace("resetting Database Entry Counter");
	DatabaseEntry foundData = new DatabaseEntry();

	if (SecCursor.getSearchKey(this.searchKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS)
	    return SecCursor.count();

	return -1;
    }

    public void close() throws DatabaseException {
	this.SecCursor.close();
    }

}
