package net.verza.jdict.dictionary.sleepycat;

/**
 *  
 * Effettua la scansione del file di properties e memorizza in una struttura
 * di tipo HashMap le chiavi <language><nickname> ed i valori <language><class>
 *
 * @author ChristianVerdelli
 *
 */

import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.properties.PropertiesLoader;

import org.apache.log4j.Logger;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;

public class SleepyBinding {

    private static Logger log;
    private static EntryBinding eb;

    public SleepyBinding() {
	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());
	try {
	    String serializedClass = PropertiesLoader
		    .getProperty("dataBindingClass");
	    log.info("serializing class " + serializedClass
		    + " used for dataBinding ");
	    eb = new SerialBinding(getClassCatalog(), Class
		    .forName(serializedClass));

	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}

    }

    /**
     * restituisce l'oggetto EntryBinding presente nell'HashMap associato alla
     * chiave
     * 
     * @return l'oggetto EntryBinding della class /language/class associato alla
     *         key
     */
    static public EntryBinding getDataBinding() throws KeyNotFoundException {

	log.trace("called method getDataBinding");
	return eb;

    }

    /**
     * 
     * @return ritorna il database ClassCatalog
     */
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
