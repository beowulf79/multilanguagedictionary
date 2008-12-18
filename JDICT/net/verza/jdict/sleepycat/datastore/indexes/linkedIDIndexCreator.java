package net.verza.jdict.sleepycat.datastore.indexes;

/**
 * 
 */

import org.apache.log4j.Logger;
import net.verza.jdict.ItalianWord;
import net.verza.jdict.sleepycat.SleepyBinding;
import com.sleepycat.je.SecondaryKeyCreator;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.DatabaseEntry;

/**
 * @author ChristianVerdelli
 * 
 */
public class linkedIDIndexCreator implements SecondaryKeyCreator {

	private final static String index_name = "LINKEDID";
	private static Logger log;

	public linkedIDIndexCreator() {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());
	}

	public boolean createSecondaryKey(SecondaryDatabase secondary,
			DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {
		ItalianWord pd = (ItalianWord) SleepyBinding
				.getItalianWordDataBinding().entryToObject(key);
		if (pd.getid().equals(null)) {
			log.error("can't not set up index" + index_name + " the entry");
		} else {

			// result.setData(pd.getLinkID()));
			log.info("Setting " + index_name + " index for the entry");
		}

		return true;
	}

}
