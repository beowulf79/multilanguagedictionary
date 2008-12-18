package net.verza.jdict.gui;

/**
 * @author ChristianVerdelli
 *
 */

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JCheckBox;
import net.verza.jdict.Word;
import net.verza.jdict.IWord;

import org.apache.log4j.Logger;
import java.awt.GridLayout;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class DictResultTable extends JPanel {
	private static final long serialVersionUID = 1L;

	private static Logger log;
	private JScrollPane scroll;
	private Object dataValues[][];
	private static String headers[] = { "Sing", "Plur", "Audio" };

	// Costruttore che riceve in ingresso un matrice di parole singolari
	// plurali in base ottenute dalla ricerca
	public DictResultTable(Vector<Word> data) {
		super(new GridLayout(1, 0));

		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().toString());

		CreateData(data);
		CreateTable();
		add(scroll);

	}

	// Crea la tabella ed inserisce l'intestazione
	private void CreateTable() {
		DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(dataValues, headers);
		JTable table = new JTable(dm);
		table.getColumn("Sing").setCellRenderer(
				new JTableButtonRenderer("singolare"));
		table.getColumn("Sing").setCellEditor(
				new JTableWordButtonEditor(new JCheckBox()));
		table.getColumn("Audio").setCellRenderer(
				new JTableAudioButtonRenderer("Play Audio"));
		table.getColumn("Audio").setCellEditor(
				new JTableAudioButtonEditor(new JCheckBox()));
		scroll = new JScrollPane(table);

	}

	// Popola la tabella con i dati quando l'oggetto ResultTable viene
	// instanziato passando dei dati
	private void CreateData(Vector<Word> data) {
		int length = data.size();
		log.debug("Word vector has size " + length);
		dataValues = new Object[length][headers.length];

		log.trace("looping through the received words database");
		for (int iY = 0; iY < length; iY++) {
			try {
				IWord w = (IWord) data.get(iY);
				log.debug("loop " + iY + " word ID " + w.getid());

				dataValues[iY][0] = (Object) w;
				
				if(w.getplural() != null)	{
					log.debug("found plural, filling the cell");
					dataValues[iY][1] = new String(w.getplural().getBytes(),
													"UTF-8");
				}
				
				if (w.getaudio() != null) {
					log.debug("found Audio, filling the cell as byte array "
							+ (Object) w.getaudio());
					dataValues[iY][2] = (Object) w.getaudio();
				}

			} catch (UnsupportedEncodingException e) {
				log.error("UnsupportedEncodingException : " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

}
