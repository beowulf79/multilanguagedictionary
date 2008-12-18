package net.verza.jdict.sleepycat.datastore;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.FileNotFoundException;
import net.verza.jdict.sleepycat.datastore.indexes.CategoryIndexCreator;
import net.verza.jdict.sleepycat.datastore.indexes.IDIndexCreator;
import net.verza.jdict.sleepycat.datastore.indexes.SectionIndexCreator;
import net.verza.jdict.sleepycat.datastore.indexes.arPluralIndexKeyCreator;
import net.verza.jdict.sleepycat.datastore.indexes.arSingularIndexKeyCreator;
import net.verza.jdict.sleepycat.datastore.indexes.itSingularIndexKeyCreator;
import net.verza.jdict.sleepycat.SleepyEnvironment;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.DatabaseException;

public class SleepyWordsDatabase {

	// Primary Database
	private static final String DICTIONARY_STORE = "dictionary";
	// Secondary DBs used for indexes
	private static final String ID_INDEX = "ID_PRIMARY_index";
	private static final String IT_WORD_INDEX = "itSing_part_index";
	private static final String AR_SING_INDEX = "ar_Sing_part_index";
	private static final String AR_PLUR_INDEX = "ar_Plur_part_index";
	private static final String CATEGORY_INDEX = "category_index";
	private static final String SECTION_INDEX = "section_index";
	private static SleepyEnvironment sleepyEnv;
	private static Database dict;
	private static DatabaseConfig dbConfig;
	private static SecondaryConfig SecConfig;
	private static SecondaryConfig secX2ManyConfig;
	private static SecondaryDatabase ID_Index;
	private static SecondaryDatabase itSingularIndex;
	private static SecondaryDatabase arSingularIndex;
	private static SecondaryDatabase arPluralIndex;
	private static SecondaryDatabase categoryIndex;
	private static SecondaryDatabase sectionIndex;

	
	/**
	 * Open all storage containers, indices, and catalogs.
	 */
	public SleepyWordsDatabase() throws DatabaseException,
			FileNotFoundException {

		sleepyEnv = SleepyEnvironment.getInstance();// Instance to the Singleton
													// class the manage the
													// environment

		// Set the Berkeley DB config for opening all stores.
		dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);

		dict = sleepyEnv.getEnvironment().openDatabase(null, DICTIONARY_STORE,
				dbConfig);

		// Secondary Database-Indexes Section

		// Create Secondary Databse to index Arab Singular Enviroment and
		// Configuration
		SecConfig = new SecondaryConfig();
		SecConfig.setTransactional(true);
		SecConfig.setAllowCreate(true); // Create the database if it does not
										// already exist.
		SecConfig.setAllowPopulate(true); // Allow autopopulate
		SecConfig.setSortedDuplicates(true); // Need to allow duplicates for
												// our secondary database

		// Open the secondary database. We use this to create a secondary index
		// for the inventory database
		arSingularIndex = null;

		// Open the secondary database. We use this to create a secondary index
		// for the inventory database
		arPluralIndex = null;

		// Open the secondary database. We use this to create a secondary index
		// for the italian singular database
		itSingularIndex = null;

		// Open the secondary database. Used to create an index for the singular
		// italian field
		ID_Index = null;

		// SecondaryConfig for x-to-many database
		secX2ManyConfig = new SecondaryConfig();
		secX2ManyConfig.setAllowCreate(true);
		secX2ManyConfig.setTransactional(true);
		secX2ManyConfig.setAllowPopulate(true);
		secX2ManyConfig.setSortedDuplicates(true);

		// Open the secondary database. Used to create an index for the category
		// field
		categoryIndex = null;

		/*
		 * Open the secondary database for Section. This is a one-to-many index
		 * because duplicates are not configured.
		 */
		sectionIndex = null;

	}

	
	/**
	 * Return the part storage container.
	 */
	public Database getDictDatabase() {
		return dict;
	}

	
	/*
	 * Return the secondary Index Database.
	 */
	public static final SecondaryDatabase getAR_sing_IndexDatabase()
			throws DatabaseException {
		if (arSingularIndex != null)
			return arSingularIndex;

		arSingularIndexKeyCreator arSkeyCreator = new arSingularIndexKeyCreator();
		SecConfig.setKeyCreator(arSkeyCreator);
		arSingularIndex = sleepyEnv.getEnvironment().openSecondaryDatabase(
				null, AR_SING_INDEX, dict, SecConfig);
		return arSingularIndex;
	}

	
	
	public static final SecondaryDatabase getAR_plur_IndexDatabase()
			throws DatabaseException {
		if (arPluralIndex != null)
			return arPluralIndex;
		arPluralIndexKeyCreator arPkeyCreator = new arPluralIndexKeyCreator();
		SecConfig.setKeyCreator(arPkeyCreator); // Specifies the user-supplied
												// object used for creating
												// single-valued secondary keys.
		arPluralIndex = sleepyEnv.getEnvironment().openSecondaryDatabase(null,
				AR_PLUR_INDEX, dict, SecConfig);

		return arPluralIndex;
	}

	
	
	public static final SecondaryDatabase getIT_sing_IndexDatabase()
			throws DatabaseException {
		if (itSingularIndex != null)	{
			System.out.println("not null IT index, returning the index");
			return itSingularIndex;

		}
	
		System.out.println("NULL Italian Singular index found, calling index Creator class");
		itSingularIndexKeyCreator itkeyCreator = new itSingularIndexKeyCreator();
		SecConfig.setKeyCreator(itkeyCreator);
		itSingularIndex = sleepyEnv.getEnvironment().openSecondaryDatabase(
				null, IT_WORD_INDEX, dict, SecConfig);

		return itSingularIndex;
	}

	
	
	/*
	 * Created just on the ItalianWord object because its a field shared with
	 * ArabWord
	 */
	public static final SecondaryDatabase getID_IndexDatabase()
			throws DatabaseException {
		if (ID_Index != null)
			return ID_Index;
		IDIndexCreator IDCreator = new IDIndexCreator();
		SecConfig.setKeyCreator(IDCreator); // Specifies the user-supplied
											// object used for creating
											// single-valued secondary keys.
		ID_Index = sleepyEnv.getEnvironment().openSecondaryDatabase(null,
				ID_INDEX, dict, SecConfig);

		return ID_Index;
	}

	
	
	/*
	 * Created just on the ItalianWord object because its a field shared with
	 * ArabWord
	 */
	public static final SecondaryDatabase getCategory_IndexDatabase()
			throws DatabaseException {
		if (categoryIndex != null){
			System.out.println("not null Category index, returning the index");
			return categoryIndex;	
		}
		
		System.out.println("NULL Category index found, calling index Creator class");
		secX2ManyConfig.setMultiKeyCreator(new CategoryIndexCreator());
		categoryIndex = sleepyEnv.getEnvironment().openSecondaryDatabase(null,
				CATEGORY_INDEX, dict, secX2ManyConfig);

		return categoryIndex;
	}

	
	
	/*
	 * Created just on the ItalianWord object because its a field shared with
	 * ArabWord
	 */
	public static final SecondaryDatabase getSection_IndexDatabase()
			throws DatabaseException {
		if (sectionIndex != null)	{
			System.out.println("not null Section index, returning the index");
			return sectionIndex;
		}
		
		System.out.println("NULL Section index found, calling index Creator class");
		SectionIndexCreator sectionCreator = new SectionIndexCreator();
		secX2ManyConfig.setMultiKeyCreator(sectionCreator);
		sectionIndex = sleepyEnv.getEnvironment().openSecondaryDatabase(null,
				SECTION_INDEX, dict, secX2ManyConfig);
		
		return sectionIndex;
	}

	
	
	/**
	 * Close all databases and the environment.
	 */
	public void close() throws DatabaseException {

		dict.close();

	}
}
