package testClass.copy.verbs;


import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import net.verza.jdict.PropertiesLoader;



public class Verb extends DictionaryObject implements Serializable, IVerb {

	static final long serialVersionUID = 8834844282811448538L;
	private static Logger log;


	String infinitive;
	String notes;
	Set<String> section;
	byte[] audio;


	
	
	public Verb() {
		
		super();
		log = Logger.getLogger("testClass.copy.verbs");
		log.trace("class " + this.getClass().getName() + " initialized");
		infinitive = new String();
		notes = new String();
		section = new HashSet<String>();
		
		audio = new byte[0];

		
	}
	
	
	public String getinfinitive() {
		return infinitive;
	}

	
	public void setinfinitive(String newvalue) throws NullPointerException {
		if (newvalue == null)
			throw new NullPointerException(
					"setting Infinitive with a null string");
		this.infinitive = newvalue;
	}

	
	
	public Object getaudio() {
		// http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
		// log.trace("called method getaudio");
		System.out.println("called method getaudio");
		return audio;
	}

	
	public void setaudio(byte[] newvalue) throws NullPointerException {
		if (newvalue == null)
			throw new NullPointerException("setting audio with a null string");
		log.trace("setting audio having size " + newvalue.length);
		this.audio = newvalue;
	}

	
	public Set<String
	> getsection() {
		// http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
		// log.trace("called method getsection");
		return section;
	}

	
	public void setsection(String newvalue) throws NullPointerException {
		log.trace("called method setsection with argument " + newvalue);
		if (newvalue == null) {
			throw new NullPointerException("setting section with a null string");
		}
		this.section = new HashSet<String>(Arrays.asList(newvalue
				.split(PropertiesLoader.getProperty("multivalue_separator"))));

	}

	
	public String toString() {

		return " - ID: " + id + " - infinitive: " + infinitive + " - link ID: "
				+ linkId.toString() + " - note: " + notes + " - section: "
				+ section.toString() + " - audio size" + audio.length;

	}


}
