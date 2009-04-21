
package net.verza.jdict;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.gui.JTableAudioButtonEditor;
import net.verza.jdict.gui.JTableAudioButtonRenderer;
import net.verza.jdict.gui.JTableButtonRenderer;
import net.verza.jdict.gui.JTableWordButtonEditor;
import net.verza.jdict.verbs.ArabVerbPast;
import net.verza.jdict.verbs.ArabVerbPresent;


public class ArabVerb extends Verb  implements Serializable {

	private static final long serialVersionUID = -5752057185882277357L;
	private String past;
	private String paradigm;
	private static Logger log;
	//private ArabVerbPast pastObject;
	//private ArabVerbPresent presentObject;
	
	
	public ArabVerb() {
		super();
		log = Logger.getLogger("net.verza.jdict");
		log.trace("class " + this.getClass().getName() + " initialized");
	}

	
	public String getpast()		{
		return this.past;
	}
	

	public String getparadigm()		{
		return this.paradigm;
	}
	
	
	public void setpast(String _past) {
		try {
			System.out.println("setting past to " + new String(_past.getBytes("UTF-8")) );
			this.past = _past;
		
		} catch (UnsupportedEncodingException e) {
			e.	printStackTrace();
		}
	}
	
	
	public void setparadigm(String _paradigm) {
		this.paradigm = _paradigm;
	}
	
	
	public void pastToString()	 {
		try {
			
			new ArabVerbPast(this.past.toCharArray(), this.paradigm).toString();

		} catch(DataNotFoundException e)		{
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}
	
	
	public void presentToString()	{
		try {
			
			new ArabVerbPresent(this.infinitive.toCharArray(), this.paradigm).toString();

		} catch(DataNotFoundException e)		{
			System.err.println(e.toString());
			e.printStackTrace();
		}
		
	}
	
		
	public String toString() {
		

		System.out.println("---------------------------------------------------------------------");
		System.out.println("------------   PAST VERB CONJUGATION   -------------");
		//if (this.past != null) this.pastToString();
		
		
		System.out.println("---------------------------------------------------------------------");
		System.out.println("------------   PRESENT VERB  CONJUGATION  -------------");
		//if (this.infinitive !=  null ) this.presentToString(); 

		
		String toReturn = null;
		//try {

			toReturn =  
			" - ID: " + id  
			+" - infinitive: " +infinitive //new String(this.infinitive.getBytes("UTF-8")) 
			+ " - past "+ past//new String(this.past.getBytes("UTF-8"))
			+ " - link ID: " + linkId.toString() 
			+ " - note: " + this.notes 
			+ " - section: " + this.section.toString()
			+ " - paradigm  " + this.paradigm
			+ " - audio size  " + audio.length
			 ;

	//}catch(UnsupportedEncodingException e)	{
		//	System.err.println(e.getMessage());
	//		e.printStackTrace();
//	}

		return toReturn;
	}

	
    public void toGraphich() {
    	super.toGraphich();
		
		
		// Display Past
		c.gridx = 0;
		c.gridy = 6;
		JLabel jlbl1 = new JLabel("Past");
		jpnl.add(jlbl1, c);
		c.gridx = 1;
		JTextField jtxf1 = new JTextField(this.getpast());
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
    	String headers[] = { "Obj", "Infinitive", "Past" , "Audio" };
    	dataValues[0][0] = this;
    	dataValues[0][1] = this.getinfinitive();
    	dataValues[0][2] = this.getpast();
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