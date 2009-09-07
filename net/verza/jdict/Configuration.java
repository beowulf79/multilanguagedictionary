package net.verza.jdict;

/**
 * 
 */

/**
 * @author ChristianVerdelli
 * 
 */
public class Configuration {

	public static final int EXIT_SUCCESS = 0;
	public static final int EXIT_FAILURE = 1;

	/*
	 * Log4J
	 */
	public static final String LOG4JCONF = "conf/log4j.properties";

	/*
	 * Quiz type
	 */
	public static final String AUDIO2ITALIAN = "Audio->Italian";
	public static final String AUDIO2ARABIC = "Audio->Arabic";
	public static final String AUDIO2ENGLISH = "Audio->English";
	public static final String AUDIO2EGYPTIAN = "Audio->Egyptian";
	public static final String ITALIAN2ARABIC = "Italian->Arabic";
	public static final String ITALIAN2EGYPTIAN = "Italian->Egyptian";
	public static final String ITALIAN2ENGLISH = "Italian->English";
	public static final String ARABIC2ITALIAN = "Arabic->Italian";
	public static final String ARABIC2ENGLISH = "Arabic->English";
	public static final String ARABIC2EGYPTIAN = "Arabic->Egyptian";
	public static final String ENGLISH2ARABIC = "English->Arabic";
	public static final String ENGLISH2EGYPTIAN = "English->Egyptian";
	public static final String ENGLISH2ITALIAN = "English->Italian";
	public static final String EGYPTIAN2ITALIAN = "Egyptian->Italian";
	public static final String EGYPTIAN2ENGLISH = "Egyptian->English";
	public static final String EGYPTIAN2ARABIC = "Egyptian->Arabic";
	
	public static final int QUIZMAXLOOPS = 10;
	/*
	 * Quiz answers code
	 */
	
	public static final String CORRECTANSWER = "1";
	public static final String WRONGANSWER = "-1";

	/*
	 * Quiz Statistics
	 */
	public static final int SHOWMOSTFAILEDWORDS = 10;
	public static final int SHOWMOSTGUESSEDWORDS = 10;

	/*
	 * Stored in the User Profile In a future release the GUI will allow Admin
	 * User to Edit the database
	 */
	public static final int USER_ROLE = 1;
	public static final int ADMIN_ROLE = 0;

	public static final String ENVIROMENT_HOME = "../data/";

	/*
	 * Used by the new version of SleepyLoader
	 */
	public static final String ITALIAN_AUDIO_DIR = "../audio/italian/";
	public static final String EGYPTIAN_AUDIO_DIR = "../audio/egyptian/";
	public static final String ARABIC_AUDIO_DIR = "../audio/arabic/";
	public static final String MULTIVALUE_SEPARATOR = "-";
	public static final int LINKED_MAX_REFERRER = 10;
	public static final String CATEGORY_TABLE_SHEET_NAME = "Categories";
	public static final String CATEGORY_TABLE_LIST_NAME = "CATEGORY_LIST";
	public static final String CATEGORY_TABLE_LIST_INDEX = "ID";
	public static final String SECTION_TABLE_SHEET_NAME = "Sections";
	public static final String SECTION_TABLE_LIST_NAME = "SECTION_LIST";
	public static final String SECTION_TABLE_LIST_INDEX = "ID";
	
	
	public static final int UPDATE = 0;
	public static final int REBUILD = 1;
	
	public static final int MAX_USERS_PROFILES = 1000;

	
}
