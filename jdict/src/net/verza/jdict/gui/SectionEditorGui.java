package net.verza.jdict.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

public class SectionEditorGui extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private static final String FILE_CHOOSER_COMMAND_STRING = "Select file to Load";
    private static final String MODIFY_SECTION_COMMAND_STRING = "Modify Section";
    private static Logger log;
    private static SectionEditorGui singleton = null;
    private JCheckBox isAdminJCheckBox;
    private JTextField sectionTextField;
    private JFileChooser fileChooser;
    private JFrame frame;
    private JButton create;
    private int y_position = 0;
    private Dictionary dit;
    private Object dataValues[][];
    private String headers[] = { "Section Key", "Section Value" };
    private JTable table;

    public SectionEditorGui() {
	super();
	singleton = this;
	log = Logger.getLogger("jdict");
	log.trace("Initialazing class " + this.getClass().getCanonicalName());
	try {
	    dit = Factory.getDictionary();
	    initComponents();
	    createShowGUI();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DatabaseException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DataNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DynamicCursorException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (KeyNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (NoSuchMethodException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (InstantiationException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	}

    }

    private void initComponents() throws FileNotFoundException,
	    UnsupportedEncodingException, IllegalArgumentException,
	    DatabaseException, DataNotFoundException, DynamicCursorException,
	    KeyNotFoundException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException {

	setLayout(new GridBagLayout());
	// setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5
	// pixels gap to
	// the borders

	GridBagConstraints c = new GridBagConstraints();
	c.insets = new Insets(2, 2, 2, 2);// add some space between
	// components
	// to avoid clutter
	// c.anchor = GridBagConstraints.NORTHWEST;
	c.weightx = 1.0;
	c.weighty = 1.0;
	// c.gridheight = 3;// GridBagConstraints.REMAINDER;
	// c.gridwidth = GridBagConstraints.RELATIVE;
	c.fill = GridBagConstraints.BOTH;
	c.gridx = 0;
	c.gridy = y_position++;
	JScrollPane scroll = new JScrollPane(buildTable());
	add(scroll, c);

	// Submit Button
	c.gridx = 0;
	c.gridy = y_position++;
	c.fill = GridBagConstraints.HORIZONTAL;
	// c.gridwidth = 1;
	// c.gridheight = GridBagConstraints.REMAINDER;
	// c.anchor = GridBagConstraints.NORTHEAST;
	// c.fill = GridBagConstraints.HORIZONTAL;
	create = new JButton("Modify", Commons.createImageIcon(""));
	create.addActionListener(this);
	create.setActionCommand(MODIFY_SECTION_COMMAND_STRING);
	add(create, c);

    }

    private void createShowGUI() {
	frame = new JFrame("SectionEditorGui");
	frame.addWindowListener(new MainFrameCloser(this));
	frame.setSize(600, 600);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.getContentPane().add(this);
	frame.setBackground(GUIPreferences.backgroundColor);
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
    }

    private JTable buildTable() throws FileNotFoundException,
	    UnsupportedEncodingException, IllegalArgumentException,
	    DatabaseException, DataNotFoundException, DynamicCursorException,
	    KeyNotFoundException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException {

	System.out.println("section db size " + dit.getSectionDatabaseSize());
	dataValues = new Object[dit.getSectionDatabaseSize() + 1][headers.length];
	DefaultTableModel tablemodel = new DefaultTableModel();

	String[] keys = dit.getSectionKey();
	String[] values = dit.getSectionValue();
	for (int i = 0; i < keys.length; i++) {
	    dataValues[i][0] = keys[i];
	    dataValues[i][1] = values[i];
	    String value = dit.readSectionDatabase(keys[i]);
	    System.out.println("key " + keys[i] + " value " + values[i] + "\n");
	}
	tablemodel.setDataVector(dataValues, headers);
	table = new JTable(tablemodel);
	TableColumn column = table.getColumnModel().getColumn(0);
	// table.getColumnModel().removeColumn(column);
	TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(
		tablemodel);
	table.setRowSorter(sorter);

	return table;
    }

    private void updateTable() {
	for (int i = 0; i < table.getRowCount(); i++) {
	    String key = (String) table.getValueAt(i, 0);
	    String value = (String) table.getValueAt(i, 1);
	    System.out.println("key " + key + " value " + value);
	}

    }

    private void destroyInstance() {
	if (singleton != null)
	    singleton = null;
    }

    public class MainFrameCloser extends java.awt.event.WindowAdapter {
	protected SectionEditorGui frame = null;

	public MainFrameCloser(SectionEditorGui frame) {
	    super();
	    log.trace("inside MainFrameCloser");
	    this.frame = frame;
	}

	public void windowClosed(WindowEvent e) {
	    log.trace("destroying instance and close the frame");
	    frame.destroyInstance();
	}
    }

    public static SectionEditorGui getInstance() {
	if (singleton == null)
	    singleton = new SectionEditorGui();
	return singleton;
    }

    public void actionPerformed(ActionEvent ex) {
	updateTable();
    }

}
