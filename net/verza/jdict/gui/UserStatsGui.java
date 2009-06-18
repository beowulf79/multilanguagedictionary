package net.verza.jdict.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.io.FileNotFoundException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
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

public class UserStatsGui implements ActionListener {

	private static Logger log;
	private JFrame frame;
	private JScrollPane jsp1;
	private static UserStatsGui singleton = null;
	private Dictionary dit;
	public JTextField textFilter;
	public JComboBox typeComboFilter,resultComboFilter;
	private UserStatsGuiJTable table;

	
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



		//JComboBox for Quiz Type Filtering
		c.gridx = 0;
		c.gridy = 1;
		JLabel label = new JLabel("Filter Quiz Type");
		jpnl2.add(label,c);
		c.gridx = 1;
		typeComboFilter = new JComboBox();
		typeComboFilter.addItem("");
		typeComboFilter.addItem("Italian->Egyptian");
		typeComboFilter.addItem("Italian->English");
		typeComboFilter.addItem("Italian->Arabic");
		typeComboFilter.addItem("Arabic->Italian");
		typeComboFilter.addItem("Arabic->Egyptian");
		typeComboFilter.addItem("Arabic->English");
		typeComboFilter.addItem("English->Italian");
		typeComboFilter.addItem("English->Arabic");
		typeComboFilter.addItem("English->Egyptian");
		typeComboFilter.addItem("Egyptian->Italian");
		typeComboFilter.addItem("Egyptian->Arabic");
		typeComboFilter.addItem("Egyptian->English");
		typeComboFilter.setSelectedIndex(0);
		typeComboFilter.setActionCommand("typechanged");
		typeComboFilter.addActionListener(this);
		
		jpnl2.add(typeComboFilter,c);
		
		
		//JComboBox for Quiz Type Filtering
		c.gridx = 0;
		c.gridy = 2;
		JLabel label2 = new JLabel("Filter Quiz Results");
		jpnl2.add(label2,c);
		c.gridx = 1;
		resultComboFilter = new JComboBox();
		resultComboFilter.addItem("");
		resultComboFilter.addItem("correct");
		resultComboFilter.addItem("wrong");
		resultComboFilter.setSelectedIndex(0);
		resultComboFilter.setActionCommand("resultchanged");
		resultComboFilter.addActionListener(this);
		jpnl2.add(resultComboFilter,c);
		
		
		//JTextField for Text Filtering
		c.gridx = 0;
		c.gridy = 3;
		textFilter = new JTextField(20);
		//Whenever filterText changes, invoke newFilter.
		textFilter.getDocument().addDocumentListener(
				new javax.swing.event.DocumentListener() {
					public void changedUpdate(javax.swing.event.DocumentEvent e) {
						System.out.println("changedUpdate");
						newFilter();
					}

					public void insertUpdate(javax.swing.event.DocumentEvent e) {
						System.out.println("insertUpdate");
						newFilter();
					}

					public void removeUpdate(javax.swing.event.DocumentEvent e) {
						System.out.println("removeUpdate");
						newFilter();
					}
				});
		jpnl2.add(textFilter,c);
		
		//JTable for Quiz Results
		c.gridx = 0;
		c.gridy = 4;

		table = new UserStatsGuiJTable(qz.getGuessedHash(),
											qz.getWrongHash(),
												Color.GREEN);
		JScrollPane jsp3 = new JScrollPane(table);
		jpnl2.add(jsp3, c);

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

	public void actionPerformed(ActionEvent evt) {
		if ( (evt.getActionCommand().equals("typechanged")) ||
				(evt.getActionCommand().equals("resultchanged")) )
			newFilter();
	}
	
	
	/** 
	 * Update the row filter regular expression from the expression in
	 * the text box.
	 */
	public void newFilter() {
		
		javax.swing.RowFilter<DefaultTableModel, Object> rf = null;
		javax.swing.RowFilter<DefaultTableModel, Object> rf1 = null;
		javax.swing.RowFilter<DefaultTableModel, Object> rf2 = null;
		javax.swing.RowFilter<DefaultTableModel, Object> rf3 = null;
		//If current expression doesn't parse, don't update.
		try {

			rf1 = javax.swing.RowFilter.regexFilter(textFilter.getText(), 0);
			rf2 = javax.swing.RowFilter.regexFilter((String)typeComboFilter.getSelectedItem(), 2);
			rf3 = javax.swing.RowFilter.regexFilter((String)resultComboFilter.getSelectedItem(), 3);
			
			java.util.ArrayList<javax.swing.RowFilter<DefaultTableModel, Object>> filters = 
				new java.util.ArrayList<javax.swing.RowFilter<DefaultTableModel, Object>>(2);
			
			filters.add(rf1);
			filters.add(rf2);
			filters.add(rf3);
			rf = javax.swing.RowFilter.andFilter(filters);

		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		table.sorter.setRowFilter(rf);
		
	}
	
	
}
