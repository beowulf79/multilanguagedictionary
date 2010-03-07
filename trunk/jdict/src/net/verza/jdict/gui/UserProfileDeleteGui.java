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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import net.verza.jdict.dictionary.Dictionary;
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

public class UserProfileDeleteGui extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private static final String DELETE_USERPROFILE_COMMAND_STRING = "Delete User Profile";
    private static Logger log;
    private static UserProfileDeleteGui singleton = null;
    private JFrame frame;
    private JButton deleteJButton;
    private Dictionary dit;
    private String[] usernames;
    private JList list;

    public UserProfileDeleteGui() {
	super();
	singleton = this;
	log = Logger.getLogger("jdict");
	log.trace("Initialazing class " + this.getClass().getCanonicalName());
	try {
	    dit = Factory.getDictionary();
	    usernames = dit.getUserList();

	} catch (DatabaseException e) {
	    log.error(e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (UnsupportedEncodingException e) {
	    log.error(e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (DataNotFoundException e) {
	    log.error(e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (DynamicCursorException e) {
	    log.error(e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (FileNotFoundException e) {
	    log.error(e.getMessage());
	    JOptionPane.showMessageDialog(frame, e.getMessage());
	} catch (KeyNotFoundException e) {
	    log.error(e.getMessage());
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
	c.anchor = GridBagConstraints.CENTER;// anchor all components
	// WEST
	c.weightx = 1.0;// all components use vertical available space
	c.weighty = 1.0;// all components use horizontal available space

	// Window label
	c.fill = GridBagConstraints.BOTH;
	c.gridx = 0;
	c.gridy = 0;
	JLabel jlbl1 = new JLabel("Choose User");
	jlbl1.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	add(jlbl1, c);

	c.gridx = 1;
	c.gridy = 0;
	// c.fill = GridBagConstraints.HORIZONTAL;
	// Create the list and put it in a scroll pane.
	list = new JList(usernames);
	// list.setSelectedIndex(0);
	list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	list.setVisibleRowCount(5);
	JScrollPane listScrollPane = new JScrollPane(list);
	add(listScrollPane, c);

	c.gridx = 0;
	c.gridy = 1;
	c.gridwidth = 2;
	c.anchor = GridBagConstraints.CENTER;// anchor all components
	// WEST
	c.fill = GridBagConstraints.HORIZONTAL;
	deleteJButton = new JButton(DELETE_USERPROFILE_COMMAND_STRING, Commons
		.createImageIcon("images/Open16.gif"));
	deleteJButton.addActionListener(this);
	deleteJButton.setActionCommand(DELETE_USERPROFILE_COMMAND_STRING);
	add(deleteJButton, c);

    }

    private void createShowGUI() {

	frame = new JFrame("UserProfileDeleteGui");
	frame.addWindowListener(new MainFrameCloser(this));
	frame.setSize(500, 200);
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
	protected UserProfileDeleteGui frame = null;

	public MainFrameCloser(UserProfileDeleteGui frame) {
	    super();
	    log.trace("inside MainFrameCloser");
	    this.frame = frame;
	}

	public void windowClosed(WindowEvent e) {
	    log.trace("destroying instance and close the frame");
	    frame.destroyInstance();
	}

    }

    public static UserProfileDeleteGui getInstance() {
	if (singleton == null)
	    singleton = new UserProfileDeleteGui();
	return singleton;
    }

    public void actionPerformed(ActionEvent ex) {

	String command = ex.getActionCommand();
	log.trace("command received " + command);

	if (command.equals(DELETE_USERPROFILE_COMMAND_STRING)) {
	    try {
		dit.deleteUserProfile(dit.readUserProfile((String) list
			.getSelectedValue()));
		log.info("deleted user profile");
		Window.getInstance().close();
		frame.dispose();
		UserProfileLoaderGui.getInstance();

	    } catch (DatabaseException e) {
		e.printStackTrace();
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (UnsupportedEncodingException e) {
		e.printStackTrace();
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    }
	} else
	    frame.dispose();

    }

}
