package net.verza.jdict.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;
import com.sleepycat.je.DatabaseException;
import javax.swing.BorderFactory;

/**
 * @author ChristianVerdelli
 * 
 */

public class UserProfileLoaderGui extends JPanel implements ActionListener,
		ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private static final String LOAD_USERPROFILE_COMMAND_STRING = "Load User Profile";
	private static Logger log;
	private static UserProfileLoaderGui singleton = null;

	private JFrame frame;
	private JList list;
	private JPanel jpnl1;
	private JButton selectButton;
	private JPasswordField jpwd;
	private GridBagConstraints c = new GridBagConstraints();
	private String[] usernames;
	private Dictionary dit;
	private JLabel myLabel;

	public UserProfileLoaderGui() {
		super();

		singleton = this;
		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("Initialazing UserChooseGui class");

		try {

			dit = Factory.getDictionary();
			usernames = dit.getUserList();

		} catch (DatabaseException e) {
			System.err.println(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (DataNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (DynamicCursorException e) {
			System.err.println(e.getMessage());
		}

		initComponents();
		createAndShowGUI();
	}

	private void initComponents() {

		jpnl1 = new JPanel(new GridBagLayout());
		jpnl1.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels
		// gap to
		// the
		// borders
		jpnl1.setBackground(GUIPreferences.backgroundColor);

		c.insets = new Insets(2, 2, 2, 2);// add some space between components
		// to avoid clutter
		c.anchor = GridBagConstraints.WEST;// anchor all components WEST
		c.weightx = 0.1;// all components use vertical available space
		c.weighty = 0.1;// all components use horizontal available space
		c.gridheight = 1;
		c.gridwidth = 1;

		// Window label
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		JLabel jlbl1 = new JLabel("Choose User");
		jlbl1.setBorder(BorderFactory.createLineBorder(
				GUIPreferences.borderColor, GUIPreferences.borderThickness));
		jpnl1.add(jlbl1, c);

		c.gridx = 1;
		c.gridy = 0;
		// c.fill = GridBagConstraints.HORIZONTAL;
		// Create the list and put it in a scroll pane.
		list = new JList(usernames);
		// list.setSelectedIndex(0);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(5);
		list.addListSelectionListener(this);
		JScrollPane listScrollPane = new JScrollPane(list);
		jpnl1.add(listScrollPane, c);

		// insert Password label
		c.gridx = 0;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		JLabel jlbl2 = new JLabel("Password");
		jlbl2.setBorder(BorderFactory.createLineBorder(
				GUIPreferences.borderColor, GUIPreferences.borderThickness));
		jpnl1.add(jlbl2, c);

		c.gridx = 1;
		jpwd = new JPasswordField();
		jpwd.setColumns(10);
		jpwd.setEchoChar('X');
		jpnl1.add(jpwd, c);

		c.gridx = 0;
		c.gridy = 4;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		selectButton = new JButton(LOAD_USERPROFILE_COMMAND_STRING);
		selectButton.addActionListener(this);
		selectButton.setEnabled(true);
		jpnl1.add(selectButton, c);

	}

	public JPanel getExternalPanel() {
		return jpnl1;
	}

	public static UserProfileLoaderGui getInstance() {
		if (singleton == null)
			singleton = new UserProfileLoaderGui();
		return singleton;
	}

	private void destroyInstance() {
		if (singleton != null)
			singleton = null;
	}

	public class MainFrameCloser extends java.awt.event.WindowAdapter {
		protected UserProfileLoaderGui frame = null;

		public MainFrameCloser(UserProfileLoaderGui frame) {
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

	private void createAndShowGUI() {
		frame = new JFrame("UserProfileLoaderGui");
		frame.addWindowListener(new MainFrameCloser(this));
		frame.setSize(300, 370);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(jpnl1);
		frame.setBackground(GUIPreferences.backgroundColor);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void valueChanged(ListSelectionEvent e) {
		// get the user from the list
		String username = new String((String) list.getSelectedValue());

		try {
			// set the user profile in the dictionary for this session
			dit.setUser(username);

			if (myLabel != null)
				jpnl1.remove(myLabel);
			myLabel = new JLabel();
			ImageIcon myImage = dit.getUser().getPicture();
			myLabel.setIcon(myImage);
			c.gridy = 2;
			c.gridx = 1;
			jpnl1.add(myLabel, c);
			frame.setVisible(true);

		} catch (DatabaseException en) {
			System.err.println(en.getMessage());
		} catch (UnsupportedEncodingException en) {
			System.err.println(en.getMessage());
		}
	}

	public void actionPerformed(ActionEvent evt) {

		try {

			// get the user from the list
			String username = new String((String) list.getSelectedValue());
			// set the user profile in the dictionary for this session
			dit.setUser(username);

			// Check Password
			String pwd = new String((char[]) jpwd.getPassword());

			if (pwd.trim().length() == 0) {
				log.error("empty password, authentication failed ");
				// Password field empty
				JOptionPane.showMessageDialog(null, "Password not valid !");
			} else if (dit.getUser().checkPassword(pwd) != 0) {
				log.error("wrong password, authentication failed ");
				// Wrong Password
				// let's open JDialog with the error message
				JOptionPane.showMessageDialog(null, "Wrong Password!");
				jpwd.setText("");
				pwd = null;

			} else {

				// Password OK
				// let's open the main Frame from here
				log.info("successfull authentication");
				Window.getInstance().showGui();
				// let's delete this instance so that other calls to this
				// class will reopen the frame
				destroyInstance();
				// close the Window
				frame.dispose();
			}

		} catch (DatabaseException e) {
			System.err.println(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		}

	}

}
