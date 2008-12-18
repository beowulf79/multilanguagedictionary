package net.verza.jdict.sleepycat.datastore.indexes;

/**
 * 
 */

import com.sleepycat.je.SecondaryKeyCreator;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.DatabaseEntry;
import java.io.IOException;

import org.apache.log4j.Logger;

import net.verza.jdict.ItalianWord;
import net.verza.jdict.sleepycat.SleepyBinding;

/**
 * @author ChristianVerdelli
 * 
 */
public class IDIndexCreator implements SecondaryKeyCreator {

	private final static String index_name = "ID";
	private static Logger log;

	/*
	 * This class is used to run search on the ID Field. The Category Field is
	 * shared between ArabWord and Italian word so i create the index JUST for
	 * ItalianWord; this is all to review though.
	 */

	public IDIndexCreator() {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());
	}

	public boolean createSecondaryKey(SecondaryDatabase secondary,
			DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {
		try {
			ItalianWord pd = (ItalianWord) SleepyBinding
					.getItalianWordDataBinding().entryToObject(key);
			if (pd.getid() == null) {
				log.error("can't not set up index" + index_name + " the entry");
			} else {
				result.setData(pd.getid().getBytes("UTF-8"));
				log.info("Setting " + index_name + " index for the entry");
			}
		} catch (IOException willNeverOccur) {
		}

		return true;
	}

}
