package net.verza.jdict.sleepycat.datastore;

/**
 * @author ChristianVerdelli
 *
 */

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryCursor;
import java.io.UnsupportedEncodingException;
import net.verza.jdict.exceptions.DataNotFoundException;
import org.apache.log4j.Logger;

public class SleepyWordsDatabaseSecondaryXCursor {

	private Logger log;
	private SecondaryCursor SecCursor;
	DatabaseEntry searchKey;

	public SleepyWordsDatabaseSecondaryXCursor(SecondaryDatabase database)
			throws DatabaseException {

		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("SleepyDynamicXSecondaryCursor constructor called");
		SecCursor = database.openSecondaryCursor(null, null);

	}

	public int setKey(String key) throws UnsupportedEncodingException {

		log.trace("setting Key " + key);
		this.searchKey = new DatabaseEntry(key.getBytes("UTF-8"));
		return 0;
	}

	public SecondaryCursor getSecondaryCursor(DatabaseEntry key)
			throws DatabaseException, UnsupportedEncodingException,
			DataNotFoundException {

		DatabaseEntry foundKey = new DatabaseEntry();
		DatabaseEntry foundData = new DatabaseEntry();

		if (SecCursor.getSearchKey(key, foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS)
			log.debug("successfully positioned the cursor "
					+ SecCursor.getDatabase().getDatabaseName()
					+ " with the key " + new String(key.getData(), "UTF-8"));
		else {
			log.debug("key not found, cursor "
					+ SecCursor.getDatabase().getDatabaseName()
					+ " not positioned, throwing exception");
			throw new DataNotFoundException(
					"cursor not position due to inexistence key");
		}
		return SecCursor;
	}

	/**
	 * Return the number of the entries inside the database To obtain this must
	 * go through all the database with a cursor !!!
	 */
	public int getSize() throws DatabaseException {

		log.trace("resetting Database Entry Counter");
		DatabaseEntry foundData = new DatabaseEntry();

		if (SecCursor.getSearchKey(this.searchKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS)
			return SecCursor.count();

		return -1;
	}

}
