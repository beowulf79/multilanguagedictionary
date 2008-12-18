package net.verza.jdict.sleepycat.datastore;

/**
 * @author ChristianVerdelli
 *
 */

import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import java.util.Vector;
import net.verza.jdict.sleepycat.SleepyBinding;
import net.verza.jdict.Word;
import org.apache.log4j.*;

public class SleepyWordsDatabasePrimaryCursor {

	private CursorConfig config;
	private Cursor cursor;
	private int dbsize; // store the entries number in the db
	private Logger log;
	private Vector<Word> wkey = new Vector<Word>();
	private Vector<Word> wdata = new Vector<Word>();

	public SleepyWordsDatabasePrimaryCursor(SleepyWordsDatabase db)
			throws DatabaseException {

		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());
		config = new CursorConfig();
		config.setReadUncommitted(true);
		cursor = db.getDictDatabase().openCursor(null, null);
		dbsize = 0;
		

	}

	public Vector<Word> getKey() {
		return this.wkey;
	}

	public Vector<Word> getData() {
		return this.wdata;
	}

	public Cursor getCursor() {
		return cursor;
	}

	public int getDatabaseSize() throws DatabaseException {
		countDatabaseEntry();
		return this.dbsize;
	}

	/**
	 * Count the number of the entries inside the database
	 */
	private void countDatabaseEntry() throws DatabaseException {

		DatabaseEntry keyEntry = new DatabaseEntry();
		DatabaseEntry dataEntry = new DatabaseEntry();

		while (cursor.getNext(keyEntry, dataEntry, LockMode.READ_UNCOMMITTED) == OperationStatus.SUCCESS) {
			dbsize++;
		}

		log
				.debug("database primary cursor returning number of entries (achieved by an incremental counter) "
						+ dbsize);

	}

	/**
	 * Return the tuple at position
	 */
	public void read() throws DatabaseException {

		log.trace("called method doSearch");
		// let's clear the array returned by any previous search
		DatabaseEntry foundKey = new DatabaseEntry();
		DatabaseEntry foundData = new DatabaseEntry();
		
		Word w1 = new Word();
		Word w2 = new Word();
		cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);
		w1 = (Word) SleepyBinding.getItalianWordDataBinding().entryToObject(
				foundKey);
		wkey.addElement(w1);
		w2 = (Word) SleepyBinding.getArabWordDataBinding().entryToObject(
				foundData);
		wdata.addElement(w2);

		while ((cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS)) {
			w1 = (Word) SleepyBinding.getItalianWordDataBinding()
					.entryToObject(foundKey);
			wkey.addElement(w1);
			w2 = (Word) SleepyBinding.getArabWordDataBinding().entryToObject(
					foundData);
			wdata.addElement(w2);
			log.debug("adding to Vector key with ID " + w1.getid()
					+ " and data with ID " + w2.getid());
			wkey.addElement(w1);
			wdata.addElement(w2);

		}
		

	}

}
