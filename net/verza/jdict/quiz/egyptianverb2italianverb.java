/**
 * 
 */
package net.verza.jdict.quiz;

import net.verza.jdict.exceptions.LinkIDException;
import java.io.FileNotFoundException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import java.io.UnsupportedEncodingException;

import java.util.Random;
import java.util.Vector;
import net.verza.jdict.Configuration;
import net.verza.jdict.SearchableObject;
import net.verza.jdict.Verb;
import net.verza.jdict.ArabVerb;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.QuizLoadException;

import com.sleepycat.je.DatabaseException;
import org.apache.log4j.Logger;

/**
 * @author ChristianVerdelli
 * 
 */
public class egyptianverb2italianverb extends QuizAbstract {

	private static Logger log;
	private Vector<ArabVerb> localKeyArray;
	private Vector<Verb> localDataArray;

	/**
	 * @throws DatabaseException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws DynamicCursorException
	 * @throws DataNotFoundException
	 */
	public egyptianverb2italianverb() throws DatabaseException,
			FileNotFoundException, UnsupportedEncodingException,
			DynamicCursorException, DataNotFoundException {

		log = Logger.getLogger("net.verza.jdict.quiz");
		log.trace("called class " + this.getClass().getName());
		sourceLanguage = "egyptianverb";
		targetLanguage = "italianverb";

	}

	@SuppressWarnings(value = "unchecked")
	public void load() throws UnsupportedEncodingException, DatabaseException,
			FileNotFoundException, DynamicCursorException,
			KeyNotFoundException, DataNotFoundException, LinkIDException,
			QuizLoadException {

		int number = -1, dbsize = 0, counter = 0, max_loop_counter = 0;
		Random generator = new Random();

		localKeyArray = (Vector<ArabVerb>) dit.read("egyptianverb").clone();
		dbsize = localKeyArray.size();
		// if db size is 0 let's throw an exception key not found
		if (dbsize == 0)
			throw new KeyNotFoundException(
					"No record found for the specified key");
		log.trace("key vector size outside loop " + localKeyArray.size());

		while ((max_loop_counter++ < Configuration.QUIZMAXLOOPS)
				&& (counter < iterations)) {
			log.trace("iteration number " + counter);
			log.trace("database size " + dbsize);
			number = generator.nextInt(dbsize);
			log.debug("random generated index " + number);
			log.trace("key vector size inside loop " + localKeyArray.size());
			Verb key = localKeyArray.get(number);
			if (null == key) {
				log.error("word is null, skip to next word ");
				continue;
			}

			quizResult = new QuizResult();
			quizResult.setQuizType(Configuration.EGYPTIAN2ITALIAN);
			quizResult.setWordID(key.getid().toString());
			// The Question String is composed by the Singular plus the comment
			// if present
			quizResult.setQuestion((key.getnotes() == null) ? key
					.getinfinitive() : key.getinfinitive());
			quizResult.setNotes(key.getnotes());

			// Save in localDataArray the word connected to this
			localDataArray = (Vector<Verb>) dit.read("egyptianverb",
					key.getid().toString(), "italianverb").clone();

			localDataArray.iterator();
			String answer = new String();
			for (int i = 0; i < localDataArray.size(); i++) {
				answer = answer.concat(localDataArray.get(i).getinfinitive())
						+ " / ";
			}
			log.info("setting correct answer into stats object as "
					+ answer.substring(0, answer.length() - 1));
			quizResult.setCorrectAnswer(answer
					.substring(0, answer.length() - 1));

			questions.add(counter, key);
			log.trace("Writing statistic Object to Array index " + counter);

			stats.add(counter, quizResult);
			quizResult = null;

			counter++;
		}
		//if the iterations number has not been reached it means that there were errors during
		//quiz load. throws an exception 
		if(counter < iterations)
			throw new QuizLoadException("errors while quiz loading");
	}

	public int userAnswer(int index, String userAnswer)
			throws DatabaseException, FileNotFoundException,
			UnsupportedEncodingException, DynamicCursorException,
			DataNotFoundException, KeyNotFoundException {

		log.trace("storing user answer " + userAnswer
				+ " inside statistic object at position " + index);

		QuizResult stObj = (QuizResult) stats.get(index);

		SearchableObject srcObj = questions.get(index);
		SearchableObject trgObj = dit.read(this.targetLanguage, userAnswer);
		if (trgObj != null)
			if (srcObj.equals(trgObj, this.sourceLanguage)) {
				stObj.setQuizExitCode("1");
				System.out.println("compared is ok");
			}

		stObj.setUserAnswer(userAnswer);
		stats.remove(index);
		stats.add(index, stObj);
		log.info(stObj.toString());

		return 0;
	}

}
