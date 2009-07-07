package net.verza.jdict.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;

import javax.swing.BorderFactory;
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
	public JComboBox typeComboFilter, resultComboFilter;
	private UserStatsGuiTable table;

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

		JPanel mainPanel = new JPanel(new BorderLayout());
		jsp1 = new JScrollPane(mainPanel);
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2);
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridheight = 1;
		c.gridwidth = 1;

		// JLabel Quiz Type Filtering
		c.gridx = 0;
		c.gridy = 1;
		JLabel quizTypeFilterJLabel = new JLabel("Show ony Quiz Type");
		quizTypeFilterJLabel.setBorder(BorderFactory.createLineBorder(
				GUIPreferences.borderColor, GUIPreferences.borderThickness));
		mainPanel.add(quizTypeFilterJLabel, c);
		// JComboBox for Quiz Type Filtering
		c.gridx = 1;
		c.anchor = GridBagConstraints.EAST;
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
		mainPanel.add(typeComboFilter, c);

		// JLabel Quiz Result Filtering
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.WEST;
		JLabel quizResultFilterJLabel = new JLabel("Show only Results");
		quizResultFilterJLabel.setBorder(BorderFactory.createLineBorder(
				GUIPreferences.borderColor, GUIPreferences.borderThickness));
		mainPanel.add(quizResultFilterJLabel, c);
		// JComboBox Quiz Result Filtering
		c.gridx = 1;
		c.anchor = GridBagConstraints.EAST;
		resultComboFilter = new JComboBox();
		resultComboFilter.addItem("");
		resultComboFilter.addItem("correct");
		resultComboFilter.addItem("wrong");
		resultComboFilter.setSelectedIndex(0);
		resultComboFilter.setActionCommand("resultchanged");
		resultComboFilter.addActionListener(this);
		mainPanel.add(resultComboFilter, c);

		// JLabel for Text Filtering
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.WEST;
		JLabel quizQuestionTextFilterJLabel = new JLabel(
				"Show only results containg the word");
		quizQuestionTextFilterJLabel.setBorder(BorderFactory.createLineBorder(
				GUIPreferences.borderColor, GUIPreferences.borderThickness));
		mainPanel.add(quizQuestionTextFilterJLabel, c);
		// JTextField for Text Filtering
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 1;
		textFilter = new JTextField(10);
		// Whenever filterText changes, invoke newFilter.
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
		mainPanel.add(textFilter, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		table = new UserStatsGuiTable(qz.getResultStatsMap());
		JScrollPane jsp3 = new JScrollPane(table);
		mainPanel.add(jsp3, c);

	}

	private void createAndShowGUI() {
		frame = new JFrame("UserStatsGui");
		frame.addWindowListener(new MainFrameCloser(this));
		frame.setSize(500, 620);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(jsp1);
		frame.setBackground(GUIPreferences.backgroundColor);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

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
		if ((evt.getActionCommand().equals("typechanged"))
				|| (evt.getActionCommand().equals("resultchanged")))
			newFilter();
	}

	/**
	 * Update the row filter regular expression from the expression in the text
	 * box.
	 */
	public void newFilter() {

		javax.swing.RowFilter<DefaultTableModel, Object> rf = null;
		javax.swing.RowFilter<DefaultTableModel, Object> rf1 = null;
		javax.swing.RowFilter<DefaultTableModel, Object> rf2 = null;
		javax.swing.RowFilter<DefaultTableModel, Object> rf3 = null;
		// If current expression doesn't parse, don't update.
		try {

			rf1 = javax.swing.RowFilter.regexFilter(textFilter.getText(), 0);
			rf2 = javax.swing.RowFilter.regexFilter((String) typeComboFilter
					.getSelectedItem(), 2);
			rf3 = javax.swing.RowFilter.regexFilter((String) resultComboFilter
					.getSelectedItem(), 3);

			java.util.ArrayList<javax.swing.RowFilter<DefaultTableModel, Object>> filters = new java.util.ArrayList<javax.swing.RowFilter<DefaultTableModel, Object>>(
					2);

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
