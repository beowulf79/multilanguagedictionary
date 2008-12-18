package net.verza.jdict.sleepycat;

/**
 * @author ChristianVerdelli
 *
 */

import net.verza.jdict.ArabWord;
import net.verza.jdict.ItalianWord;
import net.verza.jdict.UserProfile;
import net.verza.jdict.Word;
import net.verza.jdict.sleepycat.datastore.SleepyClassCatalogDatabase;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;

public class SleepyBinding {

	private static EntryBinding WordDataBinding;
	private static EntryBinding ItalianWordDataBinding;
	private static EntryBinding ArabWordDataBinding;
	private static EntryBinding UserProfileDataBinding;
	private static EntryBinding VerbDataBinding;
	
	static public EntryBinding getWordDataBinding() {
		WordDataBinding = new SerialBinding(getClassCatalog(), Word.class);
		return WordDataBinding;
	}

	static public EntryBinding getItalianWordDataBinding() {
		ItalianWordDataBinding = new SerialBinding(getClassCatalog(),
				ItalianWord.class);
		return ItalianWordDataBinding;
	}

	static public EntryBinding getArabWordDataBinding() {
		ArabWordDataBinding = new SerialBinding(getClassCatalog(),
				ArabWord.class);
		return ArabWordDataBinding;
	}
	
	
	static public EntryBinding getVerbDataBinding() {
		VerbDataBinding = new SerialBinding(getClassCatalog(),
				testClass.copy.verbs.Verb.class);
		return VerbDataBinding;
	}
	

	static public EntryBinding getUserProfiledDataBinding() {
		UserProfileDataBinding = new SerialBinding(getClassCatalog(),
				UserProfile.class);
		return UserProfileDataBinding;
	}

	private static StoredClassCatalog getClassCatalog() {
		SleepyClassCatalogDatabase catalog = null;
		try {
			catalog = SleepyClassCatalogDatabase.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return catalog.getClassCatalog();
	}

	
	
}