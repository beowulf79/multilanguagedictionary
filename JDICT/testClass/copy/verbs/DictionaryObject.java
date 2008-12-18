package testClass.copy.verbs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.verza.jdict.PropertiesLoader;
import org.apache.log4j.Logger;


public class DictionaryObject implements Serializable, SearchableObject {

	private static final long serialVersionUID = 8834844282811448538L;
	protected Integer id;
	protected Map<String, Integer[]> linkId;
	private static Logger log;
	//private Integer[] sinonymous_id;
	//private Integer[] contrarious_id;
	protected String notes;
	protected Set<String> section;
	protected Set<String> category;
	protected byte[] audio;
	
	public DictionaryObject() {

		log = Logger.getLogger("testClass.copy.verbs");
		log.trace("class " + this.getClass().getName() + " initialized");
		linkId = new HashMap<String, Integer[]>();
		
	}

	public Integer getid() {
		if (this.id == null) {
			// http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
			// log.error("not valid value found for ID");
			return null;
		}
		log.trace("called method getid");
		return id;
	}

	public void setid(String newvalue) throws NullPointerException {
		log.trace("called method setid with argument " + newvalue);
		if (newvalue == null)
			throw new NullPointerException(
					"setting Infinitive with a null string");

		this.id = new Integer(newvalue);
		log.trace(" id has been set to " + newvalue);
	}

	public Integer[] getlinkid(String language) {
		if (this.linkId == null) {
			// http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
			// log.error("linkID empty for this word");
			return null;
		}
		// log.trace("called method getlinkid");
		return linkId.get(language);
	}

	public Map<String, Integer[]> getlinkid() {
		if (this.linkId == null) {
			// http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
			// log.error("linkID empty for this word");
			return null;
		}
		// log.trace("called method getlinkid");
		return this.linkId;
	}

	public void setlinkid(String language, String newvalue)
			throws NullPointerException {
		log.trace("called method setlinkid with language " + language
				+ " and value  " + newvalue);
		if (newvalue == null)
			throw new NullPointerException("setting linkid with a null string");

		String[] ids = newvalue.split(PropertiesLoader.getProperty("multivalue_separator"));

		Integer[] ids_integer = Utility.StringAR2IntgerAR(ids);
		this.linkId.put(language, ids_integer);
	}

	public String getnotes() {
		// http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
		// log.trace("called method getsingular");
		return notes;
	}

	
	public void setnotes(String newvalue) throws NullPointerException {
		if (newvalue == null)
			throw new NullPointerException("setting notes with a null string");

		this.notes = newvalue;
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

	
	public Set<String> getsection() {
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
	
	
}
