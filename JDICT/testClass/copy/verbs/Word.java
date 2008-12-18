package testClass.copy.verbs;


import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import net.verza.jdict.PropertiesLoader;



public class Word extends DictionaryObject implements Serializable, IWord  {

	static final long serialVersionUID = 8834844282811448538L;
	private static Logger log;
	protected String singular;
	
	protected Set<String> section;
	protected Set<String> category;
	protected byte[] audio;
	
	
	public Word() {
		
		super();
		log = Logger.getLogger("testClass.copy.words");
		log.trace("class " + this.getClass().getName() + " initialized");
		singular = new String();
		notes = new String();
		section = new HashSet<String>();
		audio = new byte[0];

	}


	
	public String getsingular() {
		return this.singular;
	}

	
	public void setsingular(String newvalue) throws NullPointerException {
		if (newvalue == null)
			throw new NullPointerException(
					"setting Infinitive with a null string");
		this.singular = newvalue;
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

	
	public Set<String> getcategory() {
		//http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
		//log.trace("called method getcategory");
		System.out.println("called getcatgory");
		System.out.println("returning category "+category);
		return category;
	}

	
	
	public void setcategory(String newvalue) throws NullPointerException {
		log.trace("called method setcategory with argument "+newvalue);
		if (newvalue == null) {
			log.error("cannot set category with null data");
			throw new NullPointerException("setting category with a null string");
		}
		this.category = new HashSet<String>(Arrays.asList(newvalue
				.split(PropertiesLoader.getProperty("multivalue_separator"))));

	}

	
	public String toString() {

		return " - ID: " + id 
					+ " - singular: " + this.singular 
					+ " - link ID: " + this.linkId.toString() 
					+ " - note: " + this.notes 
					+ " - section: "+ this.section.toString() 
					+ " - audio size" + this.audio.length;
	}


}
