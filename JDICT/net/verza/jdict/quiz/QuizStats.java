package net.verza.jdict.quiz;

import net.verza.jdict.Configuration;
import org.apache.log4j.Logger;
import java.util.Hashtable;
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
	private Hashtable<String, Integer> wrongs, guessed;

	public QuizStats(Vector<QuizResult> quizResultVector) {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("initializing class "+this.getClass().getName());
		data = quizResultVector;
		log.debug("found " + quizResultVector.size() + " questions to analize");
		wrongs = new Hashtable<String, Integer>();
		guessed = new Hashtable<String, Integer>();
	}

	public Hashtable<String, Integer> getGuessedHash() {
		return guessed;
	}

	public Hashtable<String, Integer> getWrongHash() {
		return wrongs;
	}

	public void computeStats() {
		int counter = 0;
		// goes through the array of stat result
		log.debug("looping through array of stats to compute user statistics");
		while (data.size() > counter) {
			QuizResult qr = (QuizResult) data.get(counter);
			log.debug(qr.toString());
			
			if (qr.getQuizExitCode().equals(Configuration.CORRECTANSWER)) {
				if (guessed.containsKey(qr.getCorrectAnswer())) {
					Integer newcount = (Integer) guessed.get(qr
							.getCorrectAnswer());
					newcount++;
					log.trace("incrementing correct counter for the word "
							+ qr.getCorrectAnswer());
					guessed.put(qr.getCorrectAnswer(), newcount);
				} else
					guessed.put(qr.getCorrectAnswer(), 1);
			} else if (qr.getQuizExitCode().equals(Configuration.WRONGANSWER)) {
				if (wrongs.containsKey(qr.getCorrectAnswer())) {
					Integer newcount = (Integer) wrongs.get(qr
							.getCorrectAnswer());
					newcount++;
					log.trace("incrementing wrongs counter for the word "
							+ qr.getCorrectAnswer());
					wrongs.put(qr.getCorrectAnswer(), newcount);
				} else
					wrongs.put(qr.getCorrectAnswer(), 1);
			}

			counter++;
		}

		log.trace("guessed hashtable size " + guessed.size());
		log.trace("wrong hashtable size " + wrongs.size());

	}

}
