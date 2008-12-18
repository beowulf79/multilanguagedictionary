/**
 * 
 */
package net.verza.jdict.quiz;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Vector;

import net.verza.jdict.Configuration;
import net.verza.jdict.Word;
import net.verza.jdict.IWord;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.sleepycat.datastore.SleepyWordsDatabase;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import org.apache.log4j.Logger;

/**
 * @author ChristianVerdelli
 * 
 */
public class Audio2ItalianQuiz extends QuizInterface {

	private static Logger log;
	private Vector<Word> localKeyArray;
	private Vector<Word> localDataArray;

	/**
	 * @throws DatabaseException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws DynamicCursorException
	 * @throws DataNotFoundException
	 */
	public Audio2ItalianQuiz() throws DatabaseException,
			FileNotFoundException, UnsupportedEncodingException,
			DynamicCursorException, DataNotFoundException {

		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());

	}


	@SuppressWarnings(value = "unchecked")
	public int load() throws UnsupportedEncodingException, DatabaseException,
			FileNotFoundException, DynamicCursorException,
			DataNotFoundException {

		int number;
		int dbsize = 0;
		int counter = 0;
		IWord w;
		Random generator = new Random();
		
		dbsize = dit.getSize();
		dit.read();
		localKeyArray = (Vector<Word>) dit.getKey().clone();
		localDataArray = (Vector<Word>) dit.getData().clone();
		log.trace("key vector size outside loop " 
						+ localKeyArray.size());
		log.trace("data vector size outside loop "
						+ localDataArray.size());
		
		
		while (counter < iterations) {
			quizResult = new QuizResult();
			log.trace("iteration number " + counter);
			log.trace("database size " + dbsize);

			number = generator.nextInt(dbsize);
			log.debug("random generated index " + number);
			log.trace("key vector size inside loop " + localKeyArray.size());			
			Word key = localKeyArray.get(number);

			log.trace("setting quiztype to AUDIO2ITALIAN " + " Word_ID "
					+ key.getid() + " audio stream size " + key.getaudio());
			quizResult.setQuizType(Configuration.AUDIO2ITALIAN);
			quizResult.setWord_IID(key.getid());
			// The Question String is composed by the audio object
			quizResult.setQuestion( (key.getcomment() == null) ? key
					.getaudio() : key.getaudio() );

			// The correct Answer is the singular of the word asked to
			// the user plus its linkedID words concatenated.
			String answer = key.getsingular();

			// Setting the correct answer by querying the linkedID
			String[] tmp = key.getlinkid();
			if (tmp != null) {
				String[] src_linkID = tmp[0].split(",");
				log.trace("LinkID lenght " + src_linkID.length);
				for (int i = 0; i < src_linkID.length; i++) {
					log.debug("searching ID " + src_linkID[i]);
					dit.setSearchKey(SleepyWordsDatabase.getID_IndexDatabase(),
							new DatabaseEntry(src_linkID[i].getBytes("UTF-8")));
					dit.read();
					// Querying by ID only returns one entry so just retrieve
					// the first element of the Vector
					w = (IWord) dit.getKey(0);
					log.trace("adding result " + w.getsingular()
							+ " to the correct answer string");
					answer = answer.concat(" , " + w.getsingular());
				}

			} else
				log.debug("single answer;no need to recourse the linkedID");

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

}
