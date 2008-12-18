package testClass.copy.verbs;


import org.apache.log4j.Logger;

/**
 * @author Christian Verdelli
 * 
 */
public class ArabWord extends Word {

	private String plural;
	private static Logger log;
	static final long serialVersionUID = -5752057185882277357L;

	
	public ArabWord() {
		super();
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("Class " + this.getClass().getName() + " initialized");
	}

	
	public void setplural(String _plural)	{
		this.plural = _plural;
	}
	
	
	public String getplural()	{
		
		return this.plural;
	}


	public String toString() {

		return super.toString().concat(" plurale "+plural);
	}

	
}
