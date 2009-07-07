package net.verza.jdict.sleepycat.datastore;

import java.io.FileNotFoundException;
import net.verza.jdict.exceptions.LinkIDException;
import java.io.IOException;
import java.util.Vector;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import net.verza.jdict.dataloaders.LoaderOptionsStore;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.SearchableObject;
import net.verza.jdict.UserProfile;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.DatabaseImportException;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
import jxl.read.biff.BiffException;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguageFieldConfigurationClassDescritor;
import net.verza.jdict.properties.LanguagesConfiguration;

public final class SleepyConnector implements Dictionary {

	private static Logger log;

	// words database
	public SleepyDatabase db;
	private SleepyDatabaseLoader loader;
	private SleepyDatabaseReader reader;
	private SleepyDatabaseWriter writer;

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

		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName());

		new SleepyBinding();
		reader = new SleepyDatabaseReader();
		writer = new SleepyDatabaseWriter();
		loader = new SleepyDatabaseLoader();

		// Initialize User DB Handler
		users_database = new SleepyUsersDatabase();
		userReader = new SleepyUsersDatabaseReader(users_database);
		userWriter = new SleepyUsersDatabaseWriter(users_database
				.getUsersDatabase());

		// Initialize Category DB Handler
		category_database = new SleepyCategoryDatabase();
		categoryReader = new SleepyCategoryDatabaseReader(category_database);
		categoryLoader = new SleepyCategoryDatabaseLoader(category_database);

		// Initialize Section DB Handler
		section_database = new SleepySectionDatabase();
		sectionReader = new SleepySectionDatabaseReader(section_database);
		sectionLoader = new SleepySectionDatabaseLoader(section_database);

		// Initialize UserProfile class
		this.user = new UserProfile();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// WORDS DATABASE
	// ////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * 
	 */
	public void setSearchKey(String indexName, DatabaseEntry db_entry,
			String _lang) throws DatabaseException,
			UnsupportedEncodingException, DataNotFoundException {

		reader.setDatabase(SleepyFactory.getInstance().getDatabase(_lang));
		reader.setSecondaryCursor(indexName, db_entry);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.verza.jdict.dictionary.Dictionary#read(java.lang.String)
	 * 
	 * Returns all the entries in a database
	 */
	public Vector<SearchableObject> read(String language)
			throws DatabaseException, UnsupportedEncodingException,
			DynamicCursorException, DataNotFoundException, KeyNotFoundException {
		reader.setDatabase(SleepyFactory.getInstance().getDatabase(language));
		reader.setDataBinding(SleepyBinding.getDataBinding());
		reader.read();
		return reader.getData();
	}

	public SearchableObject read(String _language, String _key)
			throws DatabaseException, UnsupportedEncodingException,
			DynamicCursorException, DataNotFoundException, KeyNotFoundException {

		reader.setDatabase(SleepyFactory.getInstance().getDatabase(_language));

		// look in the language configuration for all the subindex and try
		// searching
		// with that subindex until the search returns any result
		LanguageConfigurationClassDescriptor languageConf = LanguagesConfiguration
				.getLanguageMainConfigNode(_language);
		List<LanguageFieldConfigurationClassDescritor> languageFieldsConf = languageConf
				.getFields();
		int count = 0;
		for (Iterator<LanguageFieldConfigurationClassDescritor> it = languageFieldsConf
				.iterator(); it.hasNext();) {
			LanguageFieldConfigurationClassDescritor tmp = (LanguageFieldConfigurationClassDescritor) it
					.next();
			System.out.println("trying with index " + tmp.getAttributeName());
			if ("subindex".equals(tmp.getKey_type())) {
				reader.setDataBinding(SleepyBinding.getDataBinding());
				reader.setSecondaryCursor(SleepyFactory.getInstance()
						.getDatabase(_language)
						.getIndex(tmp.getAttributeName()), new DatabaseEntry(
						_key.getBytes("UTF-8")));
				count = reader.read();
				System.out.println("entry found with index"
						+ tmp.getAttributeName() + " " + count);
				if (count > 0)
					break;

			} else if ("primary".equals(tmp.getKey_type())) {
				reader.setDataBinding(SleepyBinding.getDataBinding());
				reader.setKey(_key);
				count = reader.read();
				System.out.println("ENTRY FOUND " + count);
				if (count > 0)
					break;
			}
		}

		if (count > 0)
			return reader.getData(0);

		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.verza.jdict.dictionary.Dictionary#read(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 * 
	 * Lookup the given _key in the _srcLang database and returns all the
	 * SearchableObject connected to this using the language _dstLang
	 */
	public Vector<SearchableObject> read(String _srcLang, String _key,
			String _dstLang) throws DatabaseException,
			UnsupportedEncodingException, DynamicCursorException,
			DataNotFoundException, KeyNotFoundException, LinkIDException {

		SearchableObject obj = null;
		Vector<SearchableObject> linkedObjs = null;
		if ((obj = read(_srcLang, _key)) == null)
			return null;

		SleepyLinkidResolver linkidResolver = new SleepyLinkidResolver(
				this.reader);
		linkidResolver.setSearchableObject(obj);
		linkidResolver.addLanguage(_dstLang);
		linkidResolver.iterateLinkid();
		linkidResolver.toString();
		linkedObjs = linkidResolver.getLinkedObjects(obj, _dstLang);

		return linkedObjs;
	}

	public Vector<SearchableObject> getData() throws DataNotFoundException {

		return reader.getData();
	}

	public SearchableObject getData(int index) throws DataNotFoundException {

		return reader.getData(index);
	}

	public int writeData(SearchableObject w, SearchableObject d)
			throws DatabaseException {

		log.trace("Calling SleepyWriter-writeData with Data " + w.toString());
		return writer.write(w.getid(), d);
	}

	public int getSize() throws DatabaseException {
		return reader.getSize();
	}

	public HashMap<String, Integer> loadDatabase(LoaderOptionsStore optionsObj)
			throws LabelNotFoundException, DatabaseException, IOException,
			BiffException, KeyNotFoundException, DatabaseImportException {

		loader.setOptionObject(optionsObj);
		return loader.loadDatabases();
	}

	@SuppressWarnings(value = "unchecked")
	public void printDatabase() {

	}

	public long flushDatabase() throws DatabaseException {
		return this.db.flushDatabase();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// CATEGORY DATABASE
	// ////////////////////////////////////////////////////////////////////////////////////////

	public String readCategoryDatabase(String key) throws DatabaseException,
			UnsupportedEncodingException {

		return categoryReader.searchCategory(key);
	}

	public int loadCategoryDatabase(LoaderOptionsStore optionsObj)
			throws LabelNotFoundException, DatabaseException, BiffException,
			IOException {

		categoryLoader.setOptionObject(optionsObj);
		categoryReader.closeCursors();
		int count = categoryLoader.loadDatabases();
		categoryReader.openCursors();
		return count;
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

	public long flushCategoryDatabase() throws DatabaseException {
		return this.category_database.flushDatabase();
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

	public int loadSectionDatabase(LoaderOptionsStore optionsObj)
			throws LabelNotFoundException, DatabaseException, BiffException,
			IOException {

		sectionLoader.setOptionObject(optionsObj);
		sectionReader.closeCursors();
		int count = sectionLoader.loadDatabases();
		sectionReader.openCursors();

		return count;
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

	public long flushSectionDatabase() throws DatabaseException {
		return this.section_database.flushDatabase();
	}

	public void writeSectionDatabase(String key, String data)
			throws DatabaseException, UnsupportedEncodingException {

		sectionWriter.writeData(key, data);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////
	// USERS DATABASE
	public UserProfile readUserProfile(String key) throws DatabaseException,
			UnsupportedEncodingException {

		log.trace("called method readUsersDatabase with argument/s " + key);
		return userReader.readUser(key);
	}

	public void writeUserProfile(UserProfile up) throws DatabaseException,
			UnsupportedEncodingException {

		try {
			log.trace("called method writeUserDatabase with argument/s "
					+ up.toString());
			userWriter.writeUser(up);

		} catch (FileNotFoundException e) {

		}

	}

	public void deleteUserProfile(UserProfile up) throws DatabaseException,
			UnsupportedEncodingException {

		try {
			log.trace("called method writeUserDatabase with argument/s "
					+ up.toString());
			userWriter.deleteUser(up);

		} catch (FileNotFoundException e) {

		}

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
		this.user = readUserProfile(s);
	}

}
