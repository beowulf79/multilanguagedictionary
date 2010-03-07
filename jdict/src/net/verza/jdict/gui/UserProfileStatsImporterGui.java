package net.verza.jdict.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.model.UserProfile;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

public class UserProfileStatsImporterGui extends JPanel implements
	ActionListener {

    private static final long serialVersionUID = 1L;
    private static final String FILE_CHOOSER_COMMAND_STRING = "Select file to Load";
    private static final String IMPORT_BUTTON_COMMAND_STRING = "Import User Profile";
    private static Logger log;
    private static UserProfileStatsImporterGui singleton = null;
    private Dictionary dit;
    // swing objects
    private JFrame frame;
    private JFileChooser fileChooser;
    private GridBagConstraints c;
    private JCheckBox update;
    private JButton importButton;

    public UserProfileStatsImporterGui() {

	super();

	try {

	    log = Logger.getLogger("jdict");
	    log.trace("called class " + this.getClass().getName());
	    dit = Factory.getDictionary();
	    this.initComponents();
	    this.createShowGUI();

	} catch (DataNotFoundException e) {
	    log.error(e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (IOException e) {
	    log.error(e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (DatabaseException e) {
	    log.error(e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (DynamicCursorException e) {
	    log.error(e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (KeyNotFoundException e) {
	    log.error(e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
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
	}

    }

    private void initComponents() {

	setLayout(new GridBagLayout());
	setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels gap to
	// the borders

	c = new GridBagConstraints(); // add some space between components to
	// avoid clutter
	c.insets = new Insets(2, 2, 2, 2); // anchor all components WEST
	c.anchor = GridBagConstraints.EAST;
	c.weightx = 1.0;
	c.weighty = 0.1;
	c.gridx = 0;
	c.gridy = 0;
	c.gridwidth = 2;
	c.fill = GridBagConstraints.HORIZONTAL;
	fileChooser = new JFileChooser();
	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	JButton openButton = new JButton(FILE_CHOOSER_COMMAND_STRING, Commons
		.createImageIcon("images/Open16.gif"));
	openButton.addActionListener(this);
	add(openButton, c);

	c.gridy = 1;
	c.gridwidth = 1;
	c.fill = GridBagConstraints.NONE;
	JLabel overwriteCheckBox = new JLabel("overwrite current statistics");
	overwriteCheckBox.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(overwriteCheckBox, c);
	c.gridx = 1;
	update = new JCheckBox();
	add(update, c);

	c.gridx = 0;
	c.gridy = 2;
	c.gridwidth = 2;
	c.fill = GridBagConstraints.HORIZONTAL;
	importButton = new JButton(IMPORT_BUTTON_COMMAND_STRING);
	importButton.addActionListener(this);
	importButton.setEnabled(false);
	add(importButton, c);

    }

    private void createShowGUI() {
	// Create and set up the window.
	frame = new JFrame("UserProfileStatsImporterGui");
	frame.addWindowListener(new MainFrameCloser(this));
	frame.setSize(400, 120);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.getContentPane().add(this);
	frame.setBackground(GUIPreferences.backgroundColor);
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);

    }

    private void destroyInstance() {
	if (singleton != null)
	    singleton = null;
    }

    public class MainFrameCloser extends java.awt.event.WindowAdapter {
	protected UserProfileStatsImporterGui frame = null;

	public MainFrameCloser(UserProfileStatsImporterGui frame) {
	    super();
	    log.trace("inside MainFrameCloser");
	    this.frame = frame;
	}

	public void windowClosed(WindowEvent e) {
	    log.trace("destroying instance and close the frame");
	    frame.destroyInstance();
	}

	/*
	 * public void windowDeactivated(WindowEvent e) { log.trace("destroying
	 * instance and close the frame"); frame.destroyInstance();
	 * frame.getJFrame().dispose(); }
	 */

    }

    public static UserProfileStatsImporterGui getInstance() {
	if (singleton == null)
	    singleton = new UserProfileStatsImporterGui();
	return singleton;
    }

    public void actionPerformed(ActionEvent e) {

	String command = e.getActionCommand();
	log.trace("command received " + command);

	try {
	    if (command.equals(FILE_CHOOSER_COMMAND_STRING)) {
		if (fileChooser
			.showOpenDialog(UserProfileStatsImporterGui.this) == JFileChooser.APPROVE_OPTION)
		    this.importButton.setEnabled(true);

	    } else if (command.equals(IMPORT_BUTTON_COMMAND_STRING)) {
		UserProfile up = dit.getUser();
		if (update.isSelected())
		    up.flushStatistics();
		up.addQuizStat(fileChooser.getSelectedFile());
		dit.writeUserProfile(up);
		log.info("user stats imported");
		frame.dispose();
	    }

	} catch (IOException ex) {
	    ex.printStackTrace();
	    log.error("IOException " + ex.getMessage());
	    JOptionPane.showMessageDialog(frame, ex.getMessage());
	} catch (DatabaseException ex) {
	    ex.printStackTrace();
	    log.error("DatabaseException " + ex.getMessage());
	    JOptionPane.showMessageDialog(frame, ex.getMessage());
	}

    }

}
