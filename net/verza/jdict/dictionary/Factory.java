package net.verza.jdict.dictionary;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import com.sleepycat.je.DatabaseException;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.sleepycat.datastore.SleepyConnector;

import org.apache.log4j.Logger;

public abstract class Factory {
	
	private static SleepyConnector singleton = null;
	private static Logger log;
	
	public static Dictionary getDictionary() throws DatabaseException,
	FileNotFoundException, UnsupportedEncodingException,
	DataNotFoundException, DynamicCursorException 	{
		
		log = Logger.getLogger("net.verza.jdict.dictionary");
		log.trace("called Factory object");
		if (singleton == null)	{
			singleton = new SleepyConnector();
			return singleton;
		}
		log.debug("returning dictionary instance " + singleton);
		return singleton;
	}

}

