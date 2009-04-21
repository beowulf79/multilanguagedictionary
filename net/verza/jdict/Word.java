package net.verza.jdict;


import java.awt.Graphics;

import java.io.Serializable;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.verza.jdict.gui.JTableAudioButtonEditor;
import net.verza.jdict.gui.JTableAudioButtonRenderer;
import net.verza.jdict.gui.JTableButtonRenderer;
import net.verza.jdict.gui.JTableWordButtonEditor;
import net.verza.jdict.properties.PropertiesLoader;
import org.apache.log4j.Logger;



public class Word extends DictionaryObject implements Serializable, IWord  {

	static final long serialVersionUID = 8834844282811448538L;
	private static Logger log;
	protected String singular;
	protected Set<String> category;
	
	
	public Word() {
		super();
		log = Logger.getLogger("net.verza.jdict");
		log.trace("class " + this.getClass().getName() + " initialized");
		singular = new String();
		category = new HashSet<String>();
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

	public Set<String> getcategory() {
		//http://www.bughost.com/defecttracker/DefectEditForm.asp?DefectID=7&ListType=9&Page=1
		//log.trace("called method getcategory");
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
		
		return 		super.toString()
					+ " - singular: " + this.singular;
				//	+ " - category: " + this.category.toString();
				
	}
	
	
    public void paintComponent( Graphics g )	{
    	System.out.println("Graphics "+g.toString());
            g.setColor(new Color(255, 0, 255));
            g.drawLine(50, 50, 100, 50);
    }
    
    
    public void toGraphich() {
    	super.toGraphich();
		
		// Display Singular
		c.gridx = 0;
		c.gridy = 4;
		JLabel jlbl1 = new JLabel("Singular");
		jpnl.add(jlbl1, c);
		c.gridx = 1;
		JTextField jtxf1 = new JTextField(this.getsingular());
			
		jtxf1.setColumns(10);
		jtxf1.setEditable(false);
		jpnl.add(jtxf1, c);

		// Display Category
		c.gridx = 0;
		c.gridy = 5;
		JLabel jlbl2 = new JLabel("Category");
		jpnl.add(jlbl2, c);
		c.gridx = 1;
		JTextField jtxf2 = new JTextField("not defined for this word");
		jtxf2.setColumns(12);
		jtxf2.setEditable(false);
		if (this.getcategory().size() > 0)	{
			jtxf2.setText(this.getcategory().toString());
		}
		jpnl.add(jtxf2, c);

		jFrame.setResizable(false);
		jFrame.getContentPane().add(jpnl);
		jFrame.pack();
		jFrame.setVisible(true);
		jFrame.setSize(300, 350);
		
    }
    
    
    public JTable getTable()	{
    	super.getTable();
    	Object dataValues[][];
    	dataValues = new Object[1][3];
    	String headers[] = { "Obj", "Sing", "Audio" };
    	dataValues[0][0] = this;
    	dataValues[0][1] = this.getsingular();
    	dataValues[0][2] = this.getaudio();
    	DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(dataValues, headers);
		JTable table = new JTable(dm);
		table.getColumn("Obj").setCellRenderer(
				new JTableButtonRenderer("Obj"));
		table.getColumn("Obj").setCellEditor(
			new JTableWordButtonEditor(new JCheckBox()));
		table.getColumn("Audio").setCellRenderer(
				new JTableAudioButtonRenderer("Play Audio"));
		table.getColumn("Audio").setCellEditor(
				new JTableAudioButtonEditor(new JCheckBox()));
		return table;
		
    }



}
