package net.verza.jdict;



import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
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
	private static Logger log;
	static final long serialVersionUID = -5752057185882277357L;

	
	public ArabWord() {
		super();
		log = Logger.getLogger("net.verza.jdict");
		log.trace("Class " + this.getClass().getName() + " initialized");
	}

	
	public void setplural(String _plural)	{
		this.plural = _plural;
	}
	
	
	public String getplural()	{
		
		return this.plural;
	}


	public String toString() {

		return super.toString().concat(" plurale "+plural);
	}

	
    public void toGraphich() {
    	super.toGraphich();
		
		
		// Display Plural
		c.gridx = 0;
		c.gridy = 6;
		JLabel jlbl1 = new JLabel("Plural");
		jpnl.add(jlbl1, c);
		c.gridx = 1;
		JTextField jtxf1 = new JTextField(this.getplural());
		jtxf1.setColumns(10);
		jtxf1.setEditable(false);
		jpnl.add(jtxf1, c);
	
		jFrame.setResizable(false);
		jFrame.getContentPane().add(jpnl);
		jFrame.pack();
		jFrame.setVisible(true);
		jFrame.setSize(300, 350);
		
    }
    
	
	public JTable getTable()	{
    	//super.getTable();
    	Object dataValues[][];
    	dataValues = new Object[1][4];
    	String headers[] = { "Obj", "Sing", "Plur" , "Audio" };
    	dataValues[0][0] = this;
    	dataValues[0][1] = this.getsingular();
    	dataValues[0][2] = this.getplural();
    	dataValues[0][3] = this.getaudio();
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
