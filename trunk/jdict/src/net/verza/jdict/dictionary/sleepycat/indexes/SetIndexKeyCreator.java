package net.verza.jdict.dictionary.sleepycat.indexes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

import net.verza.jdict.model.SearchableObject;

import org.apache.log4j.Logger;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;

/**
 * @author ChristianVerdelli
 * 
 */
public class SetIndexKeyCreator extends MultiValueIndexKeyCreator {

    // SleepyDatabase db;
    private static Logger log;
    private Class<?> Class2Load;
    private Constructor<?> constr;
    private SearchableObject obj;
    private String indexName, methodName;
    private EntryBinding binding;

    public SetIndexKeyCreator(String languageName, String className,
	    String index_name) throws ClassNotFoundException,
	    IllegalArgumentException, InstantiationException,
	    IllegalAccessException, InvocationTargetException,
	    SecurityException, NoSuchMethodException {

	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());

	// this.languageName = languageName;
	this.indexName = index_name;
	// the setter method must always be the attribute name prefixed with the
	// string set
	this.methodName = "get" + index_name;

	Class2Load = Class.forName(className);
	Class<?>[] c_arr = new Class[] {};
	constr = Class2Load.getConstructor(c_arr);
	obj = (SearchableObject) constr.newInstance();
	log.debug("instantied class " + obj.getClass().getName());

    }

    public void setBinding(EntryBinding dbinding) {
	this.binding = dbinding;
    }

    @SuppressWarnings(value = "unchecked")
    public void createSecondaryKeys(SecondaryDatabase secondary,
	    DatabaseEntry key, DatabaseEntry data, Set result) {
	try {

	    log.trace("called createSecondaryKey with data " + data.toString());
	    obj = (SearchableObject) binding.entryToObject(data);

	    Class<?>[] methodTypes = new Class[] {};
	    Method Method2Call;

	    Method2Call = Class2Load.getMethod(this.methodName, methodTypes);
	    Set<String> value = (Set<String>) Method2Call.invoke(obj,
		    (Object[]) null);

	    if (value.isEmpty()) {
		log.warn("empty set, index  " + indexName
			+ " for the entry will not be set");
	    } else {

		log.debug("setting " + indexName
			+ " index for the entry with value " + value
			+ " using the object " + obj.getClass().getName()
			+ " using method " + this.methodName);

		this.copyKeysToEntries((Set) value, result);

	    }

	} catch (NoSuchMethodException e) {
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}
	// catch (KeyNotFoundException e) {
	// e.printStackTrace();
	// }

    }

    /**
     * A utility method to copy a set of keys (Strings) into a set of
     * DatabaseEntry objects.
     */
    private void copyKeysToEntries(Set<String> keys, Set<DatabaseEntry> entries) {
	StringBinding keyBinding = new StringBinding();
	log.info("setting multi value " + indexName + " index for the entry");
	for (Iterator<String> i = keys.iterator(); i.hasNext();) {
	    String s = (String) i.next();
	    log.debug("index value " + s);
	    DatabaseEntry entry = new DatabaseEntry();
	    keyBinding.objectToEntry(s, entry);
	    entries.add(entry);
	}
    }

}
