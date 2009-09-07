package net.verza.jdict.gui;

import net.verza.jdict.quiz.*;
import java.awt.GridLayout;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.HashMap;
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

public class UserStatsGuiTable extends JPanel {

	private static final long serialVersionUID = 1L;

	private static Logger log;

	private JScrollPane scroll;
	private Object dataValues[][];
	private static String headers[] = { "Question", "Counter", "Quiz Type",
			"Result" };

	public TableRowSorter<DefaultTableModel> sorter;

	/*
	 * il primo parametro corrisponde ai dati processati da QuisStats
	 */
	public UserStatsGuiTable(HashMap<QuizResult, Integer> correct) {

		super(new GridLayout(1, 0));
		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("Initialazing UserStatsGuiJTable class");
		CreateData(correct);
		CreateTable();
		add(scroll);
	}

	private void CreateTable() {

		DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(dataValues, headers);
		final JTable table = new JTable(dm);
		ColorRenderer cr = new ColorRenderer("Result");
		table.getColumnModel().getColumn(0).setCellRenderer(cr);// color the first column only
		table.getColumnModel().getColumn(0).setPreferredWidth(120);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.setAutoCreateRowSorter(true);
		sorter = new TableRowSorter<DefaultTableModel>(dm);
		table.setRowSorter(sorter);
		scroll = new JScrollPane(table);

	}

	private void CreateData(HashMap<QuizResult, Integer> data) {
		dataValues = new Object[data.size()][headers.length];

		int counter = 0;
		Iterator<QuizResult> itr = data.keySet().iterator();
		while (itr.hasNext()) {
			
			QuizResult key = (QuizResult) itr.next();			
			log.debug("reading data in the hash table using key " + key);
			try {
				// if the question is audio take the answer and the question
				if (key.getQuestion() instanceof byte[])
					dataValues[counter][0] = key.getCorrectAnswer();
				else
					dataValues[counter][0] = key.getQuestion();

				dataValues[counter][1] = new Integer((Integer) data.get(key));
				dataValues[counter][2] = new String(key.getQuizType()
						.getBytes(), "UTF-8");

				if ("1".equals(key.getQuizExitCode()))
					dataValues[counter][3] = "correct";
				else
					dataValues[counter][3] = "wrong";
				counter++;

			} catch (UnsupportedEncodingException exc) {
				System.out.println("Exception : " + exc.getMessage());
			}
		}

	}

}
