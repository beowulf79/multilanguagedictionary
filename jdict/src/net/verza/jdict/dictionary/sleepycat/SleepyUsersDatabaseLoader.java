package net.verza.jdict.dictionary.sleepycat;

import org.apache.log4j.Logger;

/**
 * @author ChristianVerdelli
 * 
 */

public class SleepyUsersDatabaseLoader {

    private Logger log;

    public SleepyUsersDatabaseLoader() {
	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());
    }

}
