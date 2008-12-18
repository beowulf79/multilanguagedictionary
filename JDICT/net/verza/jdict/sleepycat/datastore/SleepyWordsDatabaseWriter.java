package net.verza.jdict.sleepycat.datastore;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.OperationStatus;
import org.apache.log4j.Logger;
import net.verza.jdict.sleepycat.SleepyBinding;
import net.verza.jdict.IWord;

/**
 * @author ChristianVerdelli
 * 
 */
public class SleepyWordsDatabaseWriter {

	private SleepyWordsDatabase database;
	private Logger log;

	
	public SleepyWordsDatabaseWriter(SleepyWordsDatabase db) {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		database = db;

	}

	
	public int write(IWord l1, IWord l2) throws DatabaseException {

		DatabaseEntry keyEntry = new DatabaseEntry();
		DatabaseEntry dataEntry = new DatabaseEntry();
		EntryBinding kBind = SleepyBinding.getItalianWordDataBinding();
		EntryBinding dBind = SleepyBinding.getArabWordDataBinding();
		kBind.objectToEntry(l1, keyEntry);
		dBind.objectToEntry(l2, dataEntry);
		if (database.getDictDatabase().put(null, keyEntry, dataEntry).equals(
				OperationStatus.SUCCESS))
			log.info("successFully stored into database key :"+ l1.getsingular() + 
						" value " + l2.getsingular());
		else {
			log.error("cannot write data into database "
						+this.database.getDictDatabase().getDatabaseName());
			return -1;
		}

		return 0;
	}

	
	public int write(IWord[] l1, IWord[] l2) throws DatabaseException {
		if(l1.length != l2.length)	{
			log.error("cannot write into db; keys and values arrays are not of the same size" );
			return -1;
		}
		for(int i=0;i<l1.length;i++)	{
			this.write(l1[i], l2[i]);
		}
		
		return 0;
	}
	
	
}
