package net.verza.jdict.sleepycat.datastore;

/**
 * @author ChristianVerdelli
 *
 */

import net.verza.jdict.ArabWord;
import net.verza.jdict.Word;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredEntrySet;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.DatabaseException;
import java.io.FileNotFoundException;

/**
 * Defines the data bindings and collection views for the database.
 * 
 * @author Verdelli Christian
 */
public class SleepyWordsDatabaseViews {

	private StoredMap dictMap;

	/**
	 * Create the data bindings and collection views.
	 */
	public SleepyWordsDatabaseViews(SleepyWordsDatabase db)
			throws DatabaseException, FileNotFoundException {

		SleepyClassCatalogDatabase catalog = null;
		catalog = SleepyClassCatalogDatabase.getInstance();

		EntryBinding dictKeyBinding = new SerialBinding(catalog
				.getClassCatalog(), Word.class);
		EntryBinding dictDataBinding = new SerialBinding(catalog
				.getClassCatalog(), ArabWord.class);

		// Create map views for all stores and indices.
		dictMap = new StoredMap(db.getDictDatabase(), dictKeyBinding,
				dictDataBinding, true);

	}

	/**
	 * Return a map view of the dictionary storage container.
	 */
	public final StoredMap getDictMap() {

		return dictMap;
	}

	/**
	 * Return an entry set view of the dictionary storage container.
	 */
	public final StoredEntrySet getDictEntrySet() {

		return (StoredEntrySet) dictMap.entrySet();
	}

}
