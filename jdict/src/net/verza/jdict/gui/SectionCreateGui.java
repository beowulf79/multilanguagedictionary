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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

/**
 * @author ChristianVerdelli
 * 
 */

public class SectionCreateGui extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String FILE_CHOOSER_COMMAND_STRING = "Select file to Load";
    private static final String CREATE_USERPROFILE_COMMAND_STRING = "Create User Profile";
    private static Logger log;
    private static SectionCreateGui singleton = null;
    private JCheckBox isAdminJCheckBox;
    private JTextField sectionTextField;
    private JFileChooser fileChooser;
    private JFrame frame;
    private JButton create;
    private int y_position = 0;

    public SectionCreateGui() {
	super();
	singleton = this;
	log = Logger.getLogger("jdict");
	log.trace("Initialazing class " + this.getClass().getCanonicalName());
	initComponents();
	createShowGUI();
    }

    private void initComponents() {

	setLayout(new GridBagLayout());
	// setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5
	// pixels gap to
	// the borders

	GridBagConstraints c = new GridBagConstraints();
	c.insets = new Insets(2, 2, 2, 2);// add some space between
	// components
	// to avoid clutter
	c.anchor = GridBagConstraints.NORTHWEST;
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridheight = 1;
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = y_position++;
	JLabel sectionJLabel = new JLabel("Sezione");
	sectionJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(sectionJLabel, c);
	c.gridx = 1;
	c.anchor = GridBagConstraints.NORTHEAST;
	sectionTextField = new JTextField(15);
	add(sectionTextField, c);
	add(new JSeparator(), c);

	c.gridx = 0;
	c.gridy = y_position++;
	c.gridwidth = 2;
	c.anchor = GridBagConstraints.NORTHEAST;
	c.fill = GridBagConstraints.HORIZONTAL;
	create = new JButton("Create", Commons
		.createImageIcon("images/Open16.gif"));
	create.addActionListener(this);
	create.setActionCommand(CREATE_USERPROFILE_COMMAND_STRING);
	add(create, c);

    }

    private void createShowGUI() {
	frame = new JFrame("SectionCreateGui");
	frame.addWindowListener(new MainFrameCloser(this));
	frame.setSize(300, 300);
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
	protected SectionCreateGui frame = null;

	public MainFrameCloser(SectionCreateGui frame) {
	    super();
	    log.trace("inside MainFrameCloser");
	    this.frame = frame;
	}

	public void windowClosed(WindowEvent e) {
	    log.trace("destroying instance and close the frame");
	    frame.destroyInstance();
	}
    }

    public static SectionCreateGui getInstance() {
	if (singleton == null)
	    singleton = new SectionCreateGui();
	return singleton;
    }

    public void actionPerformed(ActionEvent ex) {

	String command = ex.getActionCommand();
	log.trace("command received " + command);
	if (command.equals(CREATE_USERPROFILE_COMMAND_STRING)) {

	    try {
		if (null == sectionTextField.getText())
		    JOptionPane.showMessageDialog(frame,
			    "Section and cannot be empty");
		else {
		    Factory.getDictionary().writeSectionDatabase(
			    sectionTextField.getText());
		    log.info("created new section");
		    this.frame.dispose();
		}

	    } catch (DatabaseException e) {
		e.printStackTrace();
		log.error("DatabaseException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (UnsupportedEncodingException e) {
		e.printStackTrace();
		log.error("UnsupportedEncodingException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (DataNotFoundException e) {
		e.printStackTrace();
		log.error("DataNotFoundException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (DynamicCursorException e) {
		e.printStackTrace();
		log.error("DynamicCursorException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
		log.error("FileNotFoundException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (KeyNotFoundException e) {
		e.printStackTrace();
		log.error("KeyNotFoundException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (IllegalArgumentException e) {
		e.printStackTrace();
		log.error("IllegalArgumentException " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
		log.error("ClassNotFoundException " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (NoSuchMethodException e) {
		e.printStackTrace();
		log.error("NoSuchMethodException " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (InstantiationException e) {
		e.printStackTrace();
		log.error("InstantiationException " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (IllegalAccessException e) {
		e.printStackTrace();
		log.error("IllegalAccessException " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (InvocationTargetException e) {
		e.printStackTrace();
		log.error("InvocationTargetException " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    }

	}
    }
}
