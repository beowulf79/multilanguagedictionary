package net.verza.jdict;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.DatabaseException;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.sleepycat.Factory;
import org.apache.log4j.Logger;


/**
 * @author Christian Verdelli
 * 
 */
public class WordComparator {

	private Dictionary dit;
	private boolean result;
	private IWord w1;
	private IWord w2;
	private Logger log;
	private static SecondaryDatabase secondary_database;

	
	// ////////////////////////////////////////////////////////////////////////////////
	public WordComparator() {

		result = false;
	}

	
	// ////////////////////////////////////////////////////////////////////////////////
	public WordComparator(SecondaryDatabase db_index, IWord src, String dst)
			throws DatabaseException, FileNotFoundException,
			UnsupportedEncodingException, DataNotFoundException,
			DynamicCursorException {

		log = Logger.getLogger("XMLCOMPARER_Logger");

		log.debug("Called WordComparator constructor with arguments "
				+ ", ID of the Word to Compare " + src.getid()
				+ " SecondaryDatabase " + db_index.getDatabaseName()
				+ ", User answer " + dst);

		secondary_database = db_index;
		dit = Factory.getDictionary();
		w1 = src;

		this.compareAnswer(dst);
	}

	
	
	public void setSecondaryDatabase(SecondaryDatabase db_index) {
		try {
			log.debug("setting secondary index database "
					+ db_index.getDatabaseName());
			secondary_database = db_index;
		} catch (DatabaseException e) {
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}

	
	// ////////////////////////////////////////////////////////////////////////////////
	public IWord set_dst_ID(String answer) throws DatabaseException,
			FileNotFoundException, UnsupportedEncodingException,
			DataNotFoundException, DynamicCursorException {

		log.debug("setting user answer to " + answer);
		try {
			dit.setSearchKey(secondary_database, new DatabaseEntry(answer
					.getBytes("UTF-8")));
			dit.read();
		} catch (DataNotFoundException e) {
			return null;
		}

		return dit.getKey(0);
	}

	
	
	public boolean getResult() {
		log.info("returning result " + this.result);
		return this.result;
	}

	
	// ////////////////////////////////////////////////////////////////////////////////
	/*
	 * Compared the random word/audio catched from the db with the user's answer
	 * 
	 */
	public void compareAnswer(String answer) throws DatabaseException,
			FileNotFoundException, UnsupportedEncodingException,
			DataNotFoundException, DynamicCursorException {

		w2 = this.set_dst_ID(answer);

		// Check if the answer has been found in the database
		if (w2 == null) {
			log.error("empty user answer");
			return;
		}

		if ( (w2.getid() == null) 
				|| (this.w1.getid() == null) ) {
			log.error("no data to compare with");
			return;
		}

		// Check with ID of the Word with the ID of user's answer found Word
		log.debug("comparing ID: " + this.w1.getid() + " with ID: "
				+ w2.getid());
		if (this.w1.getid().equals(w2.getid())) {
			this.result = true;
			log.info("The words's ID is the same");
			return;
		}

		// Check with the Link_ID of the Word with the ID of the user's answer
		// found Word
		if (w1.getlinkid() != null) {
			String[] tmp = w1.getlinkid();
			String[] src_linkID = tmp[0].split(",");
			log.debug("LinkID lenght " + src_linkID.length);
			log.trace("looping through link ID");
			for (int i = 0; i < src_linkID.length; i++) {
				log.trace("comparing " + src_linkID[i] + " with " + w2.getid());
				if (src_linkID[i].equals(w2.getid())) {
					log.trace("setting result to true");
					this.result = true;
					break;
				}
			}
		}

	}

}