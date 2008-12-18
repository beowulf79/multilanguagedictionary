package net.verza.jdict;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import net.verza.jdict.exceptions.AudioNotFoundException;
import org.apache.log4j.Logger;


/**
 * @author Christian Verdelli
 * 
 */

public class Word implements Serializable, IWord {

	static final long serialVersionUID = 8834844282811448538L;
	private static Logger log;

	// Class Attributes
	protected String language = "generic";
	protected String singular;
	protected String plural;
	protected String id;
	protected String[] linkid;
	protected String comment;
	protected Set<String> category;
	protected Set<String> section;
	protected byte[] audio;	
	
	
	public Word() {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("Class " + this.getClass().getName() + " initialized");
		section = new HashSet<String>();
		category = new HashSet<String>();
	}

	
	
	public String getlanguage() {
		//http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
		//log.trace("called method getlanguage");
		return language;
	}

	
	
	public String getsingular() {
		//http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
		//log.trace("called method getsingular");
		return singular;
	}

	
	
	public String getplural() {
		//http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
		//log.trace("called method getplural");
		return plural;
	}

	
	
	public String getid() {
		if (this.id == null) {
			//http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
			//log.error("not valid value found for ID");
			return null;
		}
		//log.trace("called method getid");
		return id;

	}

	

	public String[] getlinkid() {
		if (this.linkid == null) {
			//http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
			//log.error("linkID empty for this word");
			return null;
		}
		//log.trace("called method getlinkid");
		return linkid;
	}
	
	
	
	public Object getaudio() {
		//http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
		//log.trace("called method getaudio");
		return audio;
	}

	
	
	public int setsingular(String newvalue) {
		log.trace("called method setsingular with argument "+newvalue);
		if (newvalue == null) {
			log.error("error setting plural");
			return -1;
		}
		
		this.singular = newvalue;
		return 0;
	}
	
	
	
	public int setplural(String newvalue) {
		log.trace("called method setplural with argument "+newvalue);
		if (newvalue == null) {
			log.error("error setting plural");
			return -1;
		}
		
		this.plural = newvalue;
		return 0;
	}

	

	public void setaudio(byte[] newvalue) {
		log.trace("called method setaudio with argument "+newvalue);
		if (newvalue == null) 
			log.error("error setting plural");
		this.audio = newvalue;
	}

	
	
	public int setid(String newvalue) {
		log.trace("called method setid with argument "+newvalue);
		if (newvalue == null) {
			log.error("error setting id");
			return -1;
		}
		log.trace("setting id to "+newvalue);
		this.id = newvalue;
		return 0;
	}

	
	
	public int setlinkid(String newvalue) {
		log.trace("called method setlinkid with argument "+newvalue);
		if (newvalue == null) {
			log.error("error setting setlinkid");
			return -1;
		}
		String splitchar = (PropertiesLoader.getProperty("words.multivalue_separator"));
		this.linkid = newvalue.split(splitchar);
		return 0;
	}

	
	
	public String getcomment() {
		//http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
		//log.trace("called method getsingular");
		return comment;
	}

	
	
	public int setcomment(String newvalue) {
		log.trace("called method setcomment with argument "+newvalue);
		if (newvalue == null) {
			log.error("error setting comment");
			return -1;
		}
		this.comment = newvalue;
		return 0;
	}

	
	
	public Set<String> getsection() {
		//http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
		//log.trace("called method getsection");
		return section;
	}

	
	
	public int setsection(String newvalue) {
		log.trace("called method setsection with argument "+newvalue);
		if (newvalue == null) {
			log.error("cannot set Section with null data");
			return -1;
		}
		String splitchar = (PropertiesLoader.getProperty("words.multivalue_separator"));
		this.section = new HashSet<String>(Arrays.asList(newvalue.split(splitchar)) );

		return 0;
	}

	
	
	public Set<String> getcategory() {
		//http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
		//log.trace("called method getcategory");
		System.out.println("called getcatgory");
		System.out.println("returning category "+category);
		return category;
	}

	
	
	public int setcategory(String newvalue) {
		log.trace("called method setcategory with argument "+newvalue);
		if (newvalue == null) {
			log.error("cannot set Category with null data");
			return -1;
		}
		String splitchar = (PropertiesLoader.getProperty("words.multivalue_separator"));
		this.category = new HashSet<String>(Arrays.asList(newvalue.split(splitchar)));
		
		return 0;
	}

	
	
	public void playaudio() throws AudioNotFoundException {
		if (audio == null) {
			throw new AudioNotFoundException("audio null ");
		}
		AudioPlayer playerObject = new AudioPlayer(audio);
		try {
			playerObject.play();
		} catch (AudioNotFoundException e) {
			throw new AudioNotFoundException(e.getMessage());
		}

	}

	
	
	protected String linkidarray2string() {
		String lid = new String();
		if (this.linkid != null) {
			for (int i = 0; i < this.linkid.length; i++) {
				lid = lid + "-" + this.linkid[i].toString();
			}
		}
		return lid;
	}

	
	
	public String toString() {

		return " - ID: " + id + "	- link ID: " + linkidarray2string()
				+ " - language: " + language + " - singolare/infinito: "
				+ singular + " - plurale: " + plural + " - comment: " + comment
				+ " - category: " + category + " - section: " + section 
				+ "audio size" + audio.length;
	}
	
}
