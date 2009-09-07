package net.verza.jdict.gui;

import com.sleepycat.je.DatabaseException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import net.verza.jdict.UserProfile;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.DataNotFoundException;
import org.apache.log4j.Logger;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UserProfileStatsResetGui extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String RESET_BUTTON_COMMAND_STRING = "Reset User Profile Stats";
	private static final String WARNING_MESSAGE = "Are you sure you want to delete this User Profile ?";
	private static Logger log;
	private static UserProfileStatsResetGui singleton = null;
	private Dictionary dit;
	private JFrame frame;
	private GridBagConstraints c;
	private JButton resetButton;

	public UserProfileStatsResetGui() {

		super();

		try {

			log = Logger.getLogger("net.verza.jdict.gui");
			log.trace("called class " + this.getClass().getName());
			dit = Factory.getDictionary();
			this.initComponents();
			this.createShowGUI();

		} catch (DataNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (DynamicCursorException e) {
			e.printStackTrace();
		}

	}

	private void initComponents() {

		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

		c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1.0;
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;

		JLabel warningJLabel = new JLabel(WARNING_MESSAGE);
		warningJLabel.setBorder(BorderFactory.createLineBorder(
				GUIPreferences.borderColor, GUIPreferences.borderThickness));
		add(warningJLabel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		resetButton = new JButton("Yes", Commons
				.createImageIcon("images/Open16.gif"));
		resetButton.addActionListener(this);
		resetButton.setActionCommand(RESET_BUTTON_COMMAND_STRING);
		add(resetButton, c);

		c.gridx = 1;
		c.gridy = 1;
		JButton cancelJButton = new JButton("No", Commons
				.createImageIcon("images/Open16.gif"));
		cancelJButton.addActionListener(this);
		add(cancelJButton, c);

	}

	private void createShowGUI() {

		frame = new JFrame("UserProfileStatisticsResetGui");
		frame.addWindowListener(new MainFrameCloser(this));
		frame.setSize(500, 100);
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
		protected UserProfileStatsResetGui frame = null;

		public MainFrameCloser(UserProfileStatsResetGui frame) {
			super();
			log.trace("inside MainFrameCloser");
			this.frame = frame;
		}

		public void windowClosing(WindowEvent e) {
			log.trace("destroying instance and close the frame");
			frame.destroyInstance();
			frame.getJFrame().dispose();
		}
		
		public void windowDeactivated(WindowEvent e) {
			log.trace("destroying instance and close the frame");
			frame.destroyInstance();
			frame.getJFrame().dispose();
		}
	}

	private JFrame getJFrame() {
		return frame;
	}

	public static UserProfileStatsResetGui getInstance() {
		if (singleton == null)
			singleton = new UserProfileStatsResetGui();
		return singleton;
	}

	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		log.trace("command received " + command);

		try {
			if (command.equals(RESET_BUTTON_COMMAND_STRING)) {
				UserProfile up = dit.getUser();
				up.flushStatistics();
				dit.writeUserProfile(up);
				frame.dispose();
			} else	{
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
