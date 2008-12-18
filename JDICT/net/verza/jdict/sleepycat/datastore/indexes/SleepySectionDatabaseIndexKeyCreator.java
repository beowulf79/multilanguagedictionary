package net.verza.jdict.sleepycat.datastore.indexes;

import org.apache.log4j.Logger;

import net.verza.jdict.sleepycat.datastore.SleepySectionDatabase;

import com.sleepycat.je.SecondaryKeyCreator;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.DatabaseEntry;

/**
 * @author ChristianVerdelli
 * 
 */
public class SleepySectionDatabaseIndexKeyCreator implements
		SecondaryKeyCreator {

	//SleepySectionDatabase db;
	private static Logger log;
	private final static String index_name = "SECTION_STRING";

	public SleepySectionDatabaseIndexKeyCreator(
			SleepySectionDatabase sectionMapTableDatabase) {
		//db = sectionMapTableDatabase;
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());
	}

	public boolean createSecondaryKey(SecondaryDatabase secondary,
			DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {

		if (data.getData() == null) {
			log.error("can't not set up index" + index_name
					+ " the entry");
		} else {
			result.setData(data.getData());
			log.info("setting " + index_name
					+ " index for the entry with value " + data.getData());
		}

		return true;
	}
}
