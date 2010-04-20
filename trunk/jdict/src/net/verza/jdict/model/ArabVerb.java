package net.verza.jdict.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.gui.GUIPreferences;
import net.verza.jdict.gui.JTableAudioButtonEditor;
import net.verza.jdict.gui.JTableAudioButtonRenderer;
import net.verza.jdict.gui.JTableButtonRenderer;
import net.verza.jdict.gui.JTableWordButtonEditor;
import net.verza.jdict.verbs.arabic.ArabVerbPast;
import net.verza.jdict.verbs.arabic.ArabVerbPresent;

import org.apache.log4j.Logger;

public class ArabVerb extends Verb implements Serializable {

    private static final long serialVersionUID = -5752057185882277357L;
    private String past;
    private String paradigm;
    private String imperative;
    private String pronounce;
    private static Logger log;

    // private ArabVerbPast pastObject;
    // private ArabVerbPresent presentObject;

    public ArabVerb() {
	super();
	log = Logger.getLogger("jdict");
	log.trace("class " + this.getClass().getName() + " initialized");
    }

    public String getpast() {
	log.trace("called function getpast");
	return this.past;
    }

    public String getparadigm() {
	log.trace("called function getparadigm");
	return this.paradigm;
    }

    public void setpast(String _past) {
	log.trace("called function setpast " + _past);
	try {
	    log.debug("setting past to " + new String(_past.getBytes("UTF-8")));
	    this.past = _past;
	} catch (UnsupportedEncodingException e) {
	    log.error("UnsupportedEncodingException " + e.getMessage());
	    e.printStackTrace();
	}
    }

    public void setparadigm(String _paradigm) {
	log.trace("called function setparadigm with arg" + _paradigm);
	this.paradigm = _paradigm;
    }

    public void pastToString() {
	log.trace("called function pastToString");
	try {
	    new ArabVerbPast(this.past.toCharArray(), this.paradigm).toString();
	} catch (DataNotFoundException e) {
	    log.error("UnsupportedEncodingException " + e.getMessage());
	    e.printStackTrace();
	}
    }

    public void presentToString() {
	log.trace("called function presentToString");
	try {
	    new ArabVerbPresent(this.infinitive.toCharArray(), this.paradigm)
		    .toString();
	} catch (DataNotFoundException e) {
	    log.error("DataNotFoundException " + e.getMessage());
	    e.printStackTrace();
	}

    }

    public String toString() {
	log.trace("called function toString");
	// System.out.println("---------------------------------------------------------------------");
	// System.out.println("------------ PAST VERB CONJUGATION
	// -------------");
	// if (this.past != null) this.pastToString();

	// System.out.println("---------------------------------------------------------------------");
	// System.out.println("------------ PRESENT VERB CONJUGATION
	// -------------");
	// if (this.infinitive != null ) this.presentToString();

	String toReturn = null;
	// try {

	toReturn = " - ID: " + id
		+ " - infinitive: "
		+ infinitive // new String(this.infinitive.getBytes("UTF-8"))
		+ " - past "
		+ past// new String(this.past.getBytes("UTF-8"))
		+ " - link ID: " + linkId.toString() + " - note: " + this.notes
		+ " - section: " + this.section.toString() + " - paradigm  "
		+ this.paradigm + " - audio size  " + audio.length;

	// }catch(UnsupportedEncodingException e) {
	// System.err.println(e.getMessage());
	// e.printStackTrace();
	// }

	return toReturn;
    }

    public void toGraphich() {
	log.trace("called function toGraphich");
	super.toGraphich();

	// Display Past
	c.gridx = 0;
	c.gridy = 6;
	JLabel jlbl1 = new JLabel("Past");
	jlbl1.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(jlbl1, c);
	c.gridx = 1;
	JTextField jtxf1 = new JTextField(this.getpast());
	jtxf1.setColumns(10);
	jtxf1.setEditable(false);
	jpnl.add(jtxf1, c);

	jFrame.getContentPane().add(jpnl);
	jFrame.pack();
	jFrame.setVisible(true);

    }

    public JTable getTable() {
	log.trace("called function getTable");

	// super.getTable();
	Object dataValues[][];
	dataValues = new Object[1][4];
	String headers[] = { "Obj", "Infinitive", "Past", "Audio" };
	dataValues[0][0] = this;
	dataValues[0][1] = this.getinfinitive();
	dataValues[0][2] = this.getpast();
	dataValues[0][3] = this.getaudio();
	DefaultTableModel dm = new DefaultTableModel();
	dm.setDataVector(dataValues, headers);
	JTable table = new JTable(dm);
	table.getColumn("Obj").setCellRenderer(new JTableButtonRenderer("Obj"));
	table.getColumn("Obj").setCellEditor(
		new JTableWordButtonEditor(new JCheckBox()));
	table.getColumn("Audio").setCellRenderer(
		new JTableAudioButtonRenderer("Play Audio"));
	table.getColumn("Audio").setCellEditor(
		new JTableAudioButtonEditor(new JCheckBox()));
	return table;

    }

    public String getimperative() {
	log.trace("called function getimperative");
	return imperative;
    }

    public void setimperative(String imperative) {
	log.trace("called function setimperative with arg " + imperative);
	this.imperative = imperative;
    }

    public String getpronounce() {
	log.trace("called function getpronounce");
	return pronounce;
    }

    public void setpronounce(String pronounce) {
	log.trace("called function setpronounce with arg " + pronounce);
	this.pronounce = pronounce;
    }

}