package net.verza.jdict.gui;

import java.awt.GridLayout;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Enumeration;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 * @author ChristianVerdelli
 * 
 * JTable utilizzata per visualizzare le statistiche di un utente
 * 
 */

public class UserStatsGuiJTable extends JPanel {

	private static final long serialVersionUID = 1L;

	private static Logger log;

	private JScrollPane scroll;
	private Object dataValues[][];
	private static String headers[] = { "Quiz ExitCode", "Correct Answer" };

	/*
	 * il primo parametro corrisponde ai dati processati da QuisStats il secondo
	 * parametro viene utilizzato per impostare il colore della tabella
	 */
	public UserStatsGuiJTable(Hashtable<String, Integer> data, Color color) {
		super(new GridLayout(1, 0));

		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("Initialazing UserStatsGuiJTable class");

		CreateData(data);
		CreateTable(color);
		add(scroll);
	}

	// Crea la tabella ed inserisce l'intestazione
	private void CreateTable(Color color) {
		DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(dataValues, headers);
		JTable table = new JTable(dm);

		table.setBackground(color);
		scroll = new JScrollPane(table);

	}

	// Popola la tabella con i dati quando l'oggetto ResultTable viene
	// instanziato passando dei dati
	private void CreateData(Hashtable<String, Integer> data) {
		int length = data.size();
		dataValues = new Object[length][headers.length];

		log.trace("couples received " + length);
		int counter = 0;
		for (Enumeration<String> e = data.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			log.debug("reading data in the hash table using key " + key);
			try {
				dataValues[counter][0] = new String(key.getBytes(), "UTF-8");
				dataValues[counter][1] = new Integer((Integer) data.get(key));
				counter++;
			} catch (UnsupportedEncodingException exc) {
				System.out.println("Exception : " + exc.getMessage());
			}
		}

	}

}
