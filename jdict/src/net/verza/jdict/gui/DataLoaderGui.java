package net.verza.jdict.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import jxl.read.biff.BiffException;
import net.verza.jdict.dataloaders.LoaderOptionsStore;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DatabaseImportException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguagesConfiguration;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

public class DataLoaderGui extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private static Logger log;
    private static DataLoaderGui singleton = null;
    private Dictionary dit;
    private LoaderOptionsStore optionsObj;
    private JFrame frame;
    private JFileChooser fileChooser;
    private GridBagConstraints c;
    private JTextArea textAreaLog;
    private int y_coordinate;
    private JComboBox typeSelectCombo;
    private JCheckBox sectionImportCheckBox, categoryImportCheckBox;
    private JButton startImportButton;

    public DataLoaderGui() {

	super();
	try {
	    log = Logger.getLogger("jdict");
	    log.trace("called class " + this.getClass().getName());
	    optionsObj = new LoaderOptionsStore();
	    dit = Factory.getDictionary();
	    this.initComponents();
	    this.createShowGUI();
	} catch (DataNotFoundException e) {
	    e.printStackTrace();
	    log.error("DataNotFoundException " + e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (IOException e) {
	    e.printStackTrace();
	    log.error("IOException " + e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (DatabaseException e) {
	    e.printStackTrace();
	    log.error("DatabaseException " + e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (DynamicCursorException e) {
	    e.printStackTrace();
	    log.error("DynamicCursorException " + e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (KeyNotFoundException e) {
	    e.printStackTrace();
	    log.error("KeyNotFoundException " + e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	    log.error("IllegalArgumentException " + e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	    log.error("ClassNotFoundException " + e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (NoSuchMethodException e) {
	    e.printStackTrace();
	    log.error("NoSuchMethodException " + e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (InstantiationException e) {
	    e.printStackTrace();
	    log.error("InstantiationException " + e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	    log.error("IllegalAccessException " + e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
	    log.error("InvocationTargetException " + e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	}

    }

    private void initComponents() {

	setLayout(new GridBagLayout());
	setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels
	// gap to
	// the borders

	c = new GridBagConstraints(); // add some space between
	// components to
	// avoid clutter
	c.insets = new Insets(2, 2, 2, 2); // anchor all components
	// WEST
	c.anchor = GridBagConstraints.WEST;
	c.weightx = 1.0;
	c.weighty = 0.1;

	c.gridx = 0;
	c.gridy = this.y_coordinate;
	c.gridwidth = 2;
	c.fill = GridBagConstraints.HORIZONTAL;
	fileChooser = new JFileChooser();
	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	JButton openButton = new JButton("Select file to Load", Commons
		.createImageIcon("images/Open16.gif"));
	openButton.setActionCommand("openbutton");
	openButton.addActionListener(this);
	add(openButton, c);

	c.gridy = ++this.y_coordinate;
	add(new JSeparator(), c);
	c.gridwidth = 1;
	c.fill = GridBagConstraints.WEST;
	c.gridy = ++this.y_coordinate;
	JLabel buildTypeLabel = new JLabel("Select type of import");
	buildTypeLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(buildTypeLabel, c);
	c.gridx = 1;
	String[] types = { "rebuild", "update" };
	typeSelectCombo = new JComboBox(types);
	typeSelectCombo.addActionListener(this);
	add(typeSelectCombo, c);

	c.gridy = ++this.y_coordinate;
	add(new JSeparator(), c);
	// Call function buildLanguageOptionList to dynamically show
	// only enabled languages
	this.buildLanguageOptionList();

	c.gridy = ++this.y_coordinate;
	add(new JSeparator(), c);

	// Category Table Import Option
	c.gridx = 0;
	c.gridy = ++this.y_coordinate;
	JLabel categoryImportLabel = new JLabel("Import category Table");
	categoryImportLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(categoryImportLabel, c);
	c.gridx = 1;
	categoryImportCheckBox = new JCheckBox();
	categoryImportCheckBox.setActionCommand("category");
	categoryImportCheckBox.addActionListener(this);
	add(categoryImportCheckBox, c);

	// Selection Table Import Option
	c.gridx = 0;
	c.gridy = ++this.y_coordinate;
	JLabel selectionImportLabel = new JLabel("Import Section Table");
	selectionImportLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(selectionImportLabel, c);
	c.gridx = 1;
	sectionImportCheckBox = new JCheckBox();
	sectionImportCheckBox.setActionCommand("section");
	sectionImportCheckBox.addActionListener(this);
	add(sectionImportCheckBox, c);

	// Import command button
	c.gridy = ++this.y_coordinate;
	add(new JSeparator(), c);
	c.gridx = 0;
	c.gridy = ++this.y_coordinate;
	c.gridwidth = 2;
	c.fill = GridBagConstraints.HORIZONTAL;
	startImportButton = new JButton("import !!");
	startImportButton.setActionCommand("import");
	startImportButton.addActionListener(this);
	startImportButton.setEnabled(false);
	add(startImportButton, c);

	// Text Area used for logging
	c.gridy = ++this.y_coordinate;
	add(new JSeparator(), c);
	c.gridx = 0;
	c.gridy = ++this.y_coordinate;
	c.gridwidth = 3;
	c.fill = GridBagConstraints.BOTH;
	textAreaLog = new JTextArea(5, 20);
	textAreaLog.setMargin(new Insets(5, 5, 5, 5));
	textAreaLog.setEditable(false);
	JScrollPane textAreaLogScrollPane = new JScrollPane(textAreaLog);
	add(textAreaLogScrollPane, c);

    }

    private void createShowGUI() {
	// Create and set up the window.
	frame = new JFrame("Window");
	frame.addWindowListener(new MainFrameCloser(this));
	frame.setTitle("Import Options Chooser Window");
	frame.setSize(350, 550);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.getContentPane().add(this);
	frame.setBackground(GUIPreferences.backgroundColor);
	frame.setResizable(true);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);

    }

    private void destroyInstance() {
	if (singleton != null)
	    singleton = null;
    }

    public class MainFrameCloser extends java.awt.event.WindowAdapter {
	protected DataLoaderGui frame = null;

	public MainFrameCloser(DataLoaderGui frame) {
	    super();
	    log.trace("inside MainFrameCloser");
	    this.frame = frame;
	}

	public void windowClosing(WindowEvent e) {
	    log.trace("destroying instance and close the frame");
	    frame.destroyInstance();
	    frame.getJFrame().dispose();
	}
    }

    private JFrame getJFrame() {
	return frame;
    }

    public static DataLoaderGui getInstance() {
	if (singleton == null)
	    singleton = new DataLoaderGui();
	return singleton;
    }

    private void buildLanguageOptionList() {

	HashMap<String, LanguageConfigurationClassDescriptor> ldesc = LanguagesConfiguration
		.getLanguageConfigurationBlock();
	LanguageConfigurationClassDescriptor sub = null;
	for (Iterator<String> it = ldesc.keySet().iterator(); it.hasNext();) {
	    sub = (LanguageConfigurationClassDescriptor) ldesc.get(it.next());
	    if (sub.isEnabled) {
		c.gridx = 0;
		c.gridy = ++this.y_coordinate;
		String lang = new String(sub.getLanguageNickname()
			+ sub.getType());
		JLabel languageLabel = new JLabel(lang);
		languageLabel.setBorder(BorderFactory
			.createLineBorder(GUIPreferences.borderColor));
		add(languageLabel, c);
		this.optionsObj.getLabels().put(lang, false);
		c.gridx = 1;
		JCheckBox languageImportCheckBox = new JCheckBox();
		languageImportCheckBox.setActionCommand(lang);
		languageImportCheckBox.addActionListener(this);
		add(languageImportCheckBox, c);
	    }
	}
    }

    public void actionPerformed(ActionEvent ex) {

	String command = ex.getActionCommand();
	log.debug("command received " + command);

	if (this.optionsObj.getLabels().containsKey(command))
	    this.optionsObj.setLabels(command, ((JCheckBox) ex.getSource())
		    .isSelected());

	else if (command == "openbutton") {
	    int returnVal = fileChooser.showOpenDialog(DataLoaderGui.this);

	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		File file = fileChooser.getSelectedFile();
		this.optionsObj.setInputFile(file);
		textAreaLog.append("opening file " + file.getName() + ".\n");
		log.debug("choosed import file " + file);
		this.startImportButton.setEnabled(true);
	    } else {
		textAreaLog.append("open command cancelled by user.\n");
	    }

	} else if (command.equals("import")) {

	    try {

		this.optionsObj.setTypeOfImport((String) this.typeSelectCombo
			.getSelectedItem());

		// if category check box has been selected
		// import will occur
		if (this.categoryImportCheckBox.isSelected()) {
		    int count = dit.loadCategoryDatabase(optionsObj);
		    textAreaLog.append("imported category table " + count
			    + " objects\n");

		}

		if (this.sectionImportCheckBox.isSelected()) {
		    int count = dit.loadSectionDatabase(optionsObj);
		    textAreaLog.append("imported section table " + count
			    + "objects\n");
		}

		if (this.optionsObj.getLabels().size() > 0) {
		    HashMap<String, Integer> resultMap = dit
			    .loadDatabase(optionsObj);

		    Iterator<String> it = resultMap.keySet().iterator();
		    while (it.hasNext()) {
			String key = it.next();
			textAreaLog.append("imported language " + key + ": "
				+ resultMap.get(key) + " objects\n");
		    }
		}

	    } catch (IOException e) {
		e.printStackTrace();
		log.error("IOException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (DatabaseException e) {
		e.printStackTrace();
		log.error("DatabaseException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (KeyNotFoundException e) {
		e.printStackTrace();
		log.error("KeyNotFoundException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (BiffException e) {
		e.printStackTrace();
		log.error("BiffException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (LabelNotFoundException e) {
		e.printStackTrace();
		log.error("LabelNotFoundException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (DatabaseImportException e) {
		e.printStackTrace();
		log.error("DatabaseImportException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (LanguagesConfigurationException e) {
		e.printStackTrace();
		log.error("LanguagesConfigurationException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (SecurityException e) {
		e.printStackTrace();
		log.error("SecurityException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (IllegalArgumentException e) {
		e.printStackTrace();
		log.error("IllegalArgumentException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
		log.error("ClassNotFoundException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (NoSuchMethodException e) {
		e.printStackTrace();
		log.error("NoSuchMethodException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (InstantiationException e) {
		e.printStackTrace();
		log.error("InstantiationException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (IllegalAccessException e) {
		e.printStackTrace();
		log.error("IllegalAccessException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (InvocationTargetException e) {
		e.printStackTrace();
		log.error("InvocationTargetException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    }
	}

    }

}
