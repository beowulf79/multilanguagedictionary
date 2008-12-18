package net.verza.jdict.sleepycat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.sleepycat.datastore.*;
import net.verza.jdict.UserProfile;
import net.verza.jdict.Word;
import net.verza.jdict.Dictionary;
import net.verza.jdict.IWord;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import jxl.read.biff.BiffException;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.collections.StoredIterator;
import com.sleepycat.je.SecondaryDatabase;

public final class SleepyConnector implements Dictionary  {

	
		
	private static Logger log;
	private SleepyEnvironment env;
	
	
	// words database
	private SleepyWordsDatabase db;
	private SleepyWordsDatabaseViews views;
	private SleepyWordsDatabaseLoader loader;
	private SleepyWordsDatabaseReader reader;
	private SleepyWordsDatabaseWriter writer;
	
	// users profiles database
	private SleepyUsersDatabase users_database;
	private SleepyUsersDatabaseReader userReader;
	private SleepyUsersDatabaseWriter userWriter;

	// category database
	private SleepyCategoryDatabase category_database;
	private SleepyCategoryDatabaseReader categoryReader;
	private SleepyCategoryDatabaseWriter categoryWriter;
	private SleepyCategoryDatabaseLoader categoryLoader;

	// section database
	private SleepySectionDatabase section_database;
	private SleepySectionDatabaseReader sectionReader;
	private SleepySectionDatabaseWriter sectionWriter;
	private SleepySectionDatabaseLoader sectionLoader;
	
	
	// lo username dovrebbe viene valorizzato tramite la gui userchoosegui
	// e dovrebbe essere memorizzazione in un property object non qui
	private UserProfile user;
	
	public SleepyConnector() throws DatabaseException, FileNotFoundException,
			DynamicCursorException, UnsupportedEncodingException {

		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class "+this.getClass().getName());
		env = SleepyEnvironment.getInstance();
		
		db = new SleepyWordsDatabase();
		views = new SleepyWordsDatabaseViews(db);
		reader = new SleepyWordsDatabaseReader(db);
		writer = new SleepyWordsDatabaseWriter(db);
		loader = new SleepyWordsDatabaseLoader(db);
		
		// Initialize User DB Handler
		users_database = new SleepyUsersDatabase();
		userReader = new SleepyUsersDatabaseReader(users_database);
		userWriter = new SleepyUsersDatabaseWriter(users_database.getUsersDatabase());

		// Initialize Category DB Handler
		category_database = new SleepyCategoryDatabase(env);
		categoryReader = new SleepyCategoryDatabaseReader(category_database);
		categoryLoader = new SleepyCategoryDatabaseLoader(category_database);

		// Initialize Section DB Handler
		section_database = new SleepySectionDatabase(env);
		sectionReader = new SleepySectionDatabaseReader(section_database);
		sectionLoader = new SleepySectionDatabaseLoader(section_database);
		
		//Initialize UserProfile class
		this.user = new UserProfile();
	}

	
	

	
	
	
	// ////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////
	// WORDS DATABASE
	
	public void setSearchKey(SecondaryDatabase secondary_db,
			DatabaseEntry db_entry) throws DatabaseException,
			UnsupportedEncodingException, DataNotFoundException {

		reader.setSecondaryCursor(secondary_db, db_entry);
	}

	public void read() throws DatabaseException,
			UnsupportedEncodingException, DynamicCursorException {

		reader.read();
	}

	public Vector<Word> getKey() throws DataNotFoundException {

		return reader.getKey();
	}

	public Word getKey(int index) throws DataNotFoundException {

		return reader.getKey(index);
	}

	public Vector<Word> getData() throws DataNotFoundException {

		return reader.getData();
	}

	public IWord getData(int index) throws DataNotFoundException {

		return reader.getData(index);
	}

	public int writeData(IWord w, IWord d) throws DatabaseException {

		log.trace("Calling SleepyWriter-writeData with Data "
				+ w.getsingular());
		return writer.write(w, d);
	}

	public SleepyWordsDatabase getSleepyDatabase() {
		return db;
	}

	public int getSize() throws DatabaseException {
		return reader.getSize();
	}

	public void loadDatabase(String path) throws LabelNotFoundException,
			DatabaseException, IOException, BiffException {

		loader.setFileName(path);
		loader.loadDatabases();
	}

	@SuppressWarnings(value = "unchecked")
	public void printDatabase() {
		Iterator<?> iterator;

		iterator = views.getDictEntrySet().iterator();
		try {
			while (iterator.hasNext()) {
				Map.Entry<Word,Word> entry = (Map.Entry<Word,Word>) iterator.next();
				Word key = entry.getKey();
				Word value = entry.getValue();
				log.trace(key.toString());
				log.trace(value.toString());
			}
		} finally {
			StoredIterator.close(iterator);
		}
	}
	
	
	
	
	
	// ////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////
	// CATEGORY DATABASE
	
	public String readCategoryDatabase(String key) throws DatabaseException,
			UnsupportedEncodingException {

		return categoryReader.searchCategory(key);
	}

	public void loadCategoryDatabase(String inputFilePath)
			throws LabelNotFoundException, DatabaseException, BiffException,
			IOException {

		categoryLoader.setFileName(inputFilePath);
		categoryLoader.loadDatabases();
	}

	public String[] getCategoryKey() {
		return categoryReader.getKey();
	}

	public String[] getCategoryValue() {
		return categoryReader.getData();
	}

	public int getCategoriesDatabaseSize() {
		return categoryReader.getCount();
	}

	public void writeCategoryDatabase(String key, String data)
			throws DatabaseException, UnsupportedEncodingException {

		categoryWriter.writeData(key, data);
	}

	
	
	// ////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////
	// SECTION DATABASE
	public String readSectionDatabase(String key) throws DatabaseException,
			UnsupportedEncodingException {

		return sectionReader.searchCategory(key);
	}

	public void loadSectionDatabase(String inputFilePath)
			throws LabelNotFoundException, DatabaseException, BiffException,
			IOException {

		sectionLoader.setFileName(inputFilePath);
		sectionLoader.loadDatabases();
	}

	public String[] getSectionKey() {
		return sectionReader.getKey();
	}

	public String[] getSectionValue() {
		return sectionReader.getData();
	}

	public int getSectionDatabaseSize() {
		return sectionReader.getCount();
	}

	public void writeSectionDatabase(String key, String data)
			throws DatabaseException, UnsupportedEncodingException {

		sectionWriter.writeData(key, data);
	}

	
	
	
	// ////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////
	// USERS DATABASE
	public UserProfile readUsersDatabase(String key) throws DatabaseException,
			UnsupportedEncodingException {

		log.trace("called method readUsersDatabase with argument/s " + key);
		return userReader.readUser(key);
	}

	public void writeUserDatabase(UserProfile up) throws DatabaseException,
			UnsupportedEncodingException {

		log.trace("called method writeUserDatabase with argument/s "
							+ up.toString());
		userWriter.writeUser(up);
	}

	public String[] getUserList() {
		log.trace("called method getUserList");
		return userReader.getUserList();
	}

	/*
	 * returns the username which has been loaded IMPORTATN. This method doesn't
	 * query the db, just returns the user loaded at run time
	 */
	public UserProfile getUser() {
		log.trace("called function getUser");
		log.debug("returning the user " + this.user);
		return this.user;
	}

	/*
	 * Set the user IMPORTATN. This method doesn't write the user into the db,
	 * just sets the user at run time
	 */
	public void setUser(String s) throws DatabaseException,
			UnsupportedEncodingException {
		log.trace("called methid setUser with argument/s " + s);
		log.debug("setting session user " + s);
		this.user = readUsersDatabase(s);
	}

	
	

}
