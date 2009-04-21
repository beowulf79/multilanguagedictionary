package net.verza.jdict.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.io.FileNotFoundException;
import javax.swing.border.EmptyBorder;

import net.verza.jdict.UserProfile;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.*;
import net.verza.jdict.quiz.QuizStats;

import org.apache.log4j.Logger;
import com.sleepycat.je.DatabaseException;

/**
 * @author ChristianVerdelli
 * 
 */

public class UserStatsGui {

	private static Logger log;
	private JFrame frame;
	private JScrollPane jsp1;
	private static UserStatsGui singleton = null;
	private Dictionary dit;

	public UserStatsGui() {

		super();
		singleton = this;

		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("Initialazing UserStatsGui class");

		UserProfile up = null;
		try {
			dit = Factory.getDictionary();
			up = dit.getUser();

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

		QuizStats qz = new QuizStats(up.getQuizStat());
		qz.computeStats();

		initComponents(qz);
		createAndShowGUI();

	}

	private void initComponents(QuizStats qz) {

		frame = new JFrame();

		JPanel jpnl2 = new JPanel(new BorderLayout());
		jsp1 = new JScrollPane(jpnl2);

		jpnl2.setBackground(Color.orange);
		jpnl2.setLayout(new GridBagLayout());
		jpnl2.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels
																	// gap to
																	// the
																	// borders

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);// add some space between components
											// to avoid clutter
		c.anchor = GridBagConstraints.WEST;// anchor all components WEST
		c.weightx = 1.0;// all components use vertical available space
		c.weighty = 1.0;// all components use horizontal available space
		c.gridheight = 1;
		c.gridwidth = 1;

		// question label
		c.gridx = 0;
		c.gridy = 0;
		JLabel jlbl1 = new JLabel("GUESSED WORDS COUNTER");
		jpnl2.add(jlbl1, c);

		c.gridx = 0;
		c.gridy = 1;

		JPanel table_a = new UserStatsGuiJTable(qz.getGuessedHash(),
				Color.GREEN);
		JScrollPane jsp3 = new JScrollPane(table_a);
		jpnl2.add(jsp3, c);

		c.gridx = 0;
		c.gridy = 2;
		JLabel jlbl2 = new JLabel("FAILED WORDS COUNTER");
		jpnl2.add(jlbl2, c);

		c.gridx = 0;
		c.gridy = 3;
		Color red = new Color(250, 60, 0);
		JPanel table_b = new UserStatsGuiJTable(qz.getWrongHash(), red);
		JScrollPane jsp2 = new JScrollPane(table_b);
		jpnl2.add(jsp2, c);

	}

	private void createAndShowGUI() {
		// Create and set up the window.
		frame = new JFrame("UserStatsGui");
		frame.addWindowListener(new MainFrameCloser(this));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Create and set up the content pane.
		frame.getContentPane().add(jsp1);
		// Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600, 900);
	}

	private void destroyInstance() {
		if (singleton != null)
			singleton = null;
	}

	public static UserStatsGui getInstance() {
		if (singleton == null)
			singleton = new UserStatsGui();
		return singleton;
	}

	private JFrame getJFrame() {
		return frame;
	}

	public class MainFrameCloser extends java.awt.event.WindowAdapter {
		protected UserStatsGui frame = null;

		public MainFrameCloser(UserStatsGui frame) {
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

}
