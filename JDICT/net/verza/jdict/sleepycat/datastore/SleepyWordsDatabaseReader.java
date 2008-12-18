package net.verza.jdict.sleepycat.datastore;

import java.util.Vector;
import net.verza.jdict.ArabWord;
import net.verza.jdict.ItalianWord;
import net.verza.jdict.Word;
import net.verza.jdict.IWord;
import net.verza.jdict.sleepycat.SleepyBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryCursor;
import com.sleepycat.je.JoinCursor;
import com.sleepycat.je.SecondaryDatabase;
import org.apache.log4j.Logger;
import java.io.UnsupportedEncodingException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.DataNotFoundException;

/**
 * @author ChristianVerdelli
 * 
 */
public class SleepyWordsDatabaseReader {

	private SleepyWordsDatabase db;
	private SleepyWordsDatabasePrimaryCursor primaryCursor;
	private JoinCursor joinCursor;
	private SecondaryCursor[] secondaryCursorArray;
	private Logger log;
	private int arrayPositionCounter; // array used to store secondary index to be used with joinCursor
	private Vector<Word> wkey;
	private Vector<Word> wdata;
	private int databaseSize; // store the size of database

	
	
	public SleepyWordsDatabaseReader(SleepyWordsDatabase wordsDatabase)
			throws DatabaseException {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());
		db = wordsDatabase;
		primaryCursor = new SleepyWordsDatabasePrimaryCursor(db);
		wkey = new Vector<Word>();
		wdata = new Vector<Word>();
		databaseSize = 0;
		arrayPositionCounter = 0;
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

	
	
	
	public void setSecondaryCursor(SecondaryDatabase secdb, DatabaseEntry key)
			throws DatabaseException, UnsupportedEncodingException,
			DataNotFoundException {

		log.trace("setting Secondary Cursor to " + secdb.getDatabaseName());
		SleepyWordsDatabaseSecondaryXCursor Xcursor = new SleepyWordsDatabaseSecondaryXCursor(
				secdb);
		this.addSecondaryCursor(Xcursor.getSecondaryCursor(key));
		this.count(); // reset DB size counter after adding index

	}

	
	
	public void count() throws DatabaseException {

		DatabaseEntry theKey = new DatabaseEntry();
		DatabaseEntry theData = new DatabaseEntry();
		int counter = 0;

		joinCursor = db.getDictDatabase().join(this.secondaryCursorArray, null);
		while (joinCursor.getNext(theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			counter++;
		}
		log.debug("updating DB size counter to: " + counter);
		this.databaseSize = counter;

	}
	
	
	
	public Word getKey(int index) throws DataNotFoundException {

		
		log.trace("returning the key at position " + index);
		if (this.wkey == null) {
			log.error("the key is null, throwing exception");
			throw new DataNotFoundException("the Key Vector is empty");
		}
		return this.wkey.get(index);
	}

	
	
	public Vector<Word> getKey() throws DataNotFoundException {

		log.trace("returning all the keys");
		if (this.wkey == null) {
			log.error("the key is null, throwing exception");
			throw new DataNotFoundException("the Key Vector is empty");
		}
		return this.wkey;
	}

	
	
	public IWord getData(int index) throws DataNotFoundException {

		log.trace("returning the data at position " + index);
		if (this.wdata == null) {
			log.error("the Data is null, throwing exception");
			throw new DataNotFoundException("the Key Vector is empty");
		}
		return this.wdata.get(index);
	}

	
	
	public Vector<Word> getData() throws DataNotFoundException {

		log.trace("returning all the data");
		if (this.wdata == null) {
			log.error("the Data is null, throwing exception");
			throw new DataNotFoundException("the Key Vector is empty");
		}
		return this.wdata;
	}

	
	/*
	 * this routines checks if any the Secondary Cursor Array has been
	 * populated, if so it uses the JoinCursor otherwise it will use the Cursor
	 * on the primary database
	 */
	public int read() throws DatabaseException, DynamicCursorException {

		log.trace("called method read");
		// let's clear the array returned by any previous search
		this.wkey.clear();
		this.wdata.clear();

		if (this.arrayPositionCounter < 0) {
			log.error("received invalid index");
			return -1;
		}

		else if (this.arrayPositionCounter > 0) {
			log.warn("looking for data using JoinCursor");
			this.readJoin();

		} else if (this.arrayPositionCounter == 0) {
			log.warn("looking for data using Primary Cursor");
			primaryCursor.read();
			// let's clear the array returned by any previous search
			this.wkey = primaryCursor.getKey();
			this.wdata = primaryCursor.getData();

		}

		this.arrayPositionCounter = 0; // reset the counter of the
										// SecondaryDatabase array
		this.secondaryCursorArray = null; // clear the SecondaryDatabase Array for the
									// next lookup
		return 0;
	}

	
	
	private void readJoin() throws DatabaseException, DynamicCursorException {

		DatabaseEntry theKey = new DatabaseEntry();
		DatabaseEntry theData = new DatabaseEntry();

		if (this.secondaryCursorArray == null)
			throw new DynamicCursorException(
					"trying to execute read join with an empty cursorArray");

		log.debug("querying using "+secondaryCursorArray.length+ " cursor/cursors)");
		for (int i = 0; i < secondaryCursorArray.length; i++){ 
	         SecondaryCursor tmp = secondaryCursorArray[i];
	         log.debug("joined cursor "+tmp.getDatabase().getDatabaseName());
	    }
		
		joinCursor = db.getDictDatabase().join(this.secondaryCursorArray, null);
		log.trace("inside join cursor");
		while (joinCursor.getNext(theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			ItalianWord iw = (ItalianWord) SleepyBinding
					.getItalianWordDataBinding().entryToObject(theKey);
			ArabWord aw = (ArabWord) SleepyBinding.getArabWordDataBinding()
					.entryToObject(theData);
			wkey.addElement(iw);
			wdata.addElement(aw);
		}

	}

	
	
	public String toString() {
		return "Key Vector Size" + this.wkey.size() + "Data Vector Size "
				+ this.wdata.size();
	}

}