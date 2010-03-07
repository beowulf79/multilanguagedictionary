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

import net.verza.jdict.dictionary.Dictionary;
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

public class UserProfileModifyGui extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String FILE_CHOOSER_COMMAND_STRING = "Select file to Load";
    private static final String MODIFY_USERPROFILE_COMMAND_STRING = "Modify User Profile";
    private static Logger log;
    private static UserProfileModifyGui singleton = null;
    private JTextField nameTextField, emailTextField;
    private JCheckBox roleCheckBox;
    private JPasswordField passwordField;
    private JFileChooser fileChooser;
    private JFrame frame;
    private JButton modifyButton;
    private Dictionary dit;
    private UserProfile userProfile;

    public UserProfileModifyGui() {
	super();
	singleton = this;
	log = Logger.getLogger("jdict");
	log.trace("Initialazing class " + this.getClass().getCanonicalName());
	try {
	    dit = Factory.getDictionary();
	    userProfile = dit.getUser();
	} catch (DatabaseException ex) {
	    log.error(ex.getMessage());
	    JOptionPane.showMessageDialog(frame, ex.getMessage());
	} catch (UnsupportedEncodingException ex) {
	    log.error(ex.getMessage());
	    JOptionPane.showMessageDialog(frame, ex.getMessage());
	} catch (DataNotFoundException ex) {
	    log.error(ex.getMessage());
	    JOptionPane.showMessageDialog(frame, ex.getMessage());
	} catch (DynamicCursorException ex) {
	    log.error(ex.getMessage());
	    JOptionPane.showMessageDialog(frame, ex.getMessage());
	} catch (FileNotFoundException ex) {
	    log.error(ex.getMessage());
	    JOptionPane.showMessageDialog(frame, ex.getMessage());
	} catch (KeyNotFoundException ex) {
	    log.error(ex.getMessage());
	    JOptionPane.showMessageDialog(frame, ex.getMessage());
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
	initComponents();
	createShowGUI();
    }

    private void initComponents() {
	int y = 0;
	setLayout(new GridBagLayout());
	// setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5
	// pixels gap to
	// the borders

	GridBagConstraints c = new GridBagConstraints();
	c.insets = new Insets(2, 2, 2, 2);// add some space between
	// components
	// to avoid clutter
	c.anchor = GridBagConstraints.WEST;// anchor all components
	// WEST
	c.weightx = 1.0;// all components use vertical available space
	c.weighty = 1.0;// all components use horizontal available space
	c.gridheight = 1;
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = y++;
	JLabel nameJLabel = new JLabel("Name");
	nameJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(nameJLabel, c);
	c.gridx = 1;
	nameTextField = new JTextField(dit.getUser().getName());
	nameTextField.setEnabled(false);
	nameTextField.setColumns(20);
	add(nameTextField, c);
	add(new JSeparator(), c);
	c.gridx = 0;
	c.gridy = y++;

	c.gridx = 0;
	c.gridy = y++;
	JLabel passwordJLabel = new JLabel("Password");
	passwordJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(passwordJLabel, c);
	c.gridx = 1;
	passwordField = new JPasswordField(dit.getUser().getPassword());
	passwordField.setColumns(20);
	add(passwordField, c);
	add(new JSeparator(), c);

	c.gridx = 0;
	c.gridy = y++;
	JLabel emailJLabel = new JLabel("email");
	emailJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(emailJLabel, c);
	c.gridx = 1;
	emailTextField = new JTextField(dit.getUser().getEmail());
	emailTextField.setColumns(20);
	add(emailTextField, c);
	add(new JSeparator(), c);

	c.gridx = 0;
	c.gridy = y++;
	JLabel roleLabel = new JLabel("IsAdmin");
	roleLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(roleLabel, c);
	c.gridx = 1;
	roleCheckBox = new JCheckBox();
	if (userProfile.getIsAdmin())
	    roleCheckBox.setSelected(true);
	else {
	    roleCheckBox.setSelected(false);
	    roleCheckBox.setEnabled(false);
	}
	add(roleCheckBox, c);
	add(new JSeparator(), c);

	c.gridx = 0;
	c.gridy = y++;
	add(new JLabel("Picture"), c);
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
	c.gridy = y++;
	c.gridwidth = 2;
	c.fill = GridBagConstraints.HORIZONTAL;
	modifyButton = new JButton("Modify", Commons
		.createImageIcon("images/Open16.gif"));
	modifyButton.addActionListener(this);
	modifyButton.setActionCommand(MODIFY_USERPROFILE_COMMAND_STRING);
	add(modifyButton, c);

    }

    private void createShowGUI() {
	frame = new JFrame("UserProfileModifyGui");
	frame.addWindowListener(new MainFrameCloser(this));
	frame.setSize(380, 300);
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
	protected UserProfileModifyGui frame = null;

	public MainFrameCloser(UserProfileModifyGui frame) {
	    super();
	    log.trace("inside MainFrameCloser");
	    this.frame = frame;
	}

	public void windowClosed(WindowEvent e) {
	    log.trace("destroying instance and close the frame");
	    frame.destroyInstance();
	}
    }

    public static UserProfileModifyGui getInstance() {
	if (singleton == null)
	    singleton = new UserProfileModifyGui();
	return singleton;
    }

    public void actionPerformed(ActionEvent ex) {

	String command = ex.getActionCommand();
	log.trace("command received " + command);
	if (command.equals(FILE_CHOOSER_COMMAND_STRING)) {
	    fileChooser.showOpenDialog(UserProfileModifyGui.this);
	} else if (command.equals(MODIFY_USERPROFILE_COMMAND_STRING)) {

	    try {
		userProfile = dit.getUser();
		userProfile.setEmail(emailTextField.getText());
		userProfile.setPassword(new String((char[]) passwordField
			.getPassword()));

		if (roleCheckBox.isSelected())
		    userProfile.setIsAdmin(true);
		else
		    userProfile.setIsAdmin(false);

		if (fileChooser.getSelectedFile() != null)
		    userProfile.setPicture(fileChooser.getSelectedFile());
		Factory.getDictionary().writeUserProfile(userProfile);
		log.info("modified user profile");

	    } catch (DatabaseException e) {
		log.error("DatabaseException " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (UnsupportedEncodingException e) {
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (DataNotFoundException e) {
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (DynamicCursorException e) {
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (FileNotFoundException e) {
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(frame, e.getMessage());
	    } catch (KeyNotFoundException e) {
		log.error("Exception " + e.getMessage());
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
    }

}
