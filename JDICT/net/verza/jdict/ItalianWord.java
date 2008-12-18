package net.verza.jdict;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * @author ChristianVerdelli
 * 
 */

public class ItalianWord extends Word implements Serializable {

	static final long serialVersionUID = 8658300769881800087L;
	private final String language = "italiano";
	private String type = "";// Stores if it is a word or a verb
	private static Logger log;

	public ItalianWord() {
		super();
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("Class " + this.getClass().getName() + " initialized");
	}

	

	public int setType(String newt) {
		if ((newt.equals("word")) || (newt.equals("verb"))) {
			type = newt;
			return 0;
		}
		return -1;
	}

	public String getType() {
		return type;
	}


	public String toString() {

		return " - ID: " + id + "	- link ID: " + linkidarray2string()
				+ " - language: " + language + " - singolare/infinito: "
				+ singular + " - plurale: " + plural + " - comment: " + comment
				+ " - category: " + category + " - section: " + section
				+  "audio size" + audio;
	}

}
