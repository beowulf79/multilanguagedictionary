package net.verza.jdict.model;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.verza.jdict.gui.GUIPreferences;
import net.verza.jdict.properties.PropertiesLoader;
import net.verza.jdict.utils.Utility;

import org.apache.log4j.Logger;

public class DictionaryObject implements Serializable, SearchableObject {

    private static final long serialVersionUID = 8834844282811448538L;
    protected Integer id;
    protected Map<String, Integer[]> linkId;
    private static Logger log;
    // private Integer[] sinonymous_id;
    // private Integer[] contrarious_id;
    protected String notes;
    protected Set<String> section;

    protected byte[] audio;

    JFrame jFrame;
    JPanel jpnl;
    GridBagConstraints c;

    public DictionaryObject() {

	log = Logger.getLogger("jdict");
	log.trace("class " + this.getClass().getName() + " initialized");
	linkId = new HashMap<String, Integer[]>();
	notes = new String();
	section = new HashSet<String>();
	audio = new byte[0];
    }

    public Integer getid() {
	log.trace("called method getid");
	if (this.id == null) {
	    // http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
	    log.error("returning null id");
	    return null;
	}
	return id;
    }

    public void setid(Integer newvalue) {
	log.trace("called method setid with argument " + newvalue);
	if (newvalue == null) {
	    log.error("id cannot be null");
	    throw new NullPointerException("setting id with a null string");
	}
	this.id = newvalue;
    }

    public Integer[] getlinkid(String language) {
	log.trace("called method getlinkid with argument " + language);
	if (this.linkId == null) {
	    // http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
	    log.error("returning null linkid");
	    return null;
	}
	if (!this.getlinkid().containsKey(language))
	    log.warn("linkedid not present for the language " + language);

	return linkId.get(language);
    }

    public Map<String, Integer[]> getlinkid() {
	log.trace("called method getlinkid");
	if (this.linkId == null) {
	    // http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
	    log.error("returning null linkid map");
	    return null;
	}
	return this.linkId;
    }

    public void setlinkid(String language, String newvalue) {
	log.trace("called method setlinkid with language " + language
		+ " and value  " + newvalue);
	if (newvalue == null)
	    throw new NullPointerException("setting linkid with a null string");

	String[] ids = newvalue.split(PropertiesLoader
		.getProperty("multivalue_separator"));

	Integer[] ids_integer = Utility.StringAR2IntgerAR(ids);
	this.linkId.put(language, ids_integer);
    }

    public void addlinkid(String language, Integer _id) {
	log.trace("called method addLinkId with language " + language
		+ " and id " + _id);

	Integer[] oldArray = linkId.get(language);
	if (oldArray == null) {
	    log
		    .error("old array empty/null; creating new array and put the id,language");
	    oldArray = new Integer[1];
	    oldArray[0] = _id;
	    linkId.put(language, oldArray);
	    return;
	}
	// create the new array one cell bigger then the old one
	int oldArrayLenght = linkId.get(language).length;
	log.debug("oldArrayLength " + oldArrayLenght);
	Integer[] newArray = new Integer[oldArrayLenght + 1];
	// first copy the old array into the new one
	System.arraycopy(oldArray, 0, newArray, 0, oldArrayLenght);
	// then add the new id at the end
	log.debug("newArrayLength " + newArray.length);
	newArray[newArray.length - 1] = _id;
	// remove the old array and put the new one
	linkId.remove(language);
	linkId.put(language, newArray);
    }

    public void removelinkid(String language, Integer _id) {
	log.trace("called method removelinkid with language " + language
		+ " and id " + _id);

	Integer[] oldArray = linkId.get(language);
	if (oldArray == null) {
	    log.error("old array empty/null");
	    return;
	}

	// if the array is just one cell delete it and return
	if (oldArray.length == 1) {
	    log.debug("link id array is just one cell large, set it to null");
	    linkId.remove(language);
	    return;
	}

	// create the new array one cell smaller then the old one
	int oldArrayLenght = linkId.get(language).length;
	Integer[] newArray = new Integer[oldArrayLenght - 1];
	// copy each element in the new array but the id to remove
	int newArrayIndex = 0;
	for (int i = 0; i < oldArrayLenght; i++) {
	    log.debug("oldArray[" + i + "],id to remove:" + oldArray[i] + "-"
		    + _id + "-");
	    if (oldArray[i].equals(_id)) {
		// if the array is just one cell delete it and return
		if (newArray.length == 1) {
		    log
			    .debug("link id array is just one cell large, set it to null");
		    linkId.remove(language);
		    return;
		}
		log.debug("got id to remove, skipping insert on new array");
		continue;
	    }
	    log.debug("newArray.size(),oldArray.size() " + newArray.length
		    + "-" + oldArray.length);
	    newArray[newArrayIndex] = oldArray[i];
	    newArrayIndex++;
	}

	// remove the old array and put the new one
	linkId.remove(language);
	linkId.put(language, newArray);
    }

    public String getnotes() {
	// http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
	// log.trace("called method getsingular");
	return notes;
    }

    public void setnotes(String newvalue) {
	if (newvalue == null)
	    throw new NullPointerException("setting notes with a null string");

	this.notes = newvalue;
    }

    public Object getaudio() {
	// http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
	log.info("called method getaudio, returning stream of size "
		+ this.audio.length);
	return audio;
    }

    public byte[] getaudiobyte() {
	log.trace("called method getaudio, returning stream of size "
		+ this.audio.length);
	return this.audio;
    }

    public void setaudio(byte[] newvalue) {
	log.trace("called method setaudio with value " + newvalue);
	if (newvalue == null) {
	    log.error("setting audio with a null string");
	    return;
	}
	this.audio = newvalue;
    }

    public Set<String> getsection() {
	// http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
	log.trace("called method getsection");
	return section;
    }

    public void setsection(String newvalue) {
	log.trace("called method setsection with argument " + newvalue);
	if (newvalue == null) {
	    throw new NullPointerException("setting section with a null string");
	}
	this.section = new HashSet<String>(Arrays.asList(newvalue
		.split(PropertiesLoader.getProperty("multivalue_separator"))));

    }

    public void addsection(String value) {
	log.trace("called method addsection with value" + value);
	if (value == null)
	    throw new NullPointerException("adding a null value to section");
	if (section == null) {
	    log.debug("section set is null, calling setsection");
	    setsection(value);
	    return;
	}
	section.add(value);
    }

    public void removesection(String value) {
	log.trace("called method removesection with value " + value);
	if (value == null)
	    throw new NullPointerException("remove a null value from section");
	section.remove(value);
    }

    public JTable getTable() {
	return new JTable();
    }

    public void toGraphich() {
	System.out.println("to Graphic called");
	jFrame = new JFrame();

	jpnl = new JPanel(new BorderLayout());
	jpnl.setBackground(GUIPreferences.backgroundColor);
	jpnl.setLayout(new GridBagLayout());
	jpnl.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
	jFrame.getContentPane().add(BorderLayout.CENTER, jpnl);
	c = new GridBagConstraints();
	c.insets = new Insets(2, 2, 2, 2);// add some space between components
	// to avoid clutter
	c.anchor = GridBagConstraints.WEST;// anchor all components WEST
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0;// all components use vertical available space
	c.weighty = 0.1;// all components use horizontal available space
	c.gridheight = 1;
	c.gridwidth = 1;

	// Display Audio Stream Size
	c.gridx = 0;
	c.gridy = 0;
	JLabel audioSizeJLabel = new JLabel("Audio Stream Size");
	audioSizeJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(audioSizeJLabel, c);
	c.gridx = 1;
	JTextField jtxf1 = new JTextField("0 bytes");
	if (this.getaudio() != null) {
	    byte stream[] = (byte[]) this.getaudio();
	    jtxf1.setText(new Integer(stream.length).toString() + " bytes");
	}
	jtxf1.setEditable(false);
	jtxf1.setColumns(10);
	jpnl.add(jtxf1, c);

	// Display LinkID
	c.gridx = 0;
	c.gridy = 1;
	this.linkIdToString();
	JLabel linkedIdJLabel = new JLabel("Linked IDs");
	linkedIdJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(linkedIdJLabel, c);
	c.gridx = 1;
	JTextArea linkidTextArea = new JTextArea();
	if (this.getlinkid() != null) {
	    linkidTextArea.setRows(linkId.keySet().size());
	    linkidTextArea.setText(linkIdToString());
	}
	linkidTextArea.setEditable(false);
	linkidTextArea.setColumns(10);
	jpnl.add(linkidTextArea, c);

	// Display Section
	c.gridx = 0;
	c.gridy = 2;
	JLabel sectionJLabel = new JLabel("Section");
	sectionJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(sectionJLabel, c);
	c.gridx = 1;
	JTextField jtxf3 = new JTextField("");
	jtxf3.setColumns(10);
	jtxf3.setEditable(false);
	if (this.getsection().size() > 0) {
	    jtxf3.setText(this.getsection().toString());
	}
	jpnl.add(jtxf3, c);

	// Display Notes
	c.gridx = 0;
	c.gridy = 3;
	JLabel notesJLabel = new JLabel("Notes");
	notesJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(notesJLabel, c);
	c.gridx = 1;
	JTextField jtxf4 = new JTextField("not defined for this word");
	jtxf4.setEditable(false);
	jtxf4.setColumns(12);
	if (this.getnotes() != null)
	    jtxf4.setText(this.getnotes());
	jpnl.add(jtxf4, c);

	jFrame.setBounds(5, 111, 252, 140);
	jFrame.setResizable(true);
	jFrame.getContentPane().add(jpnl);
	jFrame.pack();
	jFrame.setVisible(false);
	jFrame.setSize(500, 400);

    }

    public Boolean equals(SearchableObject target, String language) {

	System.out.println("comparing with the word " + target.toString()
		+ " using language " + language + " with this word "
		+ this.getid());
	Boolean returnCode = false;
	Integer[] ids = target.getlinkid(language);
	for (int i = 0; i < ids.length; i++) {
	    System.out.println("IDS[i]:" + ids[i] + " this.getid():"
		    + this.getid());
	    if (ids[i].equals(this.getid())) {
		System.out.println("SearchableObject uguale");
		returnCode = true;
		break;
	    }
	}
	return returnCode;
    }

    public String toString() {

	return " - ID: " + id + " - link ID: " + this.linkId.toString()
		+ " - note: " + this.notes + " - section: "
		+ this.section.toString() + " - audio size" + this.audio.length;
    }

    public String linkIdToString() {
	String linkIdToString = new String();
	Iterator<String> it = this.linkId.keySet().iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    Integer[] values = this.linkId.get(key);
	    String value = new String();
	    for (int i = 0; i < values.length; i++) {
		System.out.println("values[i] " + values[i].toString());
		value = value.concat(values[i].toString() + ",");
	    }
	    value = value.substring(0, (value.length()) - 1); // removes comma
	    // in the end
	    linkIdToString = linkIdToString.concat(key + "[" + value + "]"
		    + "\n");
	}

	System.out.println("linkIdToString " + linkIdToString);
	return linkIdToString;
    }
}
