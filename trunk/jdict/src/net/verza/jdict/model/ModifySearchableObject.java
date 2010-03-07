package net.verza.jdict.model;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.exceptions.LinkIDException;
import net.verza.jdict.gui.GUIPreferences;
import net.verza.jdict.gui.LanguageSelection;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

/**
 * @author Christian Verdelli
 */

public class ModifySearchableObject extends JPanel implements ActionListener {

    public static final String NOT_SELECTED_STRING = "---- Nothing Selected ----";
    private static final long serialVersionUID = 1L;
    private static Logger log;
    private static ModifySearchableObject instance = null;
    private Dictionary dit;
    private GridBagConstraints c;
    private JFrame frame;
    private JTextField jtxf1;
    private JComboBox srcLangCombo;
    private HashMap<String, String[]> translations;

    public ModifySearchableObject() {
	super();

	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());
	translations = new HashMap<String, String[]>();
	initComponents(); // initialize components graphics
	createAndShowGUI();
    }

    private void buildLanguageMenu() throws LanguagesConfigurationException {

	translations = LanguageSelection.buildLanguageMenu();

	// add JComboBox to choose the languages of the lookup
	c.gridx = 0;
	c.gridy = 0;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = 1;
	JLabel srcLangJLabel = new JLabel("Select language to lookup");
	srcLangJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(srcLangJLabel, c);
	c.gridx = 1;
	srcLangCombo = new JComboBox(translations.keySet().toArray());
	srcLangCombo.addItem(NOT_SELECTED_STRING);
	srcLangCombo.setSelectedIndex(srcLangCombo.getItemCount() - 1);
	add(srcLangCombo, c);
    }

    private void initComponents() {

	setLayout(new GridBagLayout());
	setBorder(new EmptyBorder(new Insets(5, 5, 5, 5))); // 5 pixels gap to
	// the borders

	c = new GridBagConstraints(); // add some space between components to

	c.insets = new Insets(2, 2, 2, 2); // avoid clutter
	c.anchor = GridBagConstraints.NORTHEAST;// anchor all components WEST
	c.weightx = 0.1;
	c.weighty = 0;
	c.fill = GridBagConstraints.HORIZONTAL;

	try {
	    buildLanguageMenu();
	} catch (LanguagesConfigurationException e) {
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	    log.error("LanguagesConfigurationException " + e.getMessage());
	    e.printStackTrace();
	}

	// add JLabel and JTextfield for the user input
	c.gridx = 0;
	c.gridy = 2;
	JLabel lookupTextJLabel = new JLabel("enter the word to lookup");
	lookupTextJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(lookupTextJLabel, c);

	c.gridx = 1;
	jtxf1 = new JTextField("");
	add(jtxf1, c);

	// add JButton to send the search
	c.gridx = 0;
	c.gridy = 3;
	c.gridwidth = 2;
	JButton jbt1 = new JButton("search");
	jbt1.addActionListener(this);
	add(jbt1, c);

	setVisible(true);

    }

    private void createAndShowGUI() {
	log.trace("called function createAndShowGUI");
	frame = new JFrame("ModifySearchableObject");
	frame.addWindowListener(new MainFrameCloser(this));
	frame.setSize(500, 170);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.getContentPane().add(this);
	frame.setBackground(GUIPreferences.backgroundColor);
	frame.setResizable(true);
	frame.setLocationRelativeTo(null);
	frame.pack();
	frame.setVisible(true);
    }

    private void destroyInstance() {
	log.trace("called function destroyInstance");
	if (instance != null)
	    instance = null;
    }

    public class MainFrameCloser extends java.awt.event.WindowAdapter {
	protected ModifySearchableObject frame = null;

	public MainFrameCloser(ModifySearchableObject frame) {
	    super();
	    log.trace("called function MainFrameCloser");
	    this.frame = frame;
	}

	public void windowClosed(WindowEvent e) {
	    log.trace("called function windowClosed");
	    frame.destroyInstance();
	}
    }

    /*
     * Returns pointer to this instance; SingleTon Pattern
     */
    public static ModifySearchableObject getInstance() {
	if (instance == null) {
	    instance = new ModifySearchableObject();
	}
	return instance;
    }

    public void actionPerformed(ActionEvent evt) {

	String command = evt.getActionCommand();

	if (command.equals("search")) {
	    SearchableObject obj = null;
	    try {
		dit = Factory.getDictionary();

		obj = dit.read((String) srcLangCombo.getSelectedItem(),
			new String(jtxf1.getText().getBytes("UTF-8")));

		if (obj == null) {
		    JOptionPane.showMessageDialog(frame,
			    "data not found in the dictionary");
		    return;
		}

		EditorController.getInstance().setData(
			(String) srcLangCombo.getSelectedItem(), obj.getid());

	    } catch (UnsupportedEncodingException e) {
		log.error("UnsupportedEncodingException: " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
		return;
	    } catch (DatabaseException e) {
		JOptionPane.showMessageDialog(frame, e.getMessage());
		log.error("DatabaseException: " + e.getMessage());
		return;
	    } catch (DataNotFoundException e) {
		JOptionPane.showMessageDialog(frame, e.getMessage());
		return;
	    } catch (DynamicCursorException e) {
		JOptionPane.showMessageDialog(frame, e.getMessage());
		log.error("DynamicCursorException: " + e.getMessage());
		return;
	    } catch (FileNotFoundException e) {
		JOptionPane.showMessageDialog(frame, e.getMessage());
		log.error("DynamicCursorException: " + e.getMessage());
		return;
	    } catch (KeyNotFoundException e) {
		JOptionPane.showMessageDialog(frame, e.getMessage());
		log.error("DynamicCursorException: " + e.getMessage());
		return;
	    } catch (LanguagesConfigurationException e) {
		JOptionPane.showMessageDialog(frame, e.getMessage());
		log.error("LanguagesConfigurationException: " + e.getMessage());
		return;
	    } catch (IllegalArgumentException e) {
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
	    } catch (SecurityException e) {
		e.printStackTrace();
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (LinkIDException e) {
		e.printStackTrace();
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    }

	}
    }

}
