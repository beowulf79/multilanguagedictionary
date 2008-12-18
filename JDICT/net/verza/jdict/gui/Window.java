package net.verza.jdict.gui;

/**
 * 
 */

/**
 * @author ChristianVerdelli
 *
 */

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


import org.apache.log4j.Logger;


public class Window extends JFrame {
	private static final long serialVersionUID = 1L;

	private JFrame frame;
	private WindowAction handler;
	private JMenuBar menuBar;
	private JTabbedPane tabbedPane;

	public static final int FRAME_WIDTH = 400;
	public static final int FRAME_HEIGHT = 420;
	public static final int PANEL_WIDHT = 380;
	public static final int PANEL_HEIGHT = 600;
	public static final int START_X = 200;
	public static final int START_Y = 100;
	private static Logger log;
	private static Window windowManager = null;

	public Window() {

		super("Grid Layout");
		windowManager = this; // initialize single-ton object

		
		
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("Window Constructor");

		
		initComponents();
		createShowGUI();

		// Calling UserChooseGUI to load the User Profile
		UserChooseGui.getInstance();

	}

	private void initComponents() {
		menuBar = createMenuBar();

		tabbedPane = new JTabbedPane();

		/*
		 * Dictionary Tab
		 */
		JPanel dictTab = new DictTabGui();

		dictTab.setBackground(Color.cyan);
		dictTab.setSize(PANEL_WIDHT, 100);
		tabbedPane.addTab("Dictionary Tab", null, dictTab, "Dictionary Tab");

		/*
		 * Quiz Tab
		 */
		JPanel quizTab = new QuizTabGui();
		quizTab.setBackground(Color.cyan);
		quizTab.setSize(PANEL_WIDHT, PANEL_HEIGHT);
		tabbedPane.addTab("Quiz Tab", null, quizTab, "Quiz Tab");

		/*
		 * Verb Tab
		 */
		JPanel verbTab = new VerbTabGui();
		verbTab.setBackground(Color.cyan);
		verbTab.setSize(PANEL_WIDHT, PANEL_HEIGHT);
		tabbedPane.addTab("Verb Tab", null, verbTab, "Verb Tab");
	}

	private void createShowGUI() {
		// Create and set up the window.
		frame = new JFrame("Window");
		frame.setTitle("JDICT 0.6");
		frame.setJMenuBar(menuBar);
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// Create and set up the content pane.
		frame.getContentPane().add(tabbedPane);
	}

	public void showGui() {
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	private JMenuBar createMenuBar() {

		handler = new WindowAction();

		JMenuBar mBar = new JMenuBar();

		JMenu mFile = new JMenu("File");
		JMenuItem mImport = new JMenuItem("Import");
		mFile.add(mImport);

		JMenu mUser = new JMenu("User");
		JMenuItem mUserChoose = new JMenuItem("Load User Profile");
		JMenuItem mUserStats = new JMenuItem("Show User Quiz Statistics");

		mUserChoose.addActionListener(handler);
		mUser.add(mUserChoose);
		mUserStats.addActionListener(handler);
		mUser.add(mUserStats);

		mBar.add(mFile);
		mBar.add(mUser);

		return mBar;
	}

	/*
	 * return singleton Object
	 */
	public static Window getInstance() {
		if (windowManager == null)
			windowManager = new Window();
		return windowManager;
	}

	public class WindowAction implements ActionListener {

		private String command;
		JFileChooser fc;
		File openFile;
		File saveFile;
		UserStatsGui statgui;

		public WindowAction() {

			command = new String();
		}

		public void actionPerformed(ActionEvent evt) {
			command = evt.getActionCommand();

			if (command.equals("Load User Profile")) {

				UserChooseGui.getInstance();
			}
			if (command.equals("Show User Quiz Statistics")) {
				UserStatsGui.getInstance();
			}

		}

	}

}
