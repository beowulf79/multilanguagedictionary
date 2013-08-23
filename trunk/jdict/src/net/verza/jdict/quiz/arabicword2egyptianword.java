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
public class arabicword2egyptianword extends QuizAbstract {

    private static Logger log;
    private Vector<ArabWord> localKeyArray;
    private Vector<ArabWord> localDataArray;
    public String sourceLanguage;
    public String targetLanguage;

    /**
     * @throws DatabaseException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws DynamicCursorException
     * @throws DataNotFoundException
     */
    public arabicword2egyptianword() throws Exception {

	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());
	this.sourceLanguage = "arabicword";
	this.targetLanguage = "egyptianword";

    }

    @SuppressWarnings(value = "unchecked")
    public void load() throws Exception {

	int number = -1, dbsize = 0, counter = 0, max_loop_counter = 0;
	Random generator = new Random();

	localKeyArray = (Vector<ArabWord>) dit.read("arabicword").clone();
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
	    ArabWord key = localKeyArray.get(number);
	    if (null == key) {
		log.warn("word is null, skip to next word ");
		continue;
	    }

	    quizResult = new QuizResult();
	    quizResult.setQuizType(Configuration.ARABIC2EGYPTIAN);
	    quizResult.setWordID(key.getid().toString());
	    // The Question String is composed by the Singular plus the comment
	    // if present
	    quizResult.setQuestion((key.getnotes() == null) ? key.getsingular()
		    : key.getsingular());
	    quizResult.setNotes(key.getnotes());
	    quizResult.setExamples(key.getexample());
	    
	    // Save in localDataArray the word connected to this
	    localDataArray = (Vector<ArabWord>) dit.read("arabicword",
		    key.getid().toString(), "egyptianword").clone();

	    localDataArray.iterator();
	    String answer = new String();
	    for (int i = 0; i < localDataArray.size(); i++) {
		answer = answer.concat(localDataArray.get(i).getsingular()
			+ " / ");
	    }
	    log.debug("setting correct answer into stats object as "
		    + answer.substring(0, answer.length() - 1));
	    quizResult.setCorrectAnswer(answer
		    .substring(0, answer.length() - 1));

	    questions.add(counter, key);
	    log.debug("writing statistic object to array at position "
		    + counter);

	    stats.add(counter, quizResult);
	    quizResult = null;

	    counter++;
	}

	// if the iterations number has not been reached it means that there
	// were errors during
	// quiz load. throws an exception
	if (counter < iterations) {
	    log.error("errors while quiz loading");
	    throw new QuizLoadException("errors while quiz loading");
	}
	log.info("quiz successfully loaded");
    }

    public int userAnswer(int index, String userAnswer) throws Exception {

	log.debug("storing user answer " + userAnswer
		+ " inside statistic object at position " + index);

	QuizResult stObj = (QuizResult) stats.get(index);

	SearchableObject srcObj = questions.get(index);
	SearchableObject trgObj = dit.read(this.targetLanguage, userAnswer);
	if (trgObj != null)
	    if (srcObj.equals(trgObj, this.sourceLanguage)) {
		stObj.setQuizExitCode("1");
	    }

	stObj.setUserAnswer(userAnswer);
	stats.remove(index);
	stats.add(index, stObj);

	return 0;
    }

}
