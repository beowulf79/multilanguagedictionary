package net.verza.jdict.sleepycat.datastore;

import java.util.Vector;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryCursor;
import com.sleepycat.je.JoinCursor;
import com.sleepycat.je.SecondaryDatabase;
import org.apache.log4j.Logger;
import java.io.UnsupportedEncodingException;

import net.verza.jdict.SearchableObject;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.DataNotFoundException;

/**
 * @author ChristianVerdelli
 * 
 */
public class SleepyDatabaseReader {

	private SleepyDatabase db;
	private SleepyDatabasePrimaryCursor primaryCursor;
	private JoinCursor joinCursor;
	private SecondaryCursor[] secondaryCursorArray;
	private Logger log;
	private int arrayPositionCounter; // array used to store secondary index to be used with joinCursor
	private Vector<SearchableObject> wdata;
	private int databaseSize; // store the size of database
	private EntryBinding dataBinding;

	
	public SleepyDatabaseReader() {
		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName());
		wdata = new Vector<SearchableObject>();
		databaseSize = 0;
		arrayPositionCounter = 0;
	}

	public SleepyDatabaseReader(SleepyDatabase _sleepydb)
			throws DatabaseException {

		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName()
				+ " with argument SleepyVerbsDatabase " + _sleepydb.toString());
		db = _sleepydb;
		this.setPrimaryCursor();
		wdata = new Vector<SearchableObject>();
		databaseSize = 0;
		arrayPositionCounter = 0;

	}

	public void setDatabase(SleepyDatabase _db) throws DatabaseException {
		this.db = _db;
		this.setPrimaryCursor();
	}

	public int getSize() throws DatabaseException {
		if (this.arrayPositionCounter > 0) {
			this.count();
			return this.databaseSize;
		} else if (this.arrayPositionCounter == 0) {
			databaseSize = primaryCursor.getDatabaseSize();
			return this.databaseSize;
		} else
			log.error("error counting database entries");
		return -1;
	}

	public void setKey(String _key) throws UnsupportedEncodingException {
		log.debug("setting key " + _key);
		primaryCursor.setKey(_key);
	}

	private void setPrimaryCursor() throws DatabaseException {
		primaryCursor = new SleepyDatabasePrimaryCursor(this.db);
	}

	private void addSecondaryCursor(SecondaryCursor cursor) {
		log.debug("Extending SecondaryCursor array");

		if (this.arrayPositionCounter == 0) {
			secondaryCursorArray = new SecondaryCursor[++this.arrayPositionCounter];
			log.trace("adding SecondaryCursor position one ");
			secondaryCursorArray[secondaryCursorArray.length - 1] = cursor;
		} else {
			SecondaryCursor[] tmp = this.secondaryCursorArray;
			this.arrayPositionCounter++;
			log.debug("array index counter " + this.arrayPositionCounter);
			secondaryCursorArray = new SecondaryCursor[this.arrayPositionCounter];
			int counter = 0;
			while (counter < tmp.length) {
				secondaryCursorArray[counter] = tmp[counter];
				counter++;
			}
			log.trace("adding SecondaryCursor at position "
					+ secondaryCursorArray.length);
			secondaryCursorArray[secondaryCursorArray.length - 1] = cursor;
		}

	}


	public void setSecondaryCursor(String _index, DatabaseEntry key)
	throws DatabaseException, UnsupportedEncodingException,
	DataNotFoundException {
		this.setSecondaryCursor(db.getIndex(_index), key);
	}

		
	
	public void setSecondaryCursor(SecondaryDatabase secdb, DatabaseEntry key)
			throws DatabaseException, UnsupportedEncodingException,
			DataNotFoundException {

		log.trace("setting Secondary Cursor to " + secdb.getDatabaseName());
		SleepyDatabaseSecondaryXCursor Xcursor = new SleepyDatabaseSecondaryXCursor(
				secdb);
		this.addSecondaryCursor(Xcursor.getSecondaryCursor(key));
		this.count(); // reset DB size counter after adding index
	}

	public void count() throws DatabaseException {

		DatabaseEntry theKey = new DatabaseEntry();
		DatabaseEntry theData = new DatabaseEntry();
		int counter = 0;

		joinCursor = db.getDatabase().join(this.secondaryCursorArray, null);
		while (joinCursor.getNext(theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			counter++;
		}
		log.debug("updating DB size counter to: " + counter);
		this.databaseSize = counter;

	}

	public SearchableObject getData(int index) throws DataNotFoundException {

		log.trace("returning the data at position " + index);
		if (this.wdata == null) {
			log.error("the Data is null, throwing exception");
			throw new DataNotFoundException("the Key Vector is empty");
		}
		return this.wdata.get(index);
	}

	public Vector<SearchableObject> getData() throws DataNotFoundException {

		log.trace("returning all the data");
		if (this.wdata == null) {
			log.error("the Data is null, throwing exception");
			throw new DataNotFoundException("the Key Vector is empty");
		}
		return this.wdata;
	}

	public int read() throws DatabaseException, DynamicCursorException {

		log.trace("called method read");

		//let's clear the array returned by any previous search
		this.wdata.clear();

		if (this.arrayPositionCounter < 0) {
			log.error("received invalid index");
			return -1;
		}

		else if (this.arrayPositionCounter > 0) {
			log.warn("looking for data using JoinCursor");
			wdata = this.readJoin();

		} else if (this.arrayPositionCounter == 0) {
			log.warn("looking for data using Primary Cursor");
			primaryCursor.setDataBinding(this.dataBinding);
			primaryCursor.read();
			//need this to avoid duplicate records
			wdata = primaryCursor.getData();
		}

		this.arrayPositionCounter = 0; // reset the counter of the
		// SecondaryDatabase array
		this.secondaryCursorArray = null; // clear the SecondaryDatabase Array for the
		// next lookup

		log.trace("returning wdata vector size of " + this.wdata.size());
		return this.wdata.size();

	}

	public void setDataBinding(EntryBinding eb) {
		log.debug("setting key binding with object " + eb.hashCode());
		this.dataBinding = eb;
	}

	private Vector<SearchableObject> readJoin() throws DatabaseException,
			DynamicCursorException {

		DatabaseEntry theKey = new DatabaseEntry();
		DatabaseEntry theData = new DatabaseEntry();
		Vector<SearchableObject> local_wdata = new Vector<SearchableObject>();

		if (this.secondaryCursorArray == null)
			throw new DynamicCursorException(
					"trying to execute read join with an empty cursorArray");

		log.debug("querying using " + secondaryCursorArray.length
				+ " cursor/cursors)");
		for (int i = 0; i < secondaryCursorArray.length; i++) {
			SecondaryCursor tmp = secondaryCursorArray[i];
			log.debug("joined cursor " + tmp.getDatabase().getDatabaseName());
		}

		joinCursor = db.getDatabase().join(this.secondaryCursorArray, null);
		log.trace("inside join cursor");
		while (joinCursor.getNext(theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			SearchableObject obj = (SearchableObject) this.dataBinding
					.entryToObject(theData);
			local_wdata.addElement(obj);
		}

		return local_wdata;

	}

	public String toString() {
		return "Data Vector Size " + this.wdata.size();
	}

}