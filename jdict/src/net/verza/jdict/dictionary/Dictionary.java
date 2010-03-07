package net.verza.jdict.dictionary;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Vector;

import jxl.read.biff.BiffException;
import net.verza.jdict.dataloaders.LoaderOptionsStore;
import net.verza.jdict.dictionary.sleepycat.SleepyDatabase;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DatabaseImportException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.exceptions.LinkIDException;
import net.verza.jdict.model.SearchableObject;
import net.verza.jdict.model.UserProfile;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;

public interface Dictionary {

    /*
     * 
     */
    public abstract void setSearchKey(String indexName, DatabaseEntry db_entry,
	    String _lang) throws DatabaseException,
	    UnsupportedEncodingException, DataNotFoundException,
	    SecurityException, IllegalArgumentException,
	    ClassNotFoundException, NoSuchMethodException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException, FileNotFoundException;

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.Dictionary#read(java.lang.String)
     * 
     * Returns all the entries in a database
     */
    public abstract Vector<SearchableObject> read(String language)
	    throws DatabaseException, UnsupportedEncodingException,
	    DynamicCursorException, DataNotFoundException,
	    KeyNotFoundException, LinkIDException,
	    LanguagesConfigurationException, SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException,
	    FileNotFoundException;

    public abstract SearchableObject read(String _language, String _key)
	    throws DatabaseException, UnsupportedEncodingException,
	    DynamicCursorException, DataNotFoundException,
	    KeyNotFoundException, LinkIDException,
	    LanguagesConfigurationException, SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException,
	    FileNotFoundException;

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.dictionary.Dictionary#read(java.lang.String,
     *      java.lang.String, java.lang.String)
     * 
     * Lookup the given _key in the _srcLang database and returns all the
     * SearchableObject connected to this using the language _dstLang
     */
    public abstract Vector<SearchableObject> read(String _srcLang, String _key,
	    String _dstLang) throws DatabaseException,
	    UnsupportedEncodingException, DynamicCursorException,
	    DataNotFoundException, KeyNotFoundException, LinkIDException,
	    LanguagesConfigurationException, SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException,
	    FileNotFoundException;

    public abstract Vector<SearchableObject> getData()
	    throws DataNotFoundException;

    public abstract SearchableObject getData(int index)
	    throws DataNotFoundException;

    public abstract void write(SleepyDatabase db, SearchableObject object)
	    throws DatabaseException, UnsupportedEncodingException;

    public abstract void write(SleepyDatabase db, Integer key,
	    SearchableObject object) throws DatabaseException,
	    UnsupportedEncodingException;

    public abstract void write(SearchableObject w, SearchableObject d)
	    throws DatabaseException;

    public abstract Integer getFreeId() throws UnsupportedEncodingException,
	    DatabaseException;

    public abstract int getSize() throws DatabaseException;

    public abstract HashMap<String, Integer> loadDatabase(
	    LoaderOptionsStore optionsObj) throws LabelNotFoundException,
	    DatabaseException, IOException, BiffException,
	    KeyNotFoundException, DatabaseImportException,
	    LanguagesConfigurationException, SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException;

    @SuppressWarnings(value = "unchecked")
    public abstract void printDatabase();

    public abstract void flushDatabase() throws DatabaseException;

    // ////////////////////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////
    // CATEOGORY DATABASE

    public abstract String readCategoryDatabase(String key)
	    throws DatabaseException, UnsupportedEncodingException;

    public abstract int loadCategoryDatabase(LoaderOptionsStore optionsObj)
	    throws LabelNotFoundException, DatabaseException, BiffException,
	    IOException;

    public abstract String[] getCategoryKey();

    public abstract String[] getCategoryValue();

    public abstract int getCategoriesDatabaseSize();

    public abstract void flushCategoryDatabase() throws DatabaseException;

    public abstract void writeCategoryDatabase(String key, String data)
	    throws DatabaseException, UnsupportedEncodingException;

    // ////////////////////////////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////
    // SECTION DATABASE
    public abstract String readSectionDatabase(String key)
	    throws DatabaseException, UnsupportedEncodingException;

    public abstract int loadSectionDatabase(LoaderOptionsStore optionsObj)
	    throws LabelNotFoundException, DatabaseException, BiffException,
	    IOException;

    public abstract String[] getSectionKey();

    public abstract String[] getSectionValue();

    public abstract int getSectionDatabaseSize();

    public abstract void flushSectionDatabase() throws DatabaseException;

    public abstract void writeSectionDatabase(String key, String data)
	    throws DatabaseException, UnsupportedEncodingException;

    // ////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////
    // USERS DATABASE
    public abstract UserProfile readUserProfile(String key)
	    throws DatabaseException, UnsupportedEncodingException;

    public abstract void writeUserProfile(UserProfile up)
	    throws DatabaseException, UnsupportedEncodingException,
	    FileNotFoundException;

    public abstract void deleteUserProfile(UserProfile up)
	    throws DatabaseException, UnsupportedEncodingException,
	    FileNotFoundException;

    public abstract String[] getUserList() throws DatabaseException;

    /*
     * returns the username which has been loaded IMPORTATN. This method doesn't
     * query the db, just returns the user loaded at run time
     */
    public abstract UserProfile getUser();

    /*
     * Set the user IMPORTATN. This method doesn't write the user into the db,
     * just sets the user at run time
     */
    public abstract void setUser(String s) throws DatabaseException,
	    UnsupportedEncodingException;

}
