package net.verza.jdict.dictionary.sleepycat;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import net.verza.jdict.model.UserProfile;
import net.verza.jdict.properties.Configuration;

import org.apache.log4j.Logger;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class SleepyUsersDatabaseReader {

    private SleepyUsersDatabase dbHandler;
    private Database user_db;
    private CursorConfig config;
    private Cursor cursor;
    private String[] userList;
    private Logger log;
    private EntryBinding<UserProfile> dataBinding;

    public SleepyUsersDatabaseReader(SleepyUsersDatabase db)
	    throws DatabaseException, FileNotFoundException {

	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());
	dbHandler = db;
	dataBinding = db.getEntryBinding();

	user_db = dbHandler.getUsersDatabase();

    }

    public UserProfile readUser(String key) throws DatabaseException,
	    UnsupportedEncodingException {
	log.trace("called function readUser with key " + key);

	String aKey = (String) key;
	DatabaseEntry theData = new DatabaseEntry();
	OperationStatus retVal;

	DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
	log.debug("looking for user profile with key:" + aKey);

	retVal = user_db.get(null, theKey, theData, LockMode.DEFAULT);
	if (retVal.equals(OperationStatus.NOTFOUND)) {
	    log.error("user profile not found");
	    return null;
	}
	log.info("user profile " + aKey + " found");

	return (UserProfile) dataBinding.entryToObject(theData);

    }

    public String[] getUserList() throws DatabaseException {
	log.trace("called function getUserList");

	String[] tmpUserList = new String[Configuration.MAX_USERS_PROFILES];
	DatabaseEntry foundKey = new DatabaseEntry();
	DatabaseEntry foundData = new DatabaseEntry();
	int count = 0;
	config = new CursorConfig();
	config.setReadUncommitted(true);
	log.debug("initializing users database cursors on database "
		+ dbHandler.getUsersDatabase().count());
	cursor = dbHandler.getUsersDatabase().openCursor(null, null);
	log.debug("cursor initialized");

	while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
	    UserProfile up = (UserProfile) dataBinding.entryToObject(foundData);
	    tmpUserList[count] = up.getName();
	    log.debug("adding user profile " + up.getName()
		    + " to the users profiles list");
	    count++;
	}
	log.debug("found " + count + " users profiles");
	cursor.close();
	userList = new String[count];
	System.arraycopy(tmpUserList, 0, userList, 0, count);

	return userList;
    }
}
