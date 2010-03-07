package net.verza.jdict.dictionary.sleepycat;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import net.verza.jdict.model.SearchableObject;
import net.verza.jdict.model.Verb;
import net.verza.jdict.utils.Utility;

import org.apache.log4j.Logger;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class SleepyDatabasePrimaryCursor {

    private CursorConfig config;
    private Cursor cursor;
    private int dbsize; // store the entries number in the db
    private Logger log;
    private Vector<SearchableObject> wdata = new Vector<SearchableObject>();
    private EntryBinding dataBinding;
    private String key;

    public SleepyDatabasePrimaryCursor(SleepyDatabase db)
	    throws DatabaseException {

	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());
	config = new CursorConfig();
	config.setReadUncommitted(true);
	cursor = db.getDatabase().openCursor(null, null);
	dbsize = 0;
	key = new String();

    }

    public Vector<SearchableObject> getData() {
	return this.wdata;
    }

    /**
     * Set the key to look up for
     * 
     * @param _key
     * @return
     * @throws UnsupportedEncodingException
     */
    public int setKey(String _key) throws UnsupportedEncodingException {
	log.debug("setting Key " + _key);
	this.key = _key;
	return 0;

    }

    public Cursor getCursor() {
	return cursor;
    }

    public int getDatabaseSize() throws DatabaseException {
	countDatabaseEntry();
	log.info("database size is: " + this.dbsize);
	return this.dbsize;
    }

    /**
     * Count the number of the entries inside the database
     */
    private void countDatabaseEntry() throws DatabaseException {

	DatabaseEntry keyEntry = new DatabaseEntry();
	DatabaseEntry dataEntry = new DatabaseEntry();

	while (cursor.getNext(keyEntry, dataEntry, LockMode.READ_UNCOMMITTED) == OperationStatus.SUCCESS) {
	    dbsize++;
	}

    }

    /**
     * Effettua una ricerca sul database primario. Se la chiave di ricerca è
     * stata impostata allora viene cercata solamente quella entry altrimenti
     * viene ritornato l'interno database
     * 
     * @throws DatabaseException
     */
    public void read() throws DatabaseException {

	log.debug("called method read with key " + this.key
		+ " and database name "
		+ cursor.getDatabase().getDatabaseName());

	// let's clear the array returned by any previous search
	DatabaseEntry foundKey = new DatabaseEntry();
	DatabaseEntry foundData = new DatabaseEntry();

	SearchableObject data = new Verb();
	OperationStatus op;

	// se la chiave primaria è stata impostata cerchiamo con il cursore
	// cursor.getSearchKey;
	// non bisogna iterare in quanto è la chiave primaria e non può
	// avere duplicati.
	if (!"".equals(this.key)) {

	    log.debug("look-up using primary key");
	    try {
		foundKey = new DatabaseEntry(Utility
			.intToByteArray(new Integer(key)));
		op = cursor.getSearchKey(foundKey, foundData, LockMode.DEFAULT);
		if (op == OperationStatus.NOTFOUND) {
		    log.debug("primary " + key + " not found");
		} else {
		    data = (SearchableObject) dataBinding
			    .entryToObject(foundData);
		    log.info("found data " + data.toString());
		    wdata.addElement(data);
		}
	    } catch (NumberFormatException e) {
		log.debug("primary key is not in a valid format");
		return;
	    }

	    // la chiave primaria non è stata impostata; viene effettuata
	    // una scansione dell'interno db.
	} else {

	    log.debug("look-up the entire database");
	    cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);
	    data = (SearchableObject) dataBinding.entryToObject(foundData);
	    log.debug("adding to key with ID " + key + " and data with ID "
		    + data.getid());
	    wdata.addElement(data);

	    while ((cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS)) {
		data = (SearchableObject) dataBinding.entryToObject(foundData);
		wdata.addElement(data);
		log.info("found data " + data.toString());
	    }
	}

    }

    public void setDataBinding(EntryBinding eb) {
	log.trace("setting key binding with object " + eb.hashCode());
	this.dataBinding = eb;
    }

}
