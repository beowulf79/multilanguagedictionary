package net.verza.jdict.model;

import java.io.Serializable;
import java.util.HashSet;

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

public class Verb extends DictionaryObject implements Serializable, IVerb {

    static final long serialVersionUID = 8834844282811448538L;
    private static Logger log;
    protected String infinitive;

    public Verb() {

	super();
	log = Logger.getLogger("jdict");
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

    public String toString() {

	return super.toString() + "  - infinitive: " + infinitive;

    }

    public void toGraphich() {
	super.toGraphich();

	// Display Singular
	c.gridx = 0;
	c.gridy = 4;
	JLabel infinitiveJLabel = new JLabel("Infinitive");
	infinitiveJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(infinitiveJLabel, c);
	c.gridx = 1;
	JTextField jtxf1 = new JTextField(this.getinfinitive());

	jtxf1.setColumns(10);
	jtxf1.setEditable(false);
	jpnl.add(jtxf1, c);

	jFrame.getContentPane().add(jpnl);
	jFrame.pack();
	jFrame.setVisible(true);

    }

    public JTable getTable() {
	super.getTable();
	Object dataValues[][];
	dataValues = new Object[1][3];
	String headers[] = { "Obj", "Sing", "Audio" };
	dataValues[0][0] = this;
	dataValues[0][1] = this.getinfinitive();
	dataValues[0][2] = this.getaudio();

	DefaultTableModel dm = new DefaultTableModel();
	dm.setDataVector(dataValues, headers);
	JTable table = new JTable(dm);
	table.getColumnModel().getColumn(0).setPreferredWidth(200);
	table.getColumnModel().getColumn(1).setPreferredWidth(400);
	table.getColumnModel().getColumn(2).setPreferredWidth(200);
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
