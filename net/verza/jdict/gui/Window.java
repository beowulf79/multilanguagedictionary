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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import org.apache.log4j.Logger;
import com.sleepycat.je.DatabaseException;

public class Window extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JFrame frame;
	private JMenuBar menuBar;
	private JTabbedPane tabbedPane;

	private static final String CREATE_USERPROFILE = "Create User Profile";
	private static final String LOAD_USERPROFILE = "Load User Profile";
	private static final String MODIFY_USERPROFILE = "Modify User Profile";
	private static final String DELETE_USERPROFILE = "Delete User Profile";
	private static final String IMPORT_USERPROFILE_STATS = "Import User Profile Statistics";
	private static final String EXPORT_USERPROFILE_STATS = "Export User Profile Statistics";
	private static final String RESET_USERPROFILE_STATS = "Reset User Profile Statistics";
	private static final String SHOW_USERPROFILE_STATISTICS = "Show User Quiz Statistics";
	private static final String DATA_IMPORT = "Import Data";

	public static final int FRAME_WIDTH = 400;
	public static final int FRAME_HEIGHT = 420;
	public static final int PANEL_WIDHT = 380;
	public static final int PANEL_HEIGHT = 600;
	public static final int START_X = 200;
	public static final int START_Y = 100;
	private static Logger log;
	private static Window windowManager = null;

	public Window() {
		super();
		windowManager = this; // initialize single-ton object
		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("Window Constructor");
		initComponents();
	}

	private void initComponents() {
		menuBar = createMenuBar();

		tabbedPane = new JTabbedPane();

		/*
		 * Dictionary Tab
		 */
		JPanel dictTab = new DictTabGui();
		dictTab.setBackground(GUIPreferences.backgroundColor);
		dictTab.setSize(PANEL_WIDHT, 100);
		tabbedPane.addTab("Dictionary Tab", null, dictTab, "Dictionary Tab");

		/*
		 * Quiz Tab
		 */
		JPanel quizTab = new QuizTabGui();
		quizTab.setBackground(GUIPreferences.backgroundColor);
		quizTab.setSize(PANEL_WIDHT, PANEL_HEIGHT);
		tabbedPane.addTab("Quiz Tab", null, quizTab, "Quiz Tab");

		/*
		 * Verb Tab
		 */
		JPanel verbTab = new VerbTabGui();
		verbTab.setBackground(GUIPreferences.backgroundColor);
		verbTab.setSize(PANEL_WIDHT, PANEL_HEIGHT);
		tabbedPane.addTab("Verb Tab", null, verbTab, "Verb Tab");
		
	}

	private void createShowGUI() {
		// Create and set up the window.
		setTitle("JDICT 0.6");
		setJMenuBar(menuBar);
		setSize(1000, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(true);
		getContentPane().add(tabbedPane);
	}

	public void showGui() {
		createShowGUI();
		// Display the window.
		pack();
		setVisible(true);
	}

	public void close()	{
		windowManager = null;
		dispose();
	}
	
	private JMenuBar createMenuBar() {

		JMenuBar mBar = new JMenuBar();

		JMenu mFile = new JMenu("File");
		JMenuItem mImport = new JMenuItem(DATA_IMPORT);
		mFile.add(mImport);
		mImport.addActionListener(this);

		JMenu mUserProfileMenu = new JMenu("User");
		JMenuItem mUserProfileCreate = new JMenuItem(CREATE_USERPROFILE);
		mUserProfileCreate.addActionListener(this);
		mUserProfileMenu.add(mUserProfileCreate);
		
		JMenuItem mUserProfileLoad = new JMenuItem(LOAD_USERPROFILE);
		mUserProfileLoad.addActionListener(this);
		mUserProfileMenu.add(mUserProfileLoad);
		
		JMenuItem mUserProfileModify = new JMenuItem(MODIFY_USERPROFILE);
		mUserProfileModify.addActionListener(this);
		mUserProfileMenu.add(mUserProfileModify);
		
		JMenuItem mUserProfileDelete = new JMenuItem(DELETE_USERPROFILE);
		mUserProfileDelete.addActionListener(this);

		try {

			if(Factory.getDictionary().getUser().getIsAdmin() )	
				mUserProfileMenu.add(mUserProfileDelete);			
			
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
		
		JMenuItem mImportUserProfile = new JMenuItem(
				IMPORT_USERPROFILE_STATS);
		mUserProfileMenu.add(mImportUserProfile);
		mImportUserProfile.addActionListener(this);
		
		JMenuItem mExportUserProfile = new JMenuItem(
				EXPORT_USERPROFILE_STATS);
		mUserProfileMenu.add(mExportUserProfile);
		mExportUserProfile.addActionListener(this);
		
		JMenuItem mResetUserProfile = new JMenuItem(
				RESET_USERPROFILE_STATS);
		mUserProfileMenu.add(mResetUserProfile);
		mResetUserProfile.addActionListener(this);
		
		JMenuItem mUserProfileShowStats = new JMenuItem(SHOW_USERPROFILE_STATISTICS);
		mUserProfileShowStats.addActionListener(this);
		mUserProfileMenu.add(mUserProfileShowStats);

		mBar.add(mFile);
		mBar.add(mUserProfileMenu);

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

		if (command.equals(CREATE_USERPROFILE)) {
			UserProfileCreateGui.getInstance();
		} else if (command.equals(LOAD_USERPROFILE)) {
			UserProfileLoaderGui.getInstance();
		} else if (command.equals(MODIFY_USERPROFILE)) {
			UserProfileModifyGui.getInstance();
		} else if (command.equals(DELETE_USERPROFILE)) {
			UserProfileDeleteGui.getInstance();
		} else if (command.equals(IMPORT_USERPROFILE_STATS)) {
			UserProfileStatsImporterGui.getInstance();
		} else if (command.equals(EXPORT_USERPROFILE_STATS)) {
			UserProfileStatsExporterGui.getInstance();
		} else if (command.equals(RESET_USERPROFILE_STATS)) {
			UserProfileStatsResetGui.getInstance();
		} else if (command.equals(SHOW_USERPROFILE_STATISTICS)) {
			UserStatsGui.getInstance();
		} else if (command.equals(DATA_IMPORT)) {
			DataLoaderGui.getInstance();
		}

	}

}
