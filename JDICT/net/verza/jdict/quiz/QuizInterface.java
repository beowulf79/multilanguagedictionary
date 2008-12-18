package net.verza.jdict.quiz;

import java.util.Vector;

import net.verza.jdict.Dictionary;
import net.verza.jdict.sleepycat.Factory;
import net.verza.jdict.Word;
import net.verza.jdict.WordComparator;
import net.verza.jdict.IWord;
import net.verza.jdict.sleepycat.datastore.SleepyWordsDatabase;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import net.verza.jdict.exceptions.*;

import org.apache.log4j.*;

public abstract class QuizInterface {

	protected WordComparator wc;
	protected Logger log;

	protected Dictionary dit;
	protected SecondaryDatabase secondary_database;
	protected Vector<Word> questions;
	protected Vector<QuizResult> stats;
	protected QuizResult quizResult;
	protected int iterations;

	
	public QuizInterface() throws DatabaseException, FileNotFoundException,
			UnsupportedEncodingException, DynamicCursorException,
			DataNotFoundException {

		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());
		dit = Factory.getDictionary();
		questions = new Vector<Word>();
	}

	
	public void setIterations(int newiterations) {
		log.debug("setting iterations to " + newiterations);
		iterations = newiterations;
		stats = new Vector<QuizResult>(iterations);
	}

	
	public void setIterations(String newiterations) {
		log.debug("setting iterations to " + newiterations);
		Integer ig = new Integer(newiterations);
		iterations = ig;
		stats = new Vector<QuizResult>(iterations);
	}
	
	
	public int getIterations()	{
		return this.iterations;
	}

	
	public void setCategoryIndex(String value) throws DatabaseException,
			FileNotFoundException, UnsupportedEncodingException,
			DynamicCursorException, DataNotFoundException {

		log.debug("activating sub index on database with value " + value);
		DatabaseEntry entry = new DatabaseEntry();
		StringBinding.stringToEntry(value, entry);
		dit
				.setSearchKey(SleepyWordsDatabase.getCategory_IndexDatabase(),
						entry);
	}

	
	public void setSectionIndex(String value) throws DatabaseException,
			FileNotFoundException, UnsupportedEncodingException,
			DynamicCursorException, DataNotFoundException {

		log.debug("activating sub index on database with value " + value);

		DatabaseEntry entry = new DatabaseEntry();
		StringBinding.stringToEntry(value, entry);
		dit
				.setSearchKey(SleepyWordsDatabase.getSection_IndexDatabase(),
						entry);
	}

	
	public void setSecondaryDatbase(SecondaryDatabase secondaryDatabase)
			throws DatabaseException {

		log.debug("Received SecondaryDatabase "
				+ secondaryDatabase.getDatabaseName());
		secondary_database = secondaryDatabase;

	}

	
	public Vector<QuizResult> getStats() {
		log.trace("returning stats object with size " + stats.size());
		return stats;
	}

	
	public String readAnswer() throws IOException {

		System.out.print("Enter the answer: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		return new String(br.readLine());
	}

	
	public String getUserAnswer(int index) {
		if (index < 0) {
			log.error("error, wrong index ");
			return null;
		}
		QuizResult st = (QuizResult) stats.get(index);
		log.debug("returning answer with index " + index + "and value "
				+ st.getUserAnswer());
		return st.getUserAnswer();
	}

	
	public Object getQuestion(int index) {
		if ((index < 0) || (index > iterations)) {
			log.error("error, wrong index ");
			return null;
		}
		log.trace("returning question with index " + index);
		QuizResult st = (QuizResult) stats.get(index);
		log.debug("returning question value " + st.getQuestion());
		return st.getQuestion();
	}

	
	public int userAnswer(int index, String userAnswer)
			throws DatabaseException, FileNotFoundException,
			UnsupportedEncodingException, DynamicCursorException,
			DataNotFoundException {

		log.trace("storing user answer " + userAnswer
				+ " inside statistic object at position " + index);

		QuizResult stObj = (QuizResult) stats.get(index);
		wc = new WordComparator(secondary_database,
				(IWord) questions.get(index), userAnswer);
		if (wc.getResult()) {
			stObj.setQuizExitCode("1");
		} else {
			stObj.setQuizExitCode("-1");
		}
		stObj.setUserAnswer(userAnswer);
		stats.remove(index);
		stats.add(index, stObj);
		log.info(stObj.toString());

		return 0;
	}

	
	public abstract int load() throws UnsupportedEncodingException,
			DatabaseException, FileNotFoundException, DynamicCursorException,
			DataNotFoundException;

}
