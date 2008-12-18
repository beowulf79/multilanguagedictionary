package net.verza.jdict.sleepycat.datastore.indexes;

/**
 * 
 */

import com.sleepycat.je.SecondaryKeyCreator;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.DatabaseEntry;
import java.io.IOException;
import net.verza.jdict.ItalianWord;
import net.verza.jdict.sleepycat.SleepyBinding;
import org.apache.log4j.Logger;

/**
 * @author ChristianVerdelli
 * 
 */
public class itSingularIndexKeyCreator implements SecondaryKeyCreator {

	private final static String index_name = "ITALIAN_SINGULAR";
	private static Logger log;

	
	public itSingularIndexKeyCreator() {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());
	}

	
	public boolean createSecondaryKey(SecondaryDatabase secondary,
			DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {
		try {
			ItalianWord pd = (ItalianWord) SleepyBinding
					.getItalianWordDataBinding().entryToObject(key);
			if (pd.getsingular() == null) {
				log.error("can't not set up index" + index_name
						+ " the entry");
			} else {
				result.setData(pd.getsingular().getBytes("UTF-8"));
				log.info("setting " + index_name
						+ " index for the entry");
			}
		} catch (IOException willNeverOccur) {
		}

		return true;
	}
}
