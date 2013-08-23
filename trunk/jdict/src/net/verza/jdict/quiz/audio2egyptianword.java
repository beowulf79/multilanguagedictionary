/**
 * 
 */
package net.verza.jdict.quiz;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Vector;

import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.QuizLoadException;
import net.verza.jdict.model.ArabWord;
import net.verza.jdict.model.SearchableObject;
import net.verza.jdict.properties.Configuration;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

/**
 * @author ChristianVerdelli
 * 
 */
public class audio2egyptianword extends QuizAbstract {

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
    public audio2egyptianword() throws Exception {

	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());
	this.language = "egyptianword";

    }

    @SuppressWarnings(value = "unchecked")
    public void load() throws Exception {

	int number = -1, dbsize = 0, counter = 0, max_loop_counter = 0;
	Random generator = new Random();

	localKeyArray = (Vector<ArabWord>) dit.read(this.language).clone();
	dbsize = localKeyArray.size();
	// if db size is 0 let's throw an exception key not found
	if (dbsize == 0)
	    throw new KeyNotFoundException(
		    "No record found for the specified key");
	log.debug("key vector size outside loop " + localKeyArray.size());

	while ((max_loop_counter++ < Configuration.QUIZMAXLOOPS)
		&& (counter < iterations)) {
	    log.debug("iteration number " + counter);
	    log.debug("database size " + dbsize);
	    number = generator.nextInt(dbsize);
	    log.debug("random generated index " + number);
	    log.debug("key vector size inside loop " + localKeyArray.size());
	    ArabWord key = localKeyArray.get(number);
	    if (key.getaudiosingularbyte().length == 0) {
		log.error("audio for the word not present, skip to next word ");
		continue;
	    }

	    quizResult = new QuizResult();
	    quizResult.setQuizType(Configuration.AUDIO2EGYPTIAN);
	    quizResult.setWordID(key.getid().toString());
	    // The Question String is composed by the audio object
	    quizResult.setQuestion(key.getaudiosingular());
	    quizResult.setNotes(key.getnotes());
	    quizResult.setExamples(key.getexample());
	    
	    // The correct Answer is the singular/plural of the word asked
	    String answer = key.getsingular() + " / " + key.getplural();

	    log.info("setting correct answer into stats object as " + answer);
	    quizResult.setCorrectAnswer(answer);
	    questions.add(counter, key);
	    log.debug("Writing statistic Object to Array index " + counter);

	    stats.add(counter, quizResult);
	    quizResult = null;

	    counter++;
	}

	// if the iterations number has not been reached it means that there
	// were errors during
	// quiz load. throws an exception
	if (counter < iterations) {
	    log.error("error loading quiz");
	    throw new QuizLoadException("errors while quiz loading");
	}
	log.info("quiz successfully loaded");
    }

    public int userAnswer(int index, String userAnswer) throws Exception {

	log.debug("storing user answer " + userAnswer
		+ " inside statistic object at position " + index);

	QuizResult stObj = (QuizResult) stats.get(index);
	SearchableObject srcObj = questions.get(index);
	SearchableObject trgObj = dit.read(this.language, userAnswer);
	if (trgObj != null)
	    if (srcObj.getid().equals(trgObj.getid())) {
		stObj.setQuizExitCode("1");
	    }

	stObj.setUserAnswer(userAnswer);
	stats.remove(index);
	stats.add(index, stObj);

	return 0;
    }
}
