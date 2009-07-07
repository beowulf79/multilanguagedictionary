package net.verza.jdict.quiz;

import org.apache.log4j.Logger;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Vector;

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
	private TreeMap<QuizResult, Integer> resultsMap;

	public QuizStats(Vector<QuizResult> quizResultVector) {
		log = Logger.getLogger("net.verza.jdict.quiz");
		log.trace("initializing class " + this.getClass().getName());
		data = quizResultVector;
		log.debug("found " + quizResultVector.size() + " questions to analize");
		resultsMap = new TreeMap<QuizResult, Integer>();
	}

	public TreeMap<QuizResult, Integer> getResultStatsMap() {
		return resultsMap;
	}

	public void computeStats() {

		// iterates through the array of stat result
		log.trace("looping through array of stats to compute user statistics");
		Iterator<QuizResult> itr = data.iterator();
		QuizResult quizResultEntry;
		while (itr.hasNext()) {
			quizResultEntry = itr.next();
			//log.trace(quizResultEntry.toString());

		
			if (resultsMap.containsKey(quizResultEntry))	{
				resultsMap.put(quizResultEntry, (resultsMap
						.get(quizResultEntry) + 1));
				log.debug("incrementing counter with value "+(resultsMap
						.get(quizResultEntry) + 1)+" inside resultsMap for the key"+quizResultEntry.toString());
			}else	{
				resultsMap.put(quizResultEntry, 1);
				log.debug("added to resultsMap key "+quizResultEntry.toString() );
			}
			log.trace("TreeMap size " + resultsMap.size());

		}
		
	}

}
