package net.verza.jdict.dictionary.sleepycat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import jxl.read.biff.BiffException;
import net.verza.jdict.dataloaders.LoaderOptionsStore;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DatabaseImportException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.exceptions.LinkIDException;
import net.verza.jdict.model.SearchableObject;
import net.verza.jdict.model.UserProfile;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguageFieldConfigurationClassDescritor;
import net.verza.jdict.properties.LanguagesConfiguration;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;

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
	    DynamicCursorException, UnsupportedEncodingException,
	    KeyNotFoundException, IllegalArgumentException,
	    ClassNotFoundException, NoSuchMethodException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException {

	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());

	// new SleepyBinding();
	reader = new SleepyDatabaseReader();
	writer = new SleepyDatabaseWriter();
	// writer.setDataBinding(SleepyBinding.getDataBinding());
	loader = new SleepyDatabaseLoader();

	// Initialize User DB Handler
	users_database = new SleepyUsersDatabase();
	userReader = new SleepyUsersDatabaseReader(users_database);
	userWriter = new SleepyUsersDatabaseWriter(users_database);

	// Initialize Category DB Handler
	category_database = new SleepyCategoryDatabase();
	categoryReader = new SleepyCategoryDatabaseReader(category_database);
	categoryLoader = new SleepyCategoryDatabaseLoader(category_database);
	categoryWriter = new SleepyCategoryDatabaseWriter(category_database);

	// Initialize Section DB Handler
	section_database = new SleepySectionDatabase();
	sectionReader = new SleepySectionDatabaseReader(section_database);
	sectionLoader = new SleepySectionDatabaseLoader(section_database);
	sectionWriter = new SleepySectionDatabaseWriter(section_database);

	// Initialize UserProfile class
	this.user = new UserProfile();
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    // WORDS DATABASE
    // ////////////////////////////////////////////////////////////////////////////////////////

    /*
     * 
     */
    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#setSearchKey(java.lang.String,
     *      com.sleepycat.je.DatabaseEntry, java.lang.String)
     */
    public void setSearchKey(String indexName, DatabaseEntry db_entry,
	    String _lang) throws DatabaseException,
	    UnsupportedEncodingException, DataNotFoundException,
	    SecurityException, IllegalArgumentException,
	    ClassNotFoundException, NoSuchMethodException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException, FileNotFoundException {

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
    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#read(java.lang.String)
     */
    public Vector<SearchableObject> read(String language)
	    throws DatabaseException, UnsupportedEncodingException,
	    DynamicCursorException, DataNotFoundException,
	    KeyNotFoundException, LinkIDException,
	    LanguagesConfigurationException, SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException,
	    FileNotFoundException {
	reader.setDatabase(SleepyFactory.getInstance().getDatabase(language));
	// reader.setDataBinding(SleepyBinding.getDataBinding());
	int nentries = reader.read();
	log.debug("lookup found entries " + nentries);
	return reader.getData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#read(java.lang.String,
     *      java.lang.String)
     */
    public SearchableObject read(String _language, String _key)
	    throws DatabaseException, UnsupportedEncodingException,
	    DynamicCursorException, DataNotFoundException,
	    KeyNotFoundException, LinkIDException,
	    LanguagesConfigurationException, SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException,
	    FileNotFoundException {

	reader.setDatabase(SleepyFactory.getInstance().getDatabase(_language));

	// look in the language configuration for all the subindex and try
	// searching
	// with that subindex until the search returns any result
	LanguageConfigurationClassDescriptor languageConf = LanguagesConfiguration
		.getLanguageMainConfigNode(_language);
	List<LanguageFieldConfigurationClassDescritor> languageFieldsConf = languageConf
		.getFields();
	int nentries = 0;
	for (Iterator<LanguageFieldConfigurationClassDescritor> it = languageFieldsConf
		.iterator(); it.hasNext();) {
	    LanguageFieldConfigurationClassDescritor tmp = (LanguageFieldConfigurationClassDescritor) it
		    .next();
	    log.debug("looking up using index " + tmp.getAttributeName());
	    if ("subindex".equals(tmp.getKey_type())) {
		// reader.setDataBinding(SleepyBinding.getDataBinding());
		reader.setSecondaryCursor(SleepyFactory.getInstance()
			.getDatabase(_language)
			.getIndex(tmp.getAttributeName()), new DatabaseEntry(
			_key.getBytes("UTF-8")));
		nentries = reader.read();
		if (nentries > 0)
		    break;

	    } else if ("primary".equals(tmp.getKey_type())) {
		// reader.setDataBinding(SleepyBinding.getDataBinding());
		reader.setKey(_key);
		nentries = reader.read();

		if (nentries > 0)
		    break;
	    }

	}
	log.info("lookup using key: " + _key + " found entries" + nentries);
	if (nentries > 0)
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
    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#read(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public Vector<SearchableObject> read(String _srcLang, String _key,
	    String _dstLang) throws DatabaseException,
	    UnsupportedEncodingException, DynamicCursorException,
	    DataNotFoundException, KeyNotFoundException, LinkIDException,
	    LanguagesConfigurationException, SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException,
	    FileNotFoundException {

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

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#getData()
     */
    public Vector<SearchableObject> getData() throws DataNotFoundException {

	return reader.getData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#getData(int)
     */
    public SearchableObject getData(int index) throws DataNotFoundException {

	return reader.getData(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#write(net.verza.jdict.dictionary.sleepycat.SleepyDatabase,
     *      net.verza.jdict.model.SearchableObject)
     */
    public void write(SleepyDatabase db, SearchableObject object)
	    throws DatabaseException, UnsupportedEncodingException {
	log.trace("called function writeData");
	writer.write(db, object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#write(net.verza.jdict.dictionary.sleepycat.SleepyDatabase,
     *      java.lang.Integer, net.verza.jdict.model.SearchableObject)
     */
    public void write(SleepyDatabase db, Integer key, SearchableObject object)
	    throws DatabaseException, UnsupportedEncodingException {
	log.trace("called function writeData");
	writer.write(db, key, object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#write(net.verza.jdict.model.SearchableObject,
     *      net.verza.jdict.model.SearchableObject)
     */
    public void write(SearchableObject w, SearchableObject d)
	    throws DatabaseException {

	log.trace("Calling SleepyWriter-writeData with Data " + w.toString());
	writer.write(w.getid(), d);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#getFreeId()
     */
    public Integer getFreeId() throws UnsupportedEncodingException,
	    DatabaseException {
	log.trace("called function getFreeId");
	return db.getFreeId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#getSize()
     */
    public int getSize() throws DatabaseException {
	return reader.getSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#loadDatabase(net.verza.jdict.dataloaders.LoaderOptionsStore)
     */
    public HashMap<String, Integer> loadDatabase(LoaderOptionsStore optionsObj)
	    throws LabelNotFoundException, DatabaseException, IOException,
	    BiffException, KeyNotFoundException, DatabaseImportException,
	    LanguagesConfigurationException, SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException {

	loader.setOptionObject(optionsObj);
	return loader.loadDatabases();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#printDatabase()
     */
    @SuppressWarnings(value = "unchecked")
    public void printDatabase() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#flushDatabase()
     */
    public void flushDatabase() throws DatabaseException {
	this.db.flushDatabase();
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    // CATEGORY DATABASE
    // ////////////////////////////////////////////////////////////////////////////////////////

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#readCategoryDatabase(java.lang.String)
     */
    public String readCategoryDatabase(String key) throws DatabaseException,
	    UnsupportedEncodingException {

	return categoryReader.searchCategory(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#loadCategoryDatabase(net.verza.jdict.dataloaders.LoaderOptionsStore)
     */
    public int loadCategoryDatabase(LoaderOptionsStore optionsObj)
	    throws LabelNotFoundException, DatabaseException, BiffException,
	    IOException {

	categoryLoader.setOptionObject(optionsObj);
	categoryReader.closeCursors();
	int count = categoryLoader.loadDatabases();
	categoryReader.openCursors();
	categoryReader.initialize();

	return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#getCategoryKey()
     */
    public String[] getCategoryKey() {
	return categoryReader.getKey();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#getCategoryValue()
     */
    public String[] getCategoryValue() {
	return categoryReader.getData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#getCategoriesDatabaseSize()
     */
    public int getCategoriesDatabaseSize() {

	return categoryReader.getCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#flushCategoryDatabase()
     */
    public void flushCategoryDatabase() throws DatabaseException {
	this.category_database.flushDatabase();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#writeCategoryDatabase(java.lang.String,
     *      java.lang.String)
     */
    public void writeCategoryDatabase(String key, String data)
	    throws DatabaseException, UnsupportedEncodingException {

	categoryWriter.writeData(key, data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#writeCategoryDatabase(java.lang.String,
     *      java.lang.String)
     */
    public void writeCategoryDatabase(String key) throws DatabaseException,
	    UnsupportedEncodingException {

	categoryWriter.writeData(key);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////
    // SECTION DATABASE
    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#readSectionDatabase(java.lang.String)
     */
    public String readSectionDatabase(String key) throws DatabaseException,
	    UnsupportedEncodingException {
	return sectionReader.searchCategory(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#loadSectionDatabase(net.verza.jdict.dataloaders.LoaderOptionsStore)
     */
    public int loadSectionDatabase(LoaderOptionsStore optionsObj)
	    throws LabelNotFoundException, DatabaseException, BiffException,
	    IOException {

	sectionLoader.setOptionObject(optionsObj);
	sectionReader.closeCursors();
	int count = sectionLoader.loadDatabases();
	sectionReader.openCursors();
	sectionReader.initialize();

	return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#getSectionKey()
     */
    public String[] getSectionKey() {
	return sectionReader.getKey();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#getSectionValue()
     */
    public String[] getSectionValue() {
	return sectionReader.getData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#getSectionDatabaseSize()
     */
    public int getSectionDatabaseSize() {
	return sectionReader.getCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#flushSectionDatabase()
     */
    public void flushSectionDatabase() throws DatabaseException {
	this.section_database.flushDatabase();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#writeSectionDatabase(java.lang.String,
     *      java.lang.String)
     */
    public void writeSectionDatabase(String key, String data)
	    throws DatabaseException, UnsupportedEncodingException {

	sectionWriter.writeData(key, data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#writeSectionDatabase(java.lang.String,
     *      java.lang.String)
     */
    public void writeSectionDatabase(String key) throws DatabaseException,
	    UnsupportedEncodingException {

	sectionWriter.writeData(key);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////
    // USERS DATABASE
    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#readUserProfile(java.lang.String)
     */
    public UserProfile readUserProfile(String key) throws DatabaseException,
	    UnsupportedEncodingException {

	log.trace("called method readUsersDatabase with argument/s " + key);
	return userReader.readUser(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#writeUserProfile(net.verza.jdict.model.UserProfile)
     */
    public void writeUserProfile(UserProfile up) throws DatabaseException,
	    UnsupportedEncodingException, FileNotFoundException {

	log.trace("called method writeUserDatabase with argument/s "
		+ up.toString());
	userWriter.writeUser(up);

    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#deleteUserProfile(net.verza.jdict.model.UserProfile)
     */
    public void deleteUserProfile(UserProfile up) throws DatabaseException,
	    UnsupportedEncodingException, FileNotFoundException {

	log.trace("called method writeUserDatabase with argument/s "
		+ up.toString());
	userWriter.deleteUser(up);

    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#getUserList()
     */
    public String[] getUserList() throws DatabaseException {
	log.trace("called method getUserList");
	return userReader.getUserList();
    }

    /*
     * returns the username which has been loaded IMPORTATN. This method doesn't
     * query the db, just returns the user loaded at run time
     */
    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#getUser()
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
    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.sleepycat.AAA#setUser(java.lang.String)
     */
    public void setUser(String s) throws DatabaseException,
	    UnsupportedEncodingException {
	log.trace("called methid setUser with argument/s " + s);
	log.debug("setting session user " + s);
	this.user = readUserProfile(s);
    }

}
