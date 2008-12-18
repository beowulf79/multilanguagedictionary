package net.verza.jdict.sleepycat.datastore.indexes;

import com.sleepycat.je.SecondaryMultiKeyCreator;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.bind.tuple.StringBinding;
import java.util.Set;
import java.util.Iterator;
import org.apache.log4j.Logger;
import net.verza.jdict.ItalianWord;
import net.verza.jdict.sleepycat.SleepyBinding;

/**
 * @author ChristianVerdelli
 * 
 */

public class CategoryIndexCreator implements SecondaryMultiKeyCreator {

	private final static String index_name = "CATEGORY_MULTI_VALUE";
	private static Logger log;

	public CategoryIndexCreator() {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());
	}

	/**
	 * Returns the set of email addresses for a person. This is an example of a
	 * multi key creator for a to-many index.
	 */
	public void createSecondaryKeys(SecondaryDatabase secondary,
			DatabaseEntry key, DatabaseEntry data, Set results)
			throws DatabaseException {


		ItalianWord pd = (ItalianWord) SleepyBinding
				.getItalianWordDataBinding().entryToObject(key);
		
		System.out.println("pd.getcategory() "+pd.getsection().size() );
		copyKeysToEntries(pd.getcategory(), results);

	}

	/**
	 * A utility method to copy a set of keys (Strings) into a set of
	 * DatabaseEntry objects.
	 */
	private void copyKeysToEntries(Set<String> keys, Set<DatabaseEntry> entries) {
		StringBinding keyBinding = new StringBinding();
		log.info("Setting multi value " + index_name + " index for the entry");
		for (Iterator<String> i = keys.iterator(); i.hasNext();) {
			String s = (String) i.next();
			log.debug("index value " + s);
			DatabaseEntry entry = new DatabaseEntry();
			keyBinding.objectToEntry(s, entry);
			entries.add(entry);
		}
	}
	
	

}
