package net.verza.jdict.gui;

/**
 * @author ChristianVerdelli
 *
 */

import java.awt.Color;
import java.awt.GridLayout;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.verza.jdict.quiz.QuizResult;

import org.apache.log4j.Logger;

public class QuizResultTable extends JPanel {
    private static final long serialVersionUID = 1L;

    // Attributi di istanza
    private static Logger log;

    private JTable table;
    private JScrollPane scroll;
    private Object dataValues[][];
    static String headers[] = { "Question", "Result", "User Answer",
	    "Correct Answer" };
    Boolean isByteArray;

    // When the Question is an Audio file this is set True

    // Costruttore che riceve in ingresso un matrice di parole singolari
    // plurali in base ottenute dalla ricerca
    public QuizResultTable(Vector<QuizResult> data)
	    throws UnsupportedEncodingException {
	super(new GridLayout(1, 0));

	log = Logger.getLogger("jdict");
	log.trace("initializing QuizResultTable class");

	CreateData(data);
	CreateTable();
	add(scroll);

    }

    // Crea la tabella ed inserisce l'intestazione
    private void CreateTable() {
	DefaultTableModel dm = new DefaultTableModel();
	dm.setDataVector(dataValues, headers);
	table = new JTable(dm);
	ColorRenderer cr = new ColorRenderer("Result");
	// color the first column only
	table.getColumnModel().getColumn(1).setCellRenderer(cr);
	table.setBackground(Color.PINK);
	table.getColumnModel().getColumn(0).setPreferredWidth(150);
	table.getColumnModel().getColumn(1).setPreferredWidth(90);
	table.getColumnModel().getColumn(2).setPreferredWidth(120);
	table.getColumnModel().getColumn(3).setPreferredWidth(200);

	if (isByteArray) {
	    table.getColumn("Question").setCellRenderer(
		    new JTableButtonRenderer("Play Audio"));
	    table.getColumn("Question").setCellEditor(
		    new JTableAudioButtonEditor(new JCheckBox()));
	}
	scroll = new JScrollPane(table);

    }

    // Popola la tabella con i dati quando l'oggetto ResultTable viene
    // instanziato passando dei dati
    private void CreateData(Vector<QuizResult> data)
	    throws UnsupportedEncodingException {
	int length = data.size();
	dataValues = new Object[length][headers.length];
	log.debug("couples received " + length);

	for (int iY = 0; iY < length; iY++) {

	    QuizResult qs = (QuizResult) data.get(iY);
	    log.debug("recevied QuizStat object " + qs.toString());
	    if (qs.getQuestion() instanceof String) {
		log.debug("storing question as string type");
		isByteArray = false;
		String ques = (String) qs.getQuestion();
		dataValues[iY][0] = new String(ques.getBytes(), "UTF-8");
	    }
	    if (qs.getQuestion() instanceof byte[]) {
		log.debug("storing question as byte array");
		isByteArray = true;
		dataValues[iY][0] = qs.getQuestion();
	    }
	    dataValues[iY][1] = new String(qs.getQuizExitCode());
	    dataValues[iY][2] = new String(qs.getUserAnswer().getBytes(),
		    "UTF-8");
	    dataValues[iY][3] = new String(qs.getCorrectAnswer().getBytes(),
		    "UTF-8");

	}

    }

}
