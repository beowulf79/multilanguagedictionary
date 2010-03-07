package net.verza.jdict.model;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.exceptions.LinkIDException;
import net.verza.jdict.gui.GUIPreferences;
import net.verza.jdict.properties.LanguagesConfiguration;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

public class CreatorController extends JPanel implements ActionListener {

    public static final String NOT_SELECTED_STRING = "---- Nothing Selected ----";
    public static final String COMBO_LANGUAGE_CHANGE = "Language";
    public static final String NEXT_BUTTON_STRING = "Next";
    public static final String FINISH_BUTTON_STRING = "Finish";
    public static final String CONFIRM_MODIFY_NEW = "Proceed to modify new inserted object";

    public static final int EDITOR_POSITION = 2;
    public static final int BUTTONS_Y_POSITION = 3;

    private static final long serialVersionUID = 1L;
    private static Logger log;
    private static CreatorController singleton = null;
    private JFrame frame;
    private JPanel panel;
    private SearchableObjectEditor searchableObjectCreator;
    private JComboBox languageSelector;
    private GridBagConstraints c;
    private JButton next, finish;
    private short y_position;
    private Map<Integer, String> transactionMap;
    private String language;

    public CreatorController() {
	super();
	singleton = this;
	log = Logger.getLogger("jdict");
	log.trace("Initialazing WordCreatorController class");
	transactionMap = new HashMap<Integer, String>();

	initComponents();
	createAndShowGUI();
    }

    private void initComponents() {
	log.trace("called function initComponents");
	panel = new JPanel(new GridBagLayout());
	panel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5
	panel.setBackground(GUIPreferences.backgroundColor);
	c = new GridBagConstraints();
	c.insets = new Insets(2, 2, 2, 2);
	c.anchor = GridBagConstraints.WEST;
	c.weightx = 0.1;
	c.weighty = 0.1;
	c.gridheight = 1;
	c.gridwidth = 1;
	y_position = 0;

	c.gridx = 0;
	c.gridy = y_position;
	JLabel languageLabel = new JLabel("select language for the new object");
	languageLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	panel.add(languageLabel, c);

	c.gridx = 1;
	c.gridy = y_position++;
	Set<String> translations = LanguagesConfiguration
		.getAvalaibleLanguages();
	languageSelector = new JComboBox(translations.toArray());
	languageSelector.setActionCommand(COMBO_LANGUAGE_CHANGE);
	languageSelector.addItem(NOT_SELECTED_STRING);
	languageSelector.addActionListener(this);
	panel.add(languageSelector, c);

	c.gridx = 0;
	c.gridy = BUTTONS_Y_POSITION;
	c.anchor = GridBagConstraints.EAST;
	next = new JButton(NEXT_BUTTON_STRING);
	next.addActionListener(this);
	next.setActionCommand(NEXT_BUTTON_STRING);
	panel.add(next, c);

	c.anchor = GridBagConstraints.WEST;
	c.gridx = 1;
	finish = new JButton(FINISH_BUTTON_STRING);
	finish.setActionCommand(FINISH_BUTTON_STRING);
	finish.addActionListener(this);
	panel.add(finish, c);

    }

    private void createAndShowGUI() {
	log.trace("called functio createAndShowGUI");
	frame = new JFrame("CreatorController");
	frame.addWindowListener(new MainFrameCloser(this));
	frame.setSize(500, 170);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.getContentPane().add(panel);
	frame.setBackground(GUIPreferences.backgroundColor);
	frame.setResizable(true);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
    }

    public static CreatorController getInstance() {
	if (singleton == null)
	    singleton = new CreatorController();
	return singleton;
    }

    private void destroyInstance() {
	log.trace("called function destroyInstance");
	if (singleton != null)
	    singleton = null;
    }

    public class MainFrameCloser extends java.awt.event.WindowAdapter {
	protected CreatorController frame = null;

	public MainFrameCloser(CreatorController frame) {
	    super();
	    log.trace("called function MainFrameCloser");
	    this.frame = frame;
	}

	public void windowClosed(WindowEvent e) {
	    log.trace("called function windowClosed");
	    frame.destroyInstance();
	}
    }

    public void actionPerformed(ActionEvent e) {
	log.trace("called function actionPerformed");
	try {
	    if (e.getActionCommand().equals(NEXT_BUTTON_STRING)) {

		log.trace("received command " + NEXT_BUTTON_STRING);
		// send write command and remove previous word from the panel
		searchableObjectCreator.write();
		panel.remove(searchableObjectCreator);
		frame.pack();
		// store all the new words in a map to later give the chance to
		// modify them
		transactionMap.put(searchableObjectCreator
			.getSearchableObject().getid(), language);
		// searchableObjectCreator
		searchableObjectCreator = null;

	    } else if (e.getActionCommand().equals(FINISH_BUTTON_STRING)) {
		log.trace("received command " + FINISH_BUTTON_STRING);
		searchableObjectCreator.write();
		transactionMap.put(searchableObjectCreator
			.getSearchableObject().getid(), language);
		int confirm = JOptionPane.showConfirmDialog(null,
			CONFIRM_MODIFY_NEW);
		// Yes ; open EditorController with transactionMap
		if (confirm == JOptionPane.YES_OPTION) {
		    log.debug("calling EditorController");
		    EditorController.getInstance().setData(transactionMap);
		    frame.dispose();
		} else if (confirm == JOptionPane.NO_OPTION)
		    frame.dispose();
	    } else if (e.getActionCommand().equals(COMBO_LANGUAGE_CHANGE)) {

		// remove previous Searchable Object from panel if next button
		// hasn't been pressed
		if (searchableObjectCreator != null)
		    panel.remove(searchableObjectCreator);

		// add new word on the panel
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = EDITOR_POSITION;
		language = ((String) languageSelector.getSelectedItem());
		String classToLoadName = LanguagesConfiguration
			.getLanguageMainConfigNode(language).getEditorClass();
		log.debug("language " + language + " classToLoad "
			+ classToLoadName);

		Class<?> toRun = Class.forName(classToLoadName);
		Class<?>[] c_arr = new Class[] { String.class };
		Constructor<?> constr = toRun.getConstructor(c_arr);

		searchableObjectCreator = (SearchableObjectEditor) constr
			.newInstance(language);
		searchableObjectCreator.initComponents();

		searchableObjectCreator.setBorder(BorderFactory
			.createTitledBorder(BorderFactory.createLineBorder(
				GUIPreferences.borderColor, 3), language));

		c.gridwidth = 2;
		panel.add(searchableObjectCreator, c);
		frame.pack();
		frame.validate();
	    }

	} catch (DatabaseException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("DatabaseException: " + exp.getStackTrace());
	    exp.printStackTrace();
	} catch (HeadlessException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("HeadlessException: " + exp.getMessage());
	} catch (UnsupportedEncodingException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("UnsupportedEncodingException: " + exp.getMessage());
	} catch (KeyNotFoundException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("KeyNotFoundException: " + exp.getMessage());
	} catch (DynamicCursorException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("DynamicCursorException: " + exp.getMessage());
	} catch (DataNotFoundException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("DataNotFoundException: " + exp.getMessage());
	} catch (IllegalArgumentException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("IllegalArgumentException: " + exp.getMessage());
	    exp.printStackTrace();
	} catch (IllegalAccessException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("IllegalAccessException: " + exp.getMessage());
	    exp.printStackTrace();
	} catch (InvocationTargetException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("InvocationTargetException: " + exp.getMessage());
	    exp.printStackTrace();
	} catch (MalformedURLException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("MalformedURLException: " + exp.getMessage());
	    exp.printStackTrace();
	} catch (FileNotFoundException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("FileNotFoundException: " + exp.getMessage());
	    exp.printStackTrace();
	} catch (IOException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("IOException: " + exp.getMessage());
	    exp.printStackTrace();
	} catch (LanguagesConfigurationException e1) {
	    JOptionPane.showMessageDialog(frame, e1.getMessage());
	    log.error("LanguagesConfigurationException " + e1.getMessage());
	    e1.printStackTrace();
	} catch (SecurityException e1) {
	    JOptionPane.showMessageDialog(frame, e1.getMessage());
	    log.error("SecurityException " + e1.getMessage());
	    e1.printStackTrace();
	} catch (ClassNotFoundException e1) {
	    JOptionPane.showMessageDialog(frame, e1.getMessage());
	    log.error("ClassNotFoundException " + e1.getMessage());
	    e1.printStackTrace();
	} catch (NoSuchMethodException e1) {
	    JOptionPane.showMessageDialog(frame, e1.getMessage());
	    log.error("NoSuchMethodException " + e1.getMessage());
	    e1.printStackTrace();
	} catch (InstantiationException e1) {
	    JOptionPane.showMessageDialog(frame, e1.getMessage());
	    log.error("InstantiationException " + e1.getMessage());
	    e1.printStackTrace();
	} catch (LinkIDException e1) {
	    e1.printStackTrace();
	    log.error("Exception " + e1.getMessage());
	    JOptionPane.showMessageDialog(null, e1.getMessage());
	}

    }
}
