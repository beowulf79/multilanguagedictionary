package net.verza.jdict;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * @author Christian Verdelli
 * 
 */
public class ArabWord extends Word implements Serializable {

	private String plural;
	private final String language = "arab";
	// Stores if it is a word or a verb
	private String type = "";
	private static Logger log;

	static final long serialVersionUID = -5752057185882277357L;

	public ArabWord() {
		super();
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("Class " + this.getClass().getName() + " initialized");
	}

	

	public ArabWord(String x, String y, byte[] b) {
		singular = x;
		plural = y;
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
		// create a string concatenating the value of linkID
		String lid = new String();
		if (this.linkid != null) {
			for (int i = 0; i < this.linkid.length; i++) {
				lid = lid + "-" + this.linkid[i].toString();
			}
		}

		return " - ID: " + id + "	- link ID: " + lid + " - language: "
				+ language + " - singolare/passato: " + singular
				+ " - plurale/presente: " + plural + " - comment: " + comment
				+ " - category: " + category + " - section: " + section
				+  "audio size" + audio;
	}

}
