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
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.model.UserProfile;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

/**
 * @author ChristianVerdelli
 * 
 */

public class UserProfileCreateGui extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String FILE_CHOOSER_COMMAND_STRING = "Select file to Load";
    private static final String CREATE_USERPROFILE_COMMAND_STRING = "Create User Profile";
    private static Logger log;
    private static UserProfileCreateGui singleton = null;
    private JCheckBox isAdminJCheckBox;
    private JTextField nameTextField, emailTextField;
    private JPasswordField passwordField;
    private JFileChooser fileChooser;
    private JFrame frame;
    private JButton create;
    private int y_position = 0;

    public UserProfileCreateGui() {
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
	JLabel nameJLabel = new JLabel("username");
	nameJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(nameJLabel, c);
	c.gridx = 1;
	c.anchor = GridBagConstraints.NORTHEAST;
	nameTextField = new JTextField(15);
	add(nameTextField, c);
	add(new JSeparator(), c);

	c.gridx = 0;
	c.gridy = y_position++;
	c.anchor = GridBagConstraints.NORTHWEST;
	JLabel passwordLabel = new JLabel("password");
	passwordLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(passwordLabel, c);
	c.gridx = 1;
	c.anchor = GridBagConstraints.NORTHEAST;
	passwordField = new JPasswordField(15);
	add(passwordField, c);
	add(new JSeparator(), c);

	c.gridx = 0;
	c.gridy = y_position++;
	c.anchor = GridBagConstraints.NORTHWEST;
	JLabel emailJLabel = new JLabel("email");
	emailJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(emailJLabel, c);
	c.gridx = 1;
	c.anchor = GridBagConstraints.NORTHEAST;
	add(emailTextField = new JTextField(15), c);
	add(new JSeparator(), c);

	try {
	    if (Factory.getDictionary().getUser().getIsAdmin()) {
		c.gridx = 0;
		c.gridy = y_position++;
		c.anchor = GridBagConstraints.NORTHWEST;
		JLabel isAdminJLabel = new JLabel("isAdmin");
		isAdminJLabel.setBorder(BorderFactory.createLineBorder(
			GUIPreferences.borderColor,
			GUIPreferences.borderThickness));
		add(isAdminJLabel, c);
		c.gridx = 1;
		isAdminJCheckBox = new JCheckBox();
		isAdminJCheckBox.setSelected(false);
		add(isAdminJCheckBox, c);
	    }
	} catch (DatabaseException e) {
	    e.printStackTrace();
	    log.error("DatabaseException " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DataNotFoundException e) {
	    e.printStackTrace();
	    log.error("DataNotFoundException " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    log.error("FileNotFoundException " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	    log.error("UnsupportedEncodingException " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DynamicCursorException e) {
	    e.printStackTrace();
	    log.error("DynamicCursorException " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (KeyNotFoundException e) {
	    e.printStackTrace();
	    log.error("KeyNotFoundException " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
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

	c.gridx = 0;
	c.gridy = y_position++;
	c.anchor = GridBagConstraints.NORTHWEST;
	JLabel pictureJLabel = new JLabel("Picture");
	pictureJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(pictureJLabel, c);
	c.gridx = 1;
	fileChooser = new JFileChooser();
	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	JButton openButton = new JButton("Select file to Load", Commons
		.createImageIcon("images/Open16.gif"));
	openButton.addActionListener(this);
	openButton.setActionCommand(FILE_CHOOSER_COMMAND_STRING);
	add(openButton, c);
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
	frame = new JFrame("UserProfileCreateGui");
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
	protected UserProfileCreateGui frame = null;

	public MainFrameCloser(UserProfileCreateGui frame) {
	    super();
	    log.trace("inside MainFrameCloser");
	    this.frame = frame;
	}

	public void windowClosed(WindowEvent e) {
	    log.trace("destroying instance and close the frame");
	    frame.destroyInstance();
	}
    }

    public static UserProfileCreateGui getInstance() {
	if (singleton == null)
	    singleton = new UserProfileCreateGui();
	return singleton;
    }

    public void actionPerformed(ActionEvent ex) {

	String command = ex.getActionCommand();
	log.trace("command received " + command);
	if (command.equals(FILE_CHOOSER_COMMAND_STRING)) {
	    if (fileChooser.showOpenDialog(UserProfileCreateGui.this) == JFileChooser.APPROVE_OPTION) {

	    }
	} else if (command.equals(CREATE_USERPROFILE_COMMAND_STRING)) {

	    try {
		UserProfile userProfile = new UserProfile();
		userProfile.setName(nameTextField.getText());
		userProfile.setPassword(new String((char[]) passwordField
			.getPassword()));
		if (null != emailTextField.getText())
		    userProfile.setEmail(emailTextField.getText());
		if (null != fileChooser.getSelectedFile())
		    userProfile.setPicture(fileChooser.getSelectedFile());

		if (Factory.getDictionary().getUser().getIsAdmin())
		    if (isAdminJCheckBox.isSelected())
			userProfile.setIsAdmin(true);

		Factory.getDictionary().writeUserProfile(userProfile);
		log.info("created new user profile");

		this.frame.dispose();

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
