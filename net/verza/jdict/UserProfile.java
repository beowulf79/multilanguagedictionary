package net.verza.jdict;

import net.verza.jdict.quiz.QuizResult;
import org.apache.log4j.Logger;
import javax.swing.ImageIcon;
import java.util.Calendar;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.Vector;

/**
 * @author ChristianVerdelli
 * 
 */
public class UserProfile implements Serializable {

	private static final long serialVersionUID = 5760620481868028259L;
	public static final String SEPARATOR_CHAR = ",";
	public static final int CSV_FIELD_SIZE = 7;
	public static final String CSV_COMMENT = "#";

	private String name, password, pictureFile;
	private ImageIcon picture;
	private String email;
	private boolean isAdmin;
	private Vector<QuizResult> stats; // Stores QuizStat Objects
	private static Logger log;

	public UserProfile() {
		log = Logger.getLogger("net.verza.jdict");
		log.trace("initializing class " + this.getClass().getName());
		stats = new Vector<QuizResult>();
	}

	public UserProfile(String n, String p) {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("initializing class " + this.getClass().getName());
		name = n;
		password = p;
		stats = new Vector<QuizResult>();
	}

	/*
	 * 
	 */
	public String getName() {
		return name;
	}

	/*
	 * 
	 */
	public void setName(String _name) {
		if (_name == null) {
			log.error("error setting name");
			return;
		}
		name = _name;
	}

	/*
	 * 
	 */
	public String getPassword() {
		return password;
	}

	/*
	 * 
	 */
	public int setPassword(String _pwd) {
		if (_pwd == null) {
			log.error("error setting pwd");
			return -1;
		}
		password = _pwd;
		return 0;
	}

	/*
	 * 
	 */
	public ImageIcon getPicture() {
		return picture;
	}

	/*
	 * 
	 */
	public void setPicture(File _picture_file) {
		this.pictureFile = _picture_file.getAbsolutePath();
		this.picture = new ImageIcon(this.pictureFile);
		System.out.println("imageicon " + picture.getImage().getSource());
	}

	/*
	 * 
	 */
	public String getEmail() {
		return email;
	}

	/*
	 * 
	 */
	public int setEmail(String _email) {
		if (_email == null) {
			log.error("error setting email");
			return -1;
		}
		email = _email;
		return 0;
	}

	/*
	 * 
	 */
	public boolean getIsAdmin() {
		return isAdmin;
	}

	/*
	 * 
	 */
	public void setIsAdmin(boolean _isAdmin) {
		isAdmin = _isAdmin;
	}

	/*
	 * 
	 */
	// public Vector getAllQuizStat() {
	public Vector<QuizResult> getQuizStat() {
		log.debug("returning stats of size " + stats.size());
		return stats;
	}

	/*
	 * 
	 */
	// public QuizResult getQuizStat(int index) {
	public QuizResult getQuizStat(int index) {
		return (QuizResult) stats.get(index);
	}

	/*
	 * 
	 */
	public int addQuizStat(QuizResult quiz) {
		log.trace("adding single stat to the Stats Vector into " + this.name
				+ " profile");
		stats.add(quiz);
		return 0;
	}

	/*
	 * 
	 */
	public int addQuizStat(Vector<QuizResult> st) {
		log.debug("adding " + st.size() + " to the Stats Vector into "
				+ this.name + " profile");
		stats.addAll(st);
		return 0;
	}

	/*
	 * 
	 */
	public void addQuizStat(File csvStats) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(csvStats));
		String str;
		while ((str = in.readLine()) != null) {
			String[] tmp = new String[0];

			if ((str.trim().startsWith(CSV_COMMENT)) || //skip comments
					(tmp = str.split(SEPARATOR_CHAR)).length < CSV_FIELD_SIZE) { // skip invalid lines
				log.error("found comment or invalid record, skipping");
				continue;
			}

			QuizResult qr = new QuizResult();
			qr.setData(tmp[0]);
			qr.setWordID(tmp[1]);
			qr.setQuestion(tmp[2]);
			qr.setQuizExitCode(tmp[3]);
			qr.setQuizType(tmp[4]);
			qr.setUserAnswer(tmp[5]);
			qr.setCorrectAnswer(tmp[6]);

			addQuizStat(qr);
		}

	}

	/*
	 * 
	 */
	public void flushStatistics() {
		log.info("resetting user's statistics");
		stats.clear();
	}

	public int checkPassword(String pwd) {
		int result = 1;
		if (this.password.equals(pwd))
			result = 0;
		return result;
	}

	public String toString() {
		return "User Name " + this.getName() + " Password "
				+ this.getPassword() + " Email " + this.getEmail() + " ";
	}

	public String export2csv() {

		String results = new String();
		Iterator<QuizResult> itr = stats.iterator();
		while (itr.hasNext()) {
			results = results.concat(itr.next().export2csv());
		}
		return "# Exported " + Calendar.getInstance().getTime() + "\n"
				+ CSV_COMMENT + "\n" + results;

	}

}
