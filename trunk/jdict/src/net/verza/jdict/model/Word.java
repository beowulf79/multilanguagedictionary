package net.verza.jdict.model;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
import net.verza.jdict.properties.PropertiesLoader;

import org.apache.log4j.Logger;

public class Word extends DictionaryObject implements Serializable, IWord {

    static final long serialVersionUID = 8834844282811448538L;
    private static Logger log;
    protected String singular;
    protected byte[] audiosingular;
    protected Set<String> category;

    public Word() {
	super();
	log = Logger.getLogger("jdict");
	log.trace("class " + this.getClass().getName() + " initialized");
	singular = new String();
	category = new HashSet<String>();
	audiosingular = new byte[0];
    }

    public String getsingular() {
	return this.singular;
    }

    public void setsingular(String newvalue) {
	if (newvalue == null)
	    throw new NullPointerException(
		    "setting Infinitive with a null string");
	this.singular = newvalue;
    }

    public Object getaudiosingular() {
    	// http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
    	log.info("called method getaudio, returning stream of size "
    		+ this.audiosingular.length);
    	return audiosingular;
        }

        public byte[] getaudiosingularbyte() {
    	log.trace("called method getaudio, returning stream of size "
    		+ this.audiosingular.length);
    	return this.audiosingular;
        }

        public void setaudiosingular(byte[] newvalue) {
    	log.trace("called method setaudio with byte array " + newvalue);
    	if (newvalue == null) {
    	    log.error("setting audio with a null string");
    	    return;
    	}
    	this.audiosingular = newvalue;
        }
    
    public Set<String> getcategory() {
	// http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
	// log.trace("called method getcategory");
	return category;
    }

    public void setcategory(String newvalue) {
	log.trace("called method setcategory with argument " + newvalue);
	if (newvalue == null) {
	    log.error("cannot set category with null data");
	    throw new NullPointerException(
		    "setting category with a null string");
	}
	this.category = new HashSet<String>(Arrays.asList(newvalue
		.split(PropertiesLoader.getProperty("multivalue_separator"))));

    }

    public void addcategory(String value) {
	log.trace("called method addcategory with value" + value);
	if (value == null)
	    throw new NullPointerException("adding a null value to category");
	if (category == null) {
	    log.debug("category set is null, calling setcategory");
	    setcategory(value);
	    return;
	}
	category.add(value);
    }

    public void removecategory(String value) {
	log.trace("called method removecategory with value " + value);
	if (value == null)
	    throw new NullPointerException("remove a null value from category");
	category.remove(value);
    }

    public String toString() {

	return super.toString() + " - singular: " + this.singular;
	// + " - category: " + this.category.toString();

    }

    public void paintComponent(Graphics g) {
	System.out.println("Graphics " + g.toString());
	g.setColor(new Color(255, 0, 255));
	g.drawLine(50, 50, 100, 50);
    }

    public void toGraphich() {
	super.toGraphich();

	// Display Singular
	c.gridx = 0;
	c.gridy = 4;
	JLabel singularJLabel = new JLabel("Singular");
	singularJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(singularJLabel, c);
	c.gridx = 1;
	JTextField jtxf1 = new JTextField(this.getsingular());
	jtxf1.setColumns(10);
	jtxf1.setEditable(false);
	jpnl.add(jtxf1, c);

	// Display Category
	c.gridx = 0;
	c.gridy = 5;
	JLabel categoryJLabel = new JLabel("Category");
	categoryJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(categoryJLabel, c);
	c.gridx = 1;
	JTextField jtxf2 = new JTextField("not defined for this word");
	jtxf2.setColumns(10);
	jtxf2.setEditable(false);
	if (this.getcategory().size() > 0) {
	    jtxf2.setText(this.getcategory().toString());
	}

	jpnl.add(jtxf2, c);

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
	dataValues[0][1] = this.getsingular();
	dataValues[0][2] = this.getaudiosingular();
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
