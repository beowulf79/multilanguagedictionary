package net.verza.jdict.gui;

/**
 * 
 */

/**
 * @author ChristianVerdelli
 *
 */

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import org.apache.log4j.Logger;


public class Window extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;

	private JFrame frame;
	private JMenuBar menuBar;
	private JTabbedPane tabbedPane;

	private final String CREATE_USERPROFILE_STRING = "Create User Profile";
	private final String SHOW_USERPROFILE_STATISTICS = "Show User Quiz Statistics";
	private final String IMPORT_STRING = "Import Data";
	private final String USERPROFILE_IMPORT_STRING = "Import User Profile";
	private final String USERPROFILE_EXPORT_STRING = "Export User Profile";
	
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

		
		
		log = Logger.getLogger("net.verza.jdict.gui");
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
		pack();
		frame.setVisible(true);
	}

	private JMenuBar createMenuBar() {

		JMenuBar mBar = new JMenuBar();

		JMenu mFile = new JMenu("File");
		JMenuItem mImport = new JMenuItem(this.IMPORT_STRING);
		JMenuItem mImportUserProfile = new JMenuItem(this.USERPROFILE_IMPORT_STRING);
		JMenuItem mExportUserProfile = new JMenuItem(this.USERPROFILE_EXPORT_STRING);
		mFile.add(mImport);
		mFile.add(mImportUserProfile);
		mFile.add(mExportUserProfile);
		mImport.addActionListener(this);

		JMenu mUser = new JMenu("User");
		JMenuItem mUserChoose = new JMenuItem(this.CREATE_USERPROFILE_STRING);
		JMenuItem mUserStats = new JMenuItem(this.SHOW_USERPROFILE_STATISTICS);

		mUserChoose.addActionListener(this);
		mUser.add(mUserChoose);
		mUserStats.addActionListener(this);
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



		

		
		public void actionPerformed(ActionEvent evt) {
			String command = evt.getActionCommand();
			
			if (command.equals(this.CREATE_USERPROFILE_STRING)) {
				UserChooseGui.getInstance();
			}
			else if (command.equals(this.SHOW_USERPROFILE_STATISTICS)) {
				UserStatsGui.getInstance();
			}
			else if (command.equals(this.IMPORT_STRING)) {
				DataLoaderGui.getInstance();
			}
			else if (command.equals(this.USERPROFILE_IMPORT_STRING)) {
				DataLoaderGui.getInstance();
			}
			else if (command.equals(this.USERPROFILE_EXPORT_STRING)) {
				DataLoaderGui.getInstance();
			}

		}

	

}
