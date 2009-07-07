package net.verza.jdict.dictionary;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import java.util.HashMap;
import jxl.read.biff.BiffException;
import net.verza.jdict.SearchableObject;
import net.verza.jdict.UserProfile;
import net.verza.jdict.dataloaders.LoaderOptionsStore;
import net.verza.jdict.exceptions.LinkIDException;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.exceptions.DatabaseImportException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;

public interface Dictionary {

	public abstract void setSearchKey(String indexName, DatabaseEntry db_entry,
			String lang) throws DatabaseException,
			UnsupportedEncodingException, DataNotFoundException;

	public abstract Vector<SearchableObject> read(String _language)
			throws DatabaseException, UnsupportedEncodingException,
			DynamicCursorException, DataNotFoundException, KeyNotFoundException;

	public abstract SearchableObject read(String _language, String _key)
			throws DatabaseException, UnsupportedEncodingException,
			DynamicCursorException, DataNotFoundException, KeyNotFoundException;

	public abstract Vector<SearchableObject> read(String _srcLang, String _key,
			String _dstLang) throws DatabaseException,
			UnsupportedEncodingException, DynamicCursorException,
			DataNotFoundException, KeyNotFoundException, LinkIDException;

	public abstract Vector<SearchableObject> getData()
			throws DataNotFoundException;

	public abstract SearchableObject getData(int index)
			throws DataNotFoundException;

	public abstract int writeData(SearchableObject w, SearchableObject d)
			throws DatabaseException;

	public abstract int getSize() throws DatabaseException;

	public abstract HashMap<String, Integer> loadDatabase(
			LoaderOptionsStore optionsObj) throws LabelNotFoundException,
			DatabaseException, IOException, BiffException,
			KeyNotFoundException, DatabaseImportException;

	public abstract void printDatabase();

	public abstract long flushDatabase() throws DatabaseException;

	// ////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////
	// CATEGORY DATABASE
	public abstract String readCategoryDatabase(String key)
			throws DatabaseException, UnsupportedEncodingException;

	public abstract int loadCategoryDatabase(LoaderOptionsStore optionsObj)
			throws LabelNotFoundException, DatabaseException, BiffException,
			IOException;

	public abstract String[] getCategoryKey();

	public abstract String[] getCategoryValue();

	public abstract int getCategoriesDatabaseSize();

	public abstract long flushCategoryDatabase() throws DatabaseException;

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

	public abstract long flushSectionDatabase() throws DatabaseException;

	public abstract void writeSectionDatabase(String key, String data)
			throws DatabaseException, UnsupportedEncodingException;

	// ////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////
	// USERS DATABASE
	public abstract UserProfile readUserProfile(String key)
			throws DatabaseException, UnsupportedEncodingException;

	public abstract void writeUserProfile(UserProfile up)
			throws DatabaseException, UnsupportedEncodingException;

	public abstract void deleteUserProfile(UserProfile up)
			throws DatabaseException, UnsupportedEncodingException;

	public abstract String[] getUserList();

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