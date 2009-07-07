/**
 * 
 */
package net.verza.jdict.quiz;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Vector;
import net.verza.jdict.Configuration;
import net.verza.jdict.SearchableObject;
import net.verza.jdict.ArabWord;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import com.sleepycat.je.DatabaseException;
import org.apache.log4j.Logger;

/**
 * @author ChristianVerdelli
 * 
 */
public class audio2arabicverb extends QuizAbstract {

	private static Logger log;
	private Vector<ArabWord> localKeyArray;
	public String language;

	/**
	 * @throws DatabaseException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws DynamicCursorException
	 * @throws DataNotFoundException
	 */
	public audio2arabicverb() throws DatabaseException, FileNotFoundException,
			UnsupportedEncodingException, DynamicCursorException,
			DataNotFoundException {

		log = Logger.getLogger("net.verza.jdict.quiz");
		log.trace("called class " + this.getClass().getName());
		this.language = "arabicword";

	}

	@SuppressWarnings(value = "unchecked")
	public int load() throws UnsupportedEncodingException, DatabaseException,
			FileNotFoundException, DynamicCursorException,
			KeyNotFoundException, DataNotFoundException {

		int number;
		int dbsize = 0;
		int counter = 0;
		Random generator = new Random();

		localKeyArray = (Vector<ArabWord>) dit.read(this.language).clone();
		dbsize = localKeyArray.size();
		//if db size is 0 let's throw an exception key not found
		if (dbsize == 0)
			throw new KeyNotFoundException(
					"No record found for the specified key");
		log.trace("key vector size outside loop " + localKeyArray.size());

		System.out.println("counter " + counter + " iterations " + iterations);

		while (counter < iterations) {
			quizResult = new QuizResult();
			log.trace("iteration number " + counter);
			log.trace("database size " + dbsize);

			number = generator.nextInt(dbsize);
			log.debug("random generated index " + number);
			log.trace("key vector size inside loop " + localKeyArray.size());
			ArabWord key = localKeyArray.get(number);

			quizResult.setQuizType(Configuration.AUDIO2EGYPTIAN);
			quizResult.setWordID(key.getid().toString());

			// The Question String is composed by the audio object
			quizResult.setQuestion(key.getaudio());
			quizResult.setNotes(key.getnotes());
			// The correct Answer is the singular of the word asked 
			String answer = key.getsingular() + " / " + key.getplural();

			log.info("setting correct answer into stats object as " + answer);
			quizResult.setCorrectAnswer(answer);
			questions.add(counter, key);
			log.trace("Writing statistic Object to Array index " + counter);

			stats.add(counter, quizResult);
			quizResult = null;

			counter++;
		}

		return 0;
	}

	public int userAnswer(int index, String userAnswer)
			throws DatabaseException, FileNotFoundException,
			UnsupportedEncodingException, DynamicCursorException,
			DataNotFoundException, KeyNotFoundException {

		log.trace("storing user answer " + userAnswer
				+ " inside statistic object at position " + index);

		QuizResult stObj = (QuizResult) stats.get(index);
		SearchableObject srcObj = questions.get(index);
		SearchableObject trgObj = dit.read(this.language, userAnswer);
		if (trgObj != null)
			if (srcObj.getid().equals(trgObj.getid())) {
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
