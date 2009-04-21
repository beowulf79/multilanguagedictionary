package net.verza.jdict;

import net.verza.jdict.quiz.QuizResult;

import org.apache.log4j.Logger;

/**
 * 
 */
import java.io.Serializable;
import java.util.Vector;

/**
 * @author ChristianVerdelli
 * 
 */
public class UserProfile implements Serializable {

	static final long serialVersionUID = 5760620481868028259L;

	private String name;
	private String password;
	private byte[] picture;
	private String email;
	private int userType;
	private Vector<QuizResult> stats; // Stores QuizStat Objects

	private static Logger log;

	
	public UserProfile() {
		log = Logger.getLogger("net.verza.jdict");
		log.trace("initializing class "+this.getClass().getName());
		stats = new Vector<QuizResult>();
	}
	
	public UserProfile(String n, String p) {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("initializing class "+this.getClass().getName());
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
	public int setName(String newname) {
		if (newname.endsWith(null)) {
			log.equals("Error setting new Name");
			return -1;
		}
		name = newname;
		return 0;
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
	public int setPassword(String newpwd) {
		if (newpwd.endsWith(null)) {
			log.equals("Error setting new Name");
			return -1;
		}
		password = newpwd;
		return 0;
	}

	/*
	 * 
	 */
	public byte[] getPicture() {
		return picture;
	}

	/*
	 * 
	 */
	public int setPicture(byte[] newpic) {
		picture = newpic;
		return 0;
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
	public int setEmail(String newemail) {
		if (newemail.equals(null)) {
			log.error("Error setting new email");
			return -1;
		}
		email = newemail;
		return 0;
	}

	/*
	 * 
	 */
	public int getUserType() {
		return userType;
	}

	/*
	 * 
	 */
	public int setUserType(int newtype) {
		userType = newtype;
		return 0;
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

}
