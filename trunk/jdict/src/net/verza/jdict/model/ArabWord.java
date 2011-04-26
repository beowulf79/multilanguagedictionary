package net.verza.jdict.model;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.verza.jdict.gui.GUIPreferences;
import net.verza.jdict.gui.JTableAudioButtonEditor;
import net.verza.jdict.gui.JTableAudioButtonRenderer;
import net.verza.jdict.gui.JTableButtonRenderer;
import net.verza.jdict.gui.JTableWordButtonEditor;

import org.apache.log4j.Logger;

/**
 * @author Christian Verdelli
 * 
 */
public class ArabWord extends Word {

    private String plural;
    private byte[] audio_plural;
    private String transliteration;
    private String diacritics;
    private static Logger log;
    static final long serialVersionUID = -5752057185882277357L;

    public ArabWord() {
	super();
	log = Logger.getLogger("jdict");
	log.trace("Class " + this.getClass().getName() + " initialized");
	plural = new String();
	audio_plural = new byte[0];
	transliteration = new String();
	diacritics = new String();

    }

    public void setplural(String _plural) {
	log.trace("called methos setplural with args " + _plural);
	this.plural = _plural;
    }

    public String getplural() {
	log.trace("called method getplular");
	return this.plural;
    }

    public String getdiacritics() {
	log.trace("called method getdiacristics");
	return diacritics;
    }

    public void setdiacritics(String _diacritics) {
	log.trace("called method setdiacristics with args " + _diacritics);
	this.diacritics = _diacritics;
    }

    public String gettransliteration() {
	log.trace("called method gettransliteration");
	return transliteration;
    }

    public void settransliteration(String _transliteration) {
	log.trace("called method settransliteration with args "
		+ _transliteration);
	this.transliteration = _transliteration;
    }

    public Object getaudioplural() {
	// http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
	log.info("called method getaudioplural, returning stream of size "
		+ this.audio_plural.length);
	return audio_plural;
    }

    public byte[] getaudiobyteplural() {
	log.trace("called method getaudiobyteplural, returning stream of size "
		+ this.audio_plural.length);
	return this.audio_plural;
    }

    public void setaudioplural(byte[] newvalue) {
	log.trace("called method setaudioplural with value " + newvalue);
	if (newvalue == null) {
	    log.error("setting setaudioplural with a null byte array");
	    return;
	}
	this.audio_plural = newvalue;
    }

    public String toString() {

	return super.toString().concat(
		" plurale " + this.plural + " diacritics " + this.diacritics
			+ " transliteration " + this.transliteration);
	// + " audio plural size " + this.audio_plural.length);
    }

    public void toGraphich() {
	super.toGraphich();

	// Display Plural
	c.gridx = 0;
	c.gridy = 6;
	JLabel jlbl1 = new JLabel("Plural");
	jlbl1.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(jlbl1, c);
	c.gridx = 1;
	JTextField jtxf1 = new JTextField(this.getplural());
	jtxf1.setColumns(10);
	jtxf1.setEditable(false);
	jpnl.add(jtxf1, c);

	// Display Audio Stream Size
	c.gridx = 0;
	c.gridy = 7;
	JLabel audioSizeJLabel = new JLabel("Audio Plural Stream Size");
	audioSizeJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(audioSizeJLabel, c);
	c.gridx = 1;
	JTextField jtxf2 = new JTextField("0 bytes");
	if (this.getaudioplural() != null) {
	    byte stream[] = (byte[]) this.getaudioplural();
	    jtxf2.setText(new Integer(stream.length).toString() + " bytes");
	}
	jtxf2.setEditable(false);
	jtxf2.setColumns(10);
	jpnl.add(jtxf2, c);

	// Display Diacritics
	c.gridx = 0;
	c.gridy = 8;
	JLabel jlbl2 = new JLabel("Diacritics");
	jlbl2.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(jlbl2, c);
	c.gridx = 1;
	JTextField jtxf3 = new JTextField(this.getdiacritics());
	jtxf3.setColumns(10);
	jtxf3.setEditable(false);
	jpnl.add(jtxf3, c);

	// Display Transliteration
	c.gridx = 0;
	c.gridy = 9;
	JLabel jlbl3 = new JLabel("Transliteration");
	jlbl3.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(jlbl3, c);
	c.gridx = 1;
	JTextField jtxf4 = new JTextField(this.gettransliteration());
	jtxf4.setColumns(10);
	jtxf4.setEditable(false);
	jpnl.add(jtxf4, c);

	jFrame.getContentPane().add(jpnl);
	jFrame.pack();
	jFrame.setVisible(true);

    }

    public JTable getTable() {
	// super.getTable();
	Object dataValues[][];
	dataValues = new Object[1][4];
	String headers[] = { "Obj", "Sing", "Plur", "Audio" };
	dataValues[0][0] = this;
	dataValues[0][1] = this.getsingular();
	dataValues[0][2] = this.getplural();
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

}
