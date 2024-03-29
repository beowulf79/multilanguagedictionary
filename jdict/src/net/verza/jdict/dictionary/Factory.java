package net.verza.jdict.dictionary;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import net.verza.jdict.dictionary.sleepycat.SleepyConnector;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

public abstract class Factory {

    private static SleepyConnector singleton = null;
    private static Logger log;

    public static Dictionary getDictionary() throws DatabaseException,
	    FileNotFoundException, UnsupportedEncodingException,
	    DataNotFoundException, DynamicCursorException,
	    KeyNotFoundException, IllegalArgumentException,
	    ClassNotFoundException, NoSuchMethodException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException {

	log = Logger.getLogger("dictionary");
	log.trace("called Factory object");
	if (singleton == null) {
	    singleton = new SleepyConnector();
	    return singleton;
	}
	log.debug("returning dictionary instance " + singleton);
	return singleton;
    }

}
