package net.verza.jdict.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import net.verza.jdict.UserProfile;
import net.verza.jdict.exceptions.*;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.dictionary.Dictionary;
import org.apache.log4j.Logger;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
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
	private JCheckBox	roleCheckBox;
	private JPasswordField passwordField;
	private JFileChooser fileChooser;
	private JFrame frame;
	private JButton modifyButton;
	private Dictionary dit;
	private UserProfile userProfile;
	
	
	public UserProfileModifyGui() {
		super();
		singleton = this;
		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("Initialazing class " + this.getClass().getCanonicalName());
		try {
			dit = Factory.getDictionary();
			userProfile = dit.getUser();
		} catch (DatabaseException ex) {
			System.err.println();
		} catch (UnsupportedEncodingException ex) {
			System.err.println();
		} catch (DataNotFoundException ex) {
			System.err.println();
		} catch (DynamicCursorException ex) {
			System.err.println();
		} catch (FileNotFoundException ex) {
			System.err.println();
		}
		initComponents();
		createShowGUI();
	}

	private void initComponents() {
		int y = 0;
		setLayout(new GridBagLayout());
		// setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels gap to
		// the borders

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);// add some space between components
		// to avoid clutter
		c.anchor = GridBagConstraints.WEST;// anchor all components WEST
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
		if(userProfile.getIsAdmin())
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

		public void windowClosing(WindowEvent e) {
			log.trace("destroying instance and close the frame");
			frame.destroyInstance();
			frame.getJFrame().dispose();
		}
	}

	private JFrame getJFrame() {
		return frame;
	}

	public static UserProfileModifyGui getInstance() {
		if (singleton == null)
			singleton = new UserProfileModifyGui();
		return singleton;
	}

	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		log.trace("command received " + command);
		if (command.equals(FILE_CHOOSER_COMMAND_STRING)) {
			fileChooser.showOpenDialog(UserProfileModifyGui.this);
		} else if (command.equals(MODIFY_USERPROFILE_COMMAND_STRING)) {
			
			try {
				userProfile = dit.getUser();
				userProfile.setEmail(emailTextField.getText());
				userProfile.setPassword(new String((char[]) passwordField
						.getPassword()));
				
				if(roleCheckBox.isSelected())
					userProfile.setIsAdmin(true);
				else userProfile.setIsAdmin(false);
				
				if (fileChooser.getSelectedFile() != null)
					userProfile.setPicture(fileChooser.getSelectedFile());
				Factory.getDictionary().writeUserProfile(userProfile);
				
			} catch (DatabaseException ex) {
				System.err.println();
			} catch (UnsupportedEncodingException ex) {
				System.err.println();
			} catch (DataNotFoundException ex) {
				System.err.println();
			} catch (DynamicCursorException ex) {
				System.err.println();
			} catch (FileNotFoundException ex) {
				System.err.println();
			}

		}
	}
}
