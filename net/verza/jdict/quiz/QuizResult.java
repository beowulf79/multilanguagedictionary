package net.verza.jdict.quiz;

import org.apache.log4j.Logger;
import java.io.Serializable;
import java.util.Calendar;

/**
 * @author ChristianVerdelli
 * 
 */
public class QuizResult implements Serializable {

	static final long serialVersionUID = 5760620481868028259L;

	private String date;
	private String word_ID;
	private Object question;
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

		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		int hr = c.get(Calendar.HOUR);
		int mn = c.get(Calendar.MINUTE);
		int sc = c.get(Calendar.MINUTE);
		date = day + "/" + month + "/" + year + " at the time " + hr + ":" + mn
				+ ":" + sc;
		log.trace("setting quiz date to \"" + date);
		word_ID = null;
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
		word_ID = q;
		quizExitCode = ec;
		quizType = qt;
		userAnswer = ua;
	}

	public String getData() {
		return date;
	}

	/*
	 * 
	 */
	public String getWord_ID() {
		return word_ID;
	}

	/*
	 * 
	 */
	public int setWord_IID(String newID) {
		if (newID.equals(null)) {
			log.error("Error Setting Word_ID in the statistic Object");
			return -1;
		}
		word_ID = newID;
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

		return " Quiz Run on Date " + date + " Word_ID " + word_ID
				+ " Question " + question + " quizExitCode " + quizExitCode
				+ " quizType " + quizType + " correct answer " + correctAnswer
				+ " user answer " + userAnswer;
	}

}
