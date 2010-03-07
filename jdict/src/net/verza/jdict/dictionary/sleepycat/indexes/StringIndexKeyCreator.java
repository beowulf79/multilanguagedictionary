package net.verza.jdict.dictionary.sleepycat.indexes;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.verza.jdict.dictionary.sleepycat.SleepyDatabase;
import net.verza.jdict.model.SearchableObject;

import org.apache.log4j.Logger;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;

/**
 * @author ChristianVerdelli
 * 
 */
public class StringIndexKeyCreator extends IndexKeyCreator {

    SleepyDatabase db;
    private static Logger log;
    private Class<?> Class2Load;
    private Constructor<?> constr;
    private SearchableObject obj;
    private String indexName, methodName;
    private EntryBinding binding;

    public StringIndexKeyCreator(String _languageName, String _className,
	    String _index_name) throws ClassNotFoundException,
	    SecurityException, NoSuchMethodException, IllegalArgumentException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException {

	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName()
		+ " with arguments: language name " + _languageName
		+ "class name " + _className + "index name " + _index_name);

	// this.languageName = _languageName;
	this.indexName = _index_name;
	// the setter method must always be the attribute name prefixed with the
	// string set
	this.methodName = "get" + _index_name;

	Class2Load = Class.forName(_className);
	Class<?>[] c_arr = new Class[] {};
	constr = Class2Load.getConstructor(c_arr);
	obj = (SearchableObject) constr.newInstance();
	log.debug("instantied class " + obj.getClass().getName());

    }

    public void setBinding(EntryBinding dbinding) {
	log.trace("called method setBinding with data " + dbinding);
	this.binding = dbinding;
    }

    public boolean createSecondaryKey(SecondaryDatabase secondary,
	    DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {
	try {

	    log.trace("called method createSecondaryKey with data "
		    + data.toString());
	    obj = (SearchableObject) binding.entryToObject(data);

	    Class<?>[] methodTypes = new Class[] {};
	    Method Method2Call;

	    Method2Call = Class2Load.getMethod(this.methodName, methodTypes);
	    Object value = (Object) Method2Call.invoke(obj, (Object[]) null);

	    if (value == null) {
		log.warn("can't not set up index" + indexName + " the entry");

	    } else {

		String tmp = (String) value;
		result.setData(tmp.getBytes("UTF8"));

		log.debug("setting " + indexName
			+ " index for the entry with value " + tmp
			+ " using method " + this.methodName);

	    }

	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (NoSuchMethodException e) {
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	}

	// catch (KeyNotFoundException e) {
	// }

	return true;
    }
}
