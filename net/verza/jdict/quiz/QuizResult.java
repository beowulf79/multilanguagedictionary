package net.verza.jdict.quiz;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Calendar;

/**
 * @author ChristianVerdelli
 * 
 */
public class QuizResult implements Serializable, Comparable {

	static final long serialVersionUID = 5760620481868028259L;

	private String date;
	private String wordId;
	private Object question;
	private String notes;
	private String quizExitCode;
	private String quizType;
	private String userAnswer;
	private String correctAnswer;

	private static Logger log;

	/*
	 * Default Constructor
	 */
	public QuizResult() {
		log = Logger.getLogger("net.verza.jdict.quiz");
		log.trace("initializing QuizResult class");

		date = Calendar.getInstance().getTime().toString();
		wordId = null;
		question = null;
		quizExitCode = "-1";
		quizType = null;
		userAnswer = null;
		correctAnswer = null;

	}

	/*
	 * 
	 */
	public QuizResult(String q, String ec, String qt, String ua) {
		wordId = q;
		quizExitCode = ec;
		quizType = qt;
		userAnswer = ua;
	}

	/*
	 * 
	 */
	public void setData(String _date) {
		date = _date;
	}

	/*
	 * 
	 */
	public String getData() {
		return date;
	}

	/*
	 * 
	 */
	public String getWordID() {
		return wordId;
	}

	/*
	 * 
	 */
	public int setWordID(String newID) {
		if (newID.equals(null)) {
			log.error("Error Setting Word_ID in the statistic Object");
			return -1;
		}
		wordId = newID;
		return 0;
	}

	/*
	 * 
	 */
	public String getQuizExitCode() {
		return quizExitCode;
	}

	/*
	 * 
	 */
	public int setQuizExitCode(String qEC) {
		if (qEC.equals(null)) {
			log.error("Error Setting Quiz Exit Code in the statistic Object");
			return -1;
		}
		quizExitCode = qEC;
		return 0;
	}

	/*
	 * 
	 */
	public String getQuizType() {
		return quizType;
	}

	/*
	 * 
	 */
	public int setQuizType(String type) {
		if (type.equals(null)) {
			log.error("Error Setting Quiz Type in the statistic Object");
			return -1;
		}
		quizType = type;
		return 0;
	}

	/*
	 * 
	 */
	public String getUserAnswer() {
		return userAnswer;
	}

	/*
	 * 
	 */
	public int setUserAnswer(String answer) {
		if (answer.equals(null)) {
			log.error("Error Setting User Answer in the statistic Object");
			return -1;
		}
		userAnswer = answer;
		return 0;
	}

	/*
	 * 
	 */
	public Object getQuestion() {
		return question;
	}

	/*
	 * 
	 */
	public int setQuestion(Object o) {
		if (o == null) {
			log.error("Error Setting Question in the statistic Object");
			return -1;
		}
		question = o;
		return 0;
	}

	/*
	 * 
	 */
	public String getCorrectAnswer() {
		return correctAnswer;
	}

	/*
	 * 
	 */
	public int setCorrectAnswer(String ca) {
		if (ca.equals(null)) {
			log
					.error("Error Setting the Correct Answer in the statistic Object");
			return -1;
		}
		correctAnswer = ca;
		return 0;
	}

	public String toString() {

		return " Quiz Run on Date " + date + " Word_ID " + wordId
				+ " Question " + question + " quizExitCode " + quizExitCode
				+ " quizType " + quizType + " correct answer " + correctAnswer
				+ " user answer " + userAnswer;
	}

	public String export2csv() {
		return date + "," + wordId + "," + question + "," + quizExitCode + ","
				+ quizType + "," + userAnswer + "," + correctAnswer + "\n";

	}

	public int compareTo(Object _object) {
		QuizResult qr = (QuizResult) _object;

		if (qr.correctAnswer.equals(this.getCorrectAnswer())
				&& (qr.quizExitCode == this.quizExitCode)) {
			//System.out.println("qr.correctAnswer " + qr.correctAnswer
			//		+ " this.getCorrectAnswer " + this.getCorrectAnswer());
			//System.out
			//		.println("correct answer is the same, compareTo will return 0");
			return 0;
		} else {
			//	System.out
			//		.println("correct answer is NOT the same, compareTo will return -1");
			return -1;
		}
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String comment) {
		this.notes = comment;
	}

}
