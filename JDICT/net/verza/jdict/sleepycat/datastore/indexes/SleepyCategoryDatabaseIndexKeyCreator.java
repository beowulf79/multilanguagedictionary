package net.verza.jdict.sleepycat.datastore.indexes;

import org.apache.log4j.Logger;

import net.verza.jdict.sleepycat.datastore.SleepyCategoryDatabase;

import com.sleepycat.je.SecondaryKeyCreator;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.DatabaseEntry;

/**
 * @author ChristianVerdelli
 * 
 */
public class SleepyCategoryDatabaseIndexKeyCreator implements
		SecondaryKeyCreator {

	//private SleepyCategoryDatabase db;	
	private static Logger log;
	private final static String index_name = "CATEGORY_STRING";

	
	public SleepyCategoryDatabaseIndexKeyCreator(
			SleepyCategoryDatabase categoryMapTableDatabase) {
		//db = categoryMapTableDatabase;
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());
	}

	
	public boolean createSecondaryKey(SecondaryDatabase secondary,
			DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {

		if (data.getData() == null) {
			log.error("can't not set up index" + index_name
					+ " the entry");
			return false;
		} else {
			result.setData(data.getData());
			log.info("setting " + index_name
					+ " index for the entry with value " + data.getData());
		}
		return true;
	}
}
