package net.verza.jdict.gui;

import net.verza.jdict.quiz.*;
import java.awt.GridLayout;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Enumeration;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.apache.log4j.Logger;

/**
 * @author ChristianVerdelli
 * 
 * JTable utilizzata per visualizzare le statistiche di un utente
 * 
 */

public class UserStatsGuiJTable extends JPanel  {

	private static final long serialVersionUID = 1L;

	private static Logger log;

	private JScrollPane scroll;
	private Object dataValues[][];
	private static String headers[] = { "Question", "Counter", "Quiz Type", "Result" };

	public TableRowSorter<DefaultTableModel> sorter;



	/*
	 * il primo parametro corrisponde ai dati processati da QuisStats il secondo
	 * parametro viene utilizzato per impostare il colore della tabella
	 */
	public UserStatsGuiJTable(Hashtable<QuizResult, Integer> correct,
								Hashtable<QuizResult, Integer> wrong, 
									Color color) {
		
		super(new GridLayout(1, 0));

		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("Initialazing UserStatsGuiJTable class");

		CreateData(correct,wrong);
		CreateTable(color);
		add(scroll);
	}

	// Crea la tabella ed inserisce l'intestazione
	private void CreateTable(Color color) {
		
		DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(dataValues, headers);
		final JTable table = new JTable(dm);
		ColorRenderer cr=new ColorRenderer("Result");
		// color the first column only
		table.getColumnModel().getColumn(0).setCellRenderer(cr);
		table.setAutoCreateRowSorter(true);
		sorter = new TableRowSorter<DefaultTableModel>(dm);
		table.setRowSorter(sorter);
		scroll = new JScrollPane(table);

	}


	// Popola la tabella con i dati quando l'oggetto ResultTable viene
	// instanziato passando dei dati
	private void CreateData(Hashtable<QuizResult, Integer> data, Hashtable<QuizResult, Integer> data2) {
		int length = data.size() + data2.size();
		dataValues = new Object[length][headers.length];

		log.trace("couples received " + length);
		int counter = 0;
		for (Enumeration<QuizResult> e = data.keys(); e.hasMoreElements();) {
			QuizResult key = (QuizResult) e.nextElement();
			log.debug("reading data in the hash table using key " + key);
			try {
				// if the question is audio take the answer and the question
				if(key.getQuestion() instanceof byte[]) dataValues[counter][0] = key.getCorrectAnswer();
				else dataValues[counter][0] = key.getQuestion();
				
				dataValues[counter][1] = new Integer((Integer) data.get(key));
				dataValues[counter][2] = new String(key.getQuizType()
						.getBytes(), "UTF-8");
				dataValues[counter][3] = new String("correct");
				counter++;
			} catch (UnsupportedEncodingException exc) {
				System.out.println("Exception : " + exc.getMessage());
			}
		}
		
		
		for (Enumeration<QuizResult> e = data2.keys(); e.hasMoreElements();) {
			QuizResult key = (QuizResult) e.nextElement();
			log.debug("reading data in the hash table using key " + key);
			try {
				// if the question is audio take the answer and the question
				if(key.getQuestion() instanceof byte[]) dataValues[counter][0] = key.getCorrectAnswer();
				else dataValues[counter][0] = key.getQuestion();
				
				dataValues[counter][1] = new Integer((Integer) data2.get(key));
				dataValues[counter][2] = new String(key.getQuizType()
						.getBytes(), "UTF-8");
				dataValues[counter][3] = new String("wrong");
						
				
				
				counter++;
			} catch (UnsupportedEncodingException exc) {
				System.out.println("Exception : " + exc.getMessage());
			}
		}
		
		

	}
	
	

}
