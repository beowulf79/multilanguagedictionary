package net.verza.jdict.sleepycat.datastore;

import net.verza.jdict.UserProfile;
import org.apache.log4j.Logger;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseException;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.OperationStatus;


public class SleepyUsersDatabaseWriter {

	private Database user_db;
	private Logger log;

	public SleepyUsersDatabaseWriter(Database db) {
		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName());
		user_db = db;
	}

	public int writeUser(UserProfile up) throws DatabaseException,
			UnsupportedEncodingException, FileNotFoundException {

		DatabaseEntry keyEntry = new DatabaseEntry(up.getName().getBytes(
				"UTF-8"));
		DatabaseEntry dataEntry = new DatabaseEntry();

		EntryBinding dataBinding = new com.sleepycat.bind.serial.SerialBinding(SleepyClassCatalogDatabase.getInstance().getClassCatalog(),
				UserProfile.class);

		dataBinding.objectToEntry(up, dataEntry);

		if (user_db.put(null, keyEntry, dataEntry) == OperationStatus.SUCCESS)

			log.info("successFully stored into db " + user_db.getDatabaseName()
					+ " UserProfile having key:" + up.getName() + ": Value "
					+ up.toString());

		else {
			log.error("couldn't write user profile into the db "
					+ user_db.getDatabaseName());
			return -1;
		}

		return 0;
	}

}
