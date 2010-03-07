package net.verza.jdict.quiz;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * @author ChristianVerdelli
 * 
 * Questa classe riceve in ingresso degli oggetti di tipo QuizResult e ne
 * elabora le statistiche
 * 
 */

public class QuizStats {
    private static Logger log;
    private Vector<QuizResult> data;
    private HashMap<QuizResult, Integer> resultsMap;

    public QuizStats(Vector<QuizResult> quizResultVector) {
	log = Logger.getLogger("jdict");
	log.debug("initializing class " + this.getClass().getName());
	data = quizResultVector;
	log.debug("found " + quizResultVector.size() + " questions to analize");
	resultsMap = new HashMap<QuizResult, Integer>();
    }

    public HashMap<QuizResult, Integer> getResultStatsMap() {
	return resultsMap;
    }

    public void computeStats() {

	// iterates through the array of stat result
	log.debug("looping through array of stats to compute user statistics");
	Iterator<QuizResult> itr = data.iterator();
	QuizResult quizResultEntry;
	while (itr.hasNext()) {
	    quizResultEntry = itr.next();
	    // containsKey calls the method equals of the object
	    if (resultsMap.containsKey(quizResultEntry)) {
		log.debug("resultsMap.containsKey() is: true");
		int counter = (resultsMap.get(quizResultEntry) + 1);
		resultsMap.put(quizResultEntry, counter);

	    } else {
		log.debug("resultsMap.containsKey() is: false");
		resultsMap.put(quizResultEntry, 1);
	    }
	}

    }

}
