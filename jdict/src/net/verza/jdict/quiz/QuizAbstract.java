package net.verza.jdict.quiz;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.model.SearchableObject;

import org.apache.log4j.Logger;

import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.SecondaryDatabase;

public abstract class QuizAbstract {

    protected Logger log;
    protected Dictionary dit;
    protected SecondaryDatabase secondary_database;
    protected Vector<SearchableObject> questions;
    protected Vector<QuizResult> stats;
    protected QuizResult quizResult;
    protected int iterations;
    public String sourceLanguage;
    public String targetLanguage;

    public QuizAbstract() throws DatabaseException, FileNotFoundException,
	    UnsupportedEncodingException, DynamicCursorException,
	    DataNotFoundException, KeyNotFoundException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException {

	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());
	dit = Factory.getDictionary();
	questions = new Vector<SearchableObject>();
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

    public int getIterations() {
	return this.iterations;
    }

    public void setCategoryIndex(String value, String lang)
	    throws DatabaseException, FileNotFoundException,
	    UnsupportedEncodingException, DynamicCursorException,
	    DataNotFoundException, SecurityException, IllegalArgumentException,
	    ClassNotFoundException, NoSuchMethodException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException {

	log.debug("called function setCategoryIndex with argument " + value
		+ " and " + lang);
	log.info("setting section " + value);
	// remove the parenthesis and number before querying the section db
	String[] key = value.split("\\([0-9]+\\)");
	log.debug("splitted section value is " + key[0]);

	DatabaseEntry entry = new DatabaseEntry();
	StringBinding.stringToEntry(dit.readCategoryDatabase(key[0]), entry);
	dit.setSearchKey("category", entry, lang);

    }

    public void setSectionIndex(String value, String lang)
	    throws DatabaseException, FileNotFoundException,
	    UnsupportedEncodingException, DynamicCursorException,
	    DataNotFoundException, SecurityException, IllegalArgumentException,
	    ClassNotFoundException, NoSuchMethodException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException {

	log.debug("called function setSectionIndex with argument " + value
		+ " and " + lang);
	log.info("setting section " + value);
	// remove the parenthesis and number before querying the section db
	String[] key = value.split("\\([0-9]+\\)");
	log.debug("splitted section value is " + key[0]);

	DatabaseEntry entry = new DatabaseEntry();
	StringBinding.stringToEntry(dit.readSectionDatabase(key[0]), entry);
	dit.setSearchKey("section", entry, lang);

    }

    public void setSecondaryDatbase(SecondaryDatabase secondaryDatabase)
	    throws DatabaseException {

	log.debug("Received SecondaryDatabase "
		+ secondaryDatabase.getDatabaseName());
	secondary_database = secondaryDatabase;

    }

    public Vector<QuizResult> getStats() {
	log.debug("returning stats object with size " + stats.size());
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
	log.debug("returning question with index " + index);
	QuizResult st = (QuizResult) stats.get(index);
	log.debug("returning question value " + st.getQuestion());
	return st.getQuestion();
    }

    public String getNotes(int index) {
	return ((QuizResult) stats.get(index)).getNotes();
    }

    public abstract int userAnswer(int index, String userAnswer)
	    throws Exception;

    public abstract void load() throws Exception;

}
