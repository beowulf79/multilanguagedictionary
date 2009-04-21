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
	public static final String LOG4JCONF = "/Users/ChristianVerdelli/documents/workspace/JDICT/conf/log4j.properties";

	/*
	 * Quiz type
	 */
	public static final String AUDIO2ITALIAN = "0";
	public static final String AUDIO2ARABIC = "1";
	public static final String AUDIO2ENGLISH = "10";
	public static final String AUDIO2EGYPTIAN = "11";
	
	
	public static final String ITALIAN2ARABIC = "2";
	public static final String ITALIAN2EGYPTIAN = "3";
	public static final String ITALIAN2ENGLISH = "4";
	
	public static final String ARABIC2ITALIAN = "5";
	public static final String ARABIC2ENGLISH = "6";
	public static final String ARABIC2EGYPTIAN = "15";
	
	public static final String ENGLISH2ARABIC = "7";
	public static final String ENGLISH2EGYPTIAN = "8";
	public static final String ENGLISH2ITALIAN = "9";
	
	public static final String EGYPTIAN2ITALIAN = "12";
	public static final String EGYPTIAN2ENGLISH = "13";
	public static final String EGYPTIAN2ARABIC = "14";
	
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
	public static final int USER_ROLE = 0;
	public static final int ADMIN_ROLE = 1;

	public static final String ENVIROMENT_HOME = "/Users/ChristianVerdelli/documents/workspace/jdict/data/";

	/*
	 * Used by the new version of SleepyLoader
	 */
	public static final String ITALIAN_AUDIO_DIR = "/Users/ChristianVerdelli/documents/workspace/jdict/audio/italian/";
	public static final String EGYPTIAN_AUDIO_DIR = "/Users/ChristianVerdelli/documents/workspace/jdict/audio/egyptian/";
	public static final String ARABIC_AUDIO_DIR = "/Users/ChristianVerdelli/documents/workspace/jdict/audio/arabic/";
	public static final String MULTIVALUE_SEPARATOR = "-";
	public static final int LINKED_MAX_REFERRER = 10;
	public static final String CATEGORY_TABLE_SHEET_NAME = "Categories";
	public static final String CATEGORY_TABLE_LIST_NAME = "CATEGORY_LIST";
	public static final String CATEGORY_TABLE_LIST_INDEX = "ID";
	public static final String SECTION_TABLE_SHEET_NAME = "Sections";
	public static final String SECTION_TABLE_LIST_NAME = "SECTION_LIST";
	public static final String SECTION_TABLE_LIST_INDEX = "ID";
	
	public static final int MAX_USERS_PROFILES = 1000;

}
