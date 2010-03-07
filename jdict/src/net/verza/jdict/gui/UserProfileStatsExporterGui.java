package net.verza.jdict.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

public class UserProfileStatsExporterGui extends JPanel implements
	ActionListener {

    private static final long serialVersionUID = 1L;
    private static final String FILE_CHOOSER_COMMAND_STRING = "Select File";
    private static final String EXPORT_BUTTON_COMMAND_STRING = "Export User Profile Statistics";
    private static Logger log;
    private static UserProfileStatsExporterGui singleton = null;
    private Dictionary dit;
    // swing objects
    private JFrame frame;
    private JFileChooser fileChooser;
    private GridBagConstraints c;
    private JButton exportButton;

    public UserProfileStatsExporterGui() {

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
	log.trace("called function");
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
	c.gridy = 0;
	c.gridwidth = 2;
	c.fill = GridBagConstraints.HORIZONTAL;
	fileChooser = new JFileChooser();
	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	JButton openButton = new JButton(FILE_CHOOSER_COMMAND_STRING, Commons
		.createImageIcon("images/Open16.gif"));
	openButton.addActionListener(this);
	add(openButton, c);

	c.gridx = 0;
	c.gridy = 1;
	exportButton = new JButton(EXPORT_BUTTON_COMMAND_STRING);
	exportButton.addActionListener(this);
	exportButton.setEnabled(false);
	add(exportButton, c);

    }

    private void createShowGUI() {
	log.trace("called function");
	frame = new JFrame("UserProfileStatsExporterGui");
	frame.addWindowListener(new MainFrameCloser(this));
	frame.setSize(400, 110);
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
	protected UserProfileStatsExporterGui frame = null;

	public MainFrameCloser(UserProfileStatsExporterGui frame) {
	    super();
	    log.trace("inside MainFrameCloser");
	    this.frame = frame;
	}

	public void windowClosed(WindowEvent e) {
	    log.trace("destroying instance and close the frame");
	    frame.destroyInstance();
	}
    }

    public static UserProfileStatsExporterGui getInstance() {
	if (singleton == null)
	    singleton = new UserProfileStatsExporterGui();
	return singleton;
    }

    public void actionPerformed(ActionEvent e) {

	String command = e.getActionCommand();
	log.trace("command received " + command);

	if (command.equals(FILE_CHOOSER_COMMAND_STRING)) {
	    exportButton.setEnabled(false);

	    if (fileChooser.showSaveDialog(UserProfileStatsExporterGui.this) == JFileChooser.APPROVE_OPTION)
		exportButton.setEnabled(true);
	} else if (command.equals(EXPORT_BUTTON_COMMAND_STRING)) {
	    try {
		BufferedWriter out = new BufferedWriter(new FileWriter(
			fileChooser.getSelectedFile()));
		out.write(dit.getUser().export2csv());
		out.flush();
		out.close();
		log.info("user stats exported");
		frame.dispose();

	    } catch (IOException ex) {
		ex.printStackTrace();
		log.error("IOException " + ex.getMessage());
		JOptionPane.showMessageDialog(frame, ex.getMessage());
	    }
	}

    }

}
