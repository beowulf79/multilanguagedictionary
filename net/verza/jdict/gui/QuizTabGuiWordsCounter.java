package net.verza.jdict.gui;


import net.verza.jdict.dictionary.*;
import net.verza.jdict.exceptions.*;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.DatabaseException;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import com.sleepycat.je.DatabaseEntry;
import org.apache.log4j.*;

/**
 * This class receives a string array of section or category and
 * for each value counts in the database how many words.
 * 
 * It builds a string array which is the copy of the one received 
 * adding besides each value the number of the words in the database.
 * 
 *
 * @author Christian Verdelli
 */

public class QuizTabGuiWordsCounter {

	private Dictionary dit;
	private String language;
	private String index;
	private String[] input;
	private String[] output;
	private static Logger log;

	public QuizTabGuiWordsCounter() throws DatabaseException,
			DynamicCursorException, DataNotFoundException,
			UnsupportedEncodingException, FileNotFoundException {

		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("called class " + this.getClass().getName());
		dit = Factory.getDictionary();

	}

	public void setInputData(String[] _inp){
		log.trace("called function setInputData with an array having lenght"+_inp.length);
		this.input = _inp;
		output = new String[this.input.length];
	}
	
	public void setIndex(String _ind) {
		log.trace("called function setIndex with value " + _ind);
		this.index = _ind;
	}

	public void setLanguage(String _lang) {
		log.trace("called function setLanguage with value " + _lang);
		this.language = _lang;
	}

	public String[] search(String type) throws DatabaseException, DynamicCursorException,
			DataNotFoundException, UnsupportedEncodingException,
			FileNotFoundException, KeyNotFoundException {

		log.trace("called function search");
		for (int i = 0; i < this.input.length; i++) {
			int count = 0;
			count = this.search(this.input[i],type);
			this.output[i] = new String(this.input[i]).concat("("+count+")");
			log.debug("new value is "+this.output[i]);
		}

		return this.output;
	}

	public int search(String _key,String function) throws DatabaseException,
			DynamicCursorException, DataNotFoundException,
			UnsupportedEncodingException, FileNotFoundException,
			KeyNotFoundException {
		log.trace("called function search with value " + _key);

		DatabaseEntry entry = new DatabaseEntry();
		String key = new String();
		if(function.equals("section")) 	
			key = dit.readSectionDatabase(_key); 
		else if(function.equals("category")) 
			key = dit.readCategoryDatabase(_key); 
		
		StringBinding.stringToEntry(key, entry);
		log.debug("searching in db how many entries have index " + _key);
		dit.setSearchKey(this.index, entry, this.language);

		int entries = dit.read(this.language).size();
		log.debug("entries found that match the criteria " + entries);

		return entries;

	}

}
