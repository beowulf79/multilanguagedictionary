package net.verza.jdict;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.verza.jdict.gui.GUIPreferences;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import net.verza.jdict.properties.PropertiesLoader;
import net.verza.jdict.utils.Utility;
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

	protected byte[] audio;

	JFrame jFrame;
	JPanel jpnl;
	GridBagConstraints c;

	public DictionaryObject() {

		log = Logger.getLogger("net.verza.jdict");
		log.trace("class " + this.getClass().getName() + " initialized");
		linkId = new HashMap<String, Integer[]>();
		notes = new String();
		section = new HashSet<String>();
		audio = new byte[0];
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
		if (!this.getlinkid().containsKey(language))
			System.out.println("linkedid not present for the language "
					+ language);

		System.out.println("");
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

		String[] ids = newvalue.split(PropertiesLoader
				.getProperty("multivalue_separator"));

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
		System.out.println("called method getaudio, returning stream of size "
				+ this.audio.length);
		return audio;
	}

	public void setaudio(byte[] newvalue) throws NullPointerException {
		if (newvalue == null)
			throw new NullPointerException("setting audio with a null string");
		log.trace("setting audio having size " + newvalue.length);
		System.out.println("called method setaudio with byte size of "
				+ newvalue.length);
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

	public JTable getTable() {
		return new JTable();
	}

	public void toGraphich() {
		jFrame = new JFrame();
		
		jpnl = new JPanel(new BorderLayout());
		jpnl.setBackground(GUIPreferences.backgroundColor);
		jpnl.setLayout(new GridBagLayout());
		jpnl.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
		jFrame.getContentPane().add(BorderLayout.CENTER, jpnl);
		c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);// add some space between components to avoid clutter
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
		JLabel linkedIdJLabel = new JLabel("Linked IDs");
		linkedIdJLabel.setBorder(BorderFactory.createLineBorder(
				GUIPreferences.borderColor, GUIPreferences.borderThickness));
		jpnl.add(linkedIdJLabel, c);
		c.gridx = 1;
		JTextField jtxf2 = new JTextField("0 bytes");
		if (this.getlinkid() != null) {
			jtxf2.setText(this.getlinkid().toString());
		}
		jtxf2.setEditable(false);
		jtxf2.setColumns(10);
		jpnl.add(jtxf2, c);

		// Display Section 
		c.gridx = 0;
		c.gridy = 2;
		JLabel sectionJLabel = new JLabel("Section");
		sectionJLabel.setBorder(BorderFactory.createLineBorder(
				GUIPreferences.borderColor, GUIPreferences.borderThickness));
		jpnl.add(sectionJLabel, c);
		c.gridx = 1;
		JTextField jtxf3 = new JTextField("not defined for this word");
		jtxf3.setColumns(12);
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
		jFrame.setSize(500, 350);

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

}
