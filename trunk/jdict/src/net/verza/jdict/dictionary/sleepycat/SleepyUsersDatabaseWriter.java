package net.verza.jdict.dictionary.sleepycat;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import net.verza.jdict.model.UserProfile;

import org.apache.log4j.Logger;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.OperationStatus;

public class SleepyUsersDatabaseWriter {

    private Database database;
    private SleepyUsersDatabase users_database;
    private Logger log;

    public SleepyUsersDatabaseWriter(SleepyUsersDatabase db) {
	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());
	users_database = db;
	database = users_database.getUsersDatabase();
    }

    public void writeUser(UserProfile up) throws DatabaseException,
	    UnsupportedEncodingException, FileNotFoundException {

	DatabaseEntry keyEntry = new DatabaseEntry(up.getName().getBytes(
		"UTF-8"));
	DatabaseEntry dataEntry = new DatabaseEntry();
	EntryBinding<UserProfile> dataBinding = users_database
		.getEntryBinding();
	dataBinding.objectToEntry(up, dataEntry);
	log.debug("writing into db " + database.getDatabaseName() + "key "
		+ up.getName() + " value" + up.toString());

	if (database.put(null, keyEntry, dataEntry) == OperationStatus.SUCCESS) {
	    log.info("successFully stored into db "
		    + database.getDatabaseName() + " UserProfile having key:"
		    + up.getName() + ": Value " + up.toString());
	} else {
	    log.error("error writing into " + database.getDatabaseName()
		    + " db using Key " + up.getName());
	}
	database.getEnvironment().sync();

    }

    public void deleteUser(UserProfile up) throws DatabaseException,
	    UnsupportedEncodingException, FileNotFoundException {
	DatabaseEntry keyEntry = new DatabaseEntry(up.getName().getBytes(
		"UTF-8"));
	if (database.delete(null, keyEntry) == OperationStatus.SUCCESS)
	    log.info("successFully delete " + " UserProfile having "
		    + up.getName());
	else {
	    log.error("failed delete user profile "
		    + database.getDatabaseName());
	}
	database.getEnvironment().sync();

    }

}
