package net.verza.jdict.sleepycat.datastore;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import net.verza.jdict.SearchableObject;
import net.verza.jdict.utils.Utility;

import org.apache.log4j.Logger;


import java.io.UnsupportedEncodingException;

/**
 * @author ChristianVerdelli
 * 
 */
public class SleepyDatabaseWriter {

	private SleepyDatabase database;
	private Logger log;
	private EntryBinding dataBinding;

	
	public SleepyDatabaseWriter() {
		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName());
	}
	
	
	public SleepyDatabaseWriter(SleepyDatabase db)
			throws DatabaseException {
		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName() + "wth database "
				+ db.getDatabase().getDatabaseName());
		database = db;

	}

	
	public void setDataBinding(EntryBinding eb) {
		log.debug("setting key binding with object " + eb.hashCode());
		this.dataBinding = eb;
	}

	
	public void setDatabase(SleepyDatabase _db) {
		try {
			log.debug("setting db to " + _db.getDatabase().getDatabaseName());
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		this.database = _db;
	}

	
	public int write(Integer key, SearchableObject data) throws DatabaseException {

		//DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("UTF-8"));
		DatabaseEntry keyEntry = new DatabaseEntry(Utility.intToByteArray(key));
		DatabaseEntry dataEntry = new DatabaseEntry();

		dataBinding.objectToEntry(data, dataEntry);
		if (database.getDatabase().put(null, keyEntry, dataEntry).equals(
				OperationStatus.SUCCESS))
			log.info("successFully stored into database key :" + key
					+ " value " + data.toString());
		else {
			log.error("cannot write data into database "
					+ this.database.getDatabase().getDatabaseName());
			return -1;
		}
		OperationStatus op = database.getDatabase().get(null, keyEntry,
				dataEntry, LockMode.DEFAULT);

		System.out.println("Operation Status  " + op);

		return 0;
	}

	
	public int write(String key, SearchableObject data) throws DatabaseException {

		try {

			DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("UTF-8"));
			DatabaseEntry dataEntry = new DatabaseEntry();

			dataBinding.objectToEntry(data, dataEntry);
			
			if (database.getDatabase().put(null, keyEntry, dataEntry).equals(
					OperationStatus.SUCCESS))
				log.info("successFully stored into database "
						+ database.getDatabase().getDatabaseName() + "key :"
						+ key + " value " + data.toString() + " into database "
						+ database.getDatabase().getDatabaseName());
			else {
				log.error("cannot write data into database "
						+ this.database.getDatabase().getDatabaseName());
				return -1;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return 0;
	}

	
	public int write(String[] keys, SearchableObject[] data) throws DatabaseException {
		log.trace("key array size "+keys.length
						+ " values array size "+data.length);
		if (keys.length != data.length) {
			log
					.error("cannot write into db; keys and values arrays are not of the same size");
			return -1;
		}
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] == null) {
				log.info("null key found, stop writing");
				return 0;
			}
			log.debug("calling write method with key " + keys[i]
					+ "and data size " + data[i].toString());
			//when a null key is found it means it has done with the writing.
			

			this.write(keys[i], data[i]);
		}
		
		log.info("successfully imported into database "+database.getDatabase().getDatabaseName()
				+" entries "+keys.length);
		return 0;
	}

}
