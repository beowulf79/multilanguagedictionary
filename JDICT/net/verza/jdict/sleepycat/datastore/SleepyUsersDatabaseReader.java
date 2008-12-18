package net.verza.jdict.sleepycat.datastore;

import net.verza.jdict.UserProfile;
import net.verza.jdict.sleepycat.SleepyBinding;
import java.io.UnsupportedEncodingException;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import org.apache.log4j.Logger;
import net.verza.jdict.Configuration;

public class SleepyUsersDatabaseReader {

	private SleepyUsersDatabase dbHandler;
	private Database user_db;
	private CursorConfig config;
	private Cursor cursor;
	private String[] userlist;
	private Logger log;

	public SleepyUsersDatabaseReader(SleepyUsersDatabase db)
			throws DatabaseException {

		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());
		dbHandler = db;
		user_db = dbHandler.getUsersDatabase();
		config = new CursorConfig();
		config.setReadUncommitted(true);
		cursor = db.getUsersDatabase().openCursor(null, config);
		buildUsersList();

	}

	public UserProfile readUser(String key) throws DatabaseException,
			UnsupportedEncodingException {

		String aKey = (String) key;
		DatabaseEntry theData = new DatabaseEntry();
		OperationStatus retVal;

		DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
		log.info("looking for user profile with key:" + aKey);

		retVal = user_db.get(null, theKey, theData, LockMode.DEFAULT);
		if (retVal.equals(OperationStatus.NOTFOUND)) {
			log.error("user profile not found");
			return null;
		}
		log.info("user profile found");

		return (UserProfile) SleepyBinding.getUserProfiledDataBinding()
				.entryToObject(theData);

	}

	public String[] getUserList() {

		return userlist;
	}

	private void buildUsersList() throws DatabaseException {

		log.debug("building user list");
		userlist = new String[Configuration.MAX_USERS_PROFILES];
		DatabaseEntry foundKey = new DatabaseEntry();
		DatabaseEntry foundData = new DatabaseEntry();
		int count = 0;
		
		while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			UserProfile up = (UserProfile) SleepyBinding
					.getUserProfiledDataBinding().entryToObject(foundData);
			userlist[count] = up.getName();
			log.debug("found user profile " + up.getName());
			count++;
		}
		log.trace("found "+count+" users profiles");
		
	}

}
