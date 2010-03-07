package net.verza.jdict.gui;

/**
 * @author ChristianVerdelli
 *
 */

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

public class VerbResultTable extends JPanel {
    private static final long serialVersionUID = 1L;

    // Attributi di istanza
    private static Logger log;

    private JTable table;
    private JScrollPane scroll;
    private Object dataValues[][];
    static String headers[] = { "Person", "Verb Form" };

    // Costruttore che riceve in ingresso un matrice di parole singolari
    // plurali in base ottenute dalla ricerca
    public VerbResultTable(String formArray[][]) {
	super(new GridLayout(1, 0));

	log = Logger.getLogger("jdict");
	log.trace("initializing VerbResultTable class");

	CreateData(formArray);
	CreateTable();
	add(scroll);

    }

    // Crea la tabella ed inserisce l'intestazione
    private void CreateTable() {
	DefaultTableModel dm = new DefaultTableModel();
	dm.setDataVector(dataValues, headers);
	table = new JTable(dm);

	scroll = new JScrollPane(table);

    }

    // Popola la tabella con i dati quando l'oggetto ResultTable viene
    // instanziato passando dei dati
    private void CreateData(String[][] d) {
	int length = d.length;
	dataValues = new Object[length][headers.length];

	log.debug("couples received " + length);

	for (int iY = 0; iY < length; iY++) {
	    dataValues[iY][0] = d[iY][0];
	    dataValues[iY][1] = d[iY][1];
	}

    }

}
