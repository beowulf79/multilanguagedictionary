package net.verza.jdict.sleepycat;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import com.sleepycat.je.DatabaseException;
import net.verza.jdict.Dictionary;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.sleepycat.SleepyConnector;
import org.apache.log4j.Logger;

public abstract class Factory {
	
	private static SleepyConnector singleton = null;
	private static Logger log;
	
	public static Dictionary getDictionary() throws DatabaseException,
	FileNotFoundException, UnsupportedEncodingException,
	DataNotFoundException, DynamicCursorException 	{
		
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called Factory object");
		if (singleton == null)	{
			singleton = new SleepyConnector();
			return singleton;
		}
		log.debug("returning dictionary instance " + singleton);
		return singleton;
	}

}

