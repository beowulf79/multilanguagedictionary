package net.verza.jdict.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.verza.jdict.UserProfile;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LinkIDException;
import net.verza.jdict.quiz.QuizAbstract;
import org.apache.log4j.Logger;
import com.sleepycat.je.DatabaseException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author Christian Verdelli
 */

public class QuizTabGui extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String questionLanguageLabel = "question language";
	private static final String answerLanguageLabel = " answer language";	
	public static final int DEFAULT_ARGUMENT_INDEX = 0;
	public static final int DEFAULT_ITERATIONS = 10;
	public static final String NOT_SELECTED_STRING = "---- Nothing Selected ----";
	public static final String TYPE_SELECTION_COMBO_ACTION_STRING = "typesel";
	public static Logger log;
	public static String[] languageArray;
	public static QuizTabGui instance = null;
	public QuizTabGuiWordsCounter wcounter;
	public GridBagConstraints c;
	public JTextArea jtxa1;
	public JLabel jlb1;
	public JLabel jlb2;
	public JLabel jlb3;
	public JLabel jlb4;
	public JScrollPane scrlp1;
	public JButton jbt1;
	public JComboBox quizTypeSelectionJComboBox;
	public JComboBox categorySelectionJComboBox;
	public JComboBox sectionSelectionJComboBox;
	public JSpinner iterationJSpinner;
	public QuizAbstract quiz;
	public Dictionary dit;
	public int iterations;
	public String src_lang, dst_lang;
	private HashMap<String, String[]> translations;
	public JComboBox srcLangCombo, dstLangCombo;

	public QuizTabGui() {

		super();
		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("called class " + this.getClass().getName());
		quiz = null;
		instance = this;

		try {
			dit = Factory.getDictionary();
			wcounter = new QuizTabGuiWordsCounter();

		} catch (DatabaseException e) {
			log.error(e.toString());
		} catch (FileNotFoundException e) {
			log.error(e.toString());
		} catch (UnsupportedEncodingException e) {
			log.error(e.toString());
		} catch (DynamicCursorException e) {
			e.printStackTrace();
		} catch (DataNotFoundException e) {
			e.printStackTrace();
		}

		initComponents(); // initialize components graphich

	}

	/*
	 * Costruisce il menu JComboBox di scelta del quiz. Ottiene i linguaggi
	 * definiti nell'oggetto LanguageConfiguration, e per ciascuno essi se
	 * abilitato, ottiene i quiz disponibili e classi che li gestiscono; per
	 * ogni quiz verifica inoltre che il lingugaggio 'target' sia abilitata;
	 */
	private void buildQuizMenu() {

		translations = LanguageSelection.buildLanguageMenu();

		//JLabel
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		JLabel questionLanguageJLabel = new JLabel(questionLanguageLabel);
		questionLanguageJLabel.setBorder(BorderFactory.createLineBorder(GUIPreferences.borderColor));
		add(questionLanguageJLabel, c);
		
		//  JComboBox to choose the languages of the lookup
		c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 2;
		srcLangCombo = new JComboBox(translations.keySet().toArray());
		srcLangCombo.setActionCommand("src_lang_selection_change");
		srcLangCombo.addItem(NOT_SELECTED_STRING);
		srcLangCombo.setSelectedIndex(srcLangCombo.getItemCount() - 1);
		srcLangCombo.addActionListener(this);
		add(srcLangCombo, c);
	}

	private void initComponents() {

		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels gap to
		// the borders

		c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2); // add some space between components
		// to avoid clutter
		c.weightx = 0.1; // all components use vertical available space
		c.weighty = 0; // the components WILL NOT use horizontal available space

		buildQuizMenu(); // builds the JComboBox

		c.gridx = 0;
		c.gridy = 5;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		jlb3 = new JLabel("Iterations");
		jlb3.setBorder(BorderFactory.createLineBorder(GUIPreferences.borderColor, GUIPreferences.borderThickness));
		add(jlb3, c);

		c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 2;
		iterationJSpinner = new JSpinner();
		iterationJSpinner.setValue(DEFAULT_ITERATIONS);
		add(iterationJSpinner, c);

		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		jbt1 = new JButton("Start");
		jbt1.addActionListener(this);
		add(jbt1, c);

		c.gridx = 0;
		c.gridy = 7;
		c.gridheight = 5;
		c.gridwidth = 4;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.weighty = 1; // this component will use horizontal available space
		c.fill = GridBagConstraints.BOTH;
		jtxa1 = new JTextArea();
		jtxa1.setColumns(20);
		jtxa1.setLineWrap(true);
		jtxa1.setRows(5);
		jtxa1.setWrapStyleWord(true);
		jtxa1.setEditable(false);
		scrlp1 = new JScrollPane(jtxa1);
		add(scrlp1, c);
	}

	private String[] readCategory() {
		log.debug("building category combo with values "
				+ dit.getCategoryValue());
		return dit.getCategoryValue();
	}

	private String[] readSection() {
		log
				.debug("building section combo with values "
						+ dit.getSectionValue());
		return dit.getSectionValue();
	}

	public void showResults() {
		try {

			QuizResultTable resultTable = new QuizResultTable(quiz.getStats());
			c.gridx = 0;
			c.gridy = 7;
			c.gridheight = 6;
			c.gridwidth = 3;
			c.weighty = 0.1; // all components use horizontal available space
			c.fill = GridBagConstraints.BOTH;
			scrlp1.remove(jtxa1);
			remove(scrlp1);
			scrlp1 = new JScrollPane(resultTable);
			scrlp1.getViewport().setBackground(Color.RED);
			add(scrlp1, c);
			Window.getInstance().repaint();

			// updating UserProfile object with generated QuizResult objecj
			UserProfile up = dit.getUser();
			up.addQuizStat(quiz.getStats());
			log.debug("updating profile of user " + up.toString());
			dit.writeUserProfile(up);
		} catch (UnsupportedEncodingException e) {
			log.error("UnsupportedEncodingException " + e.getMessage());
			e.printStackTrace();
		} catch (DatabaseException e) {
			log.error("DatabaseException " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void actionPerformed(ActionEvent evt) {

		String command = evt.getActionCommand();
		try {

			// add JComboBox to choose the languages of the lookup
			if (command.equals("src_lang_selection_change")) {
				
				JLabel dst = new JLabel(answerLanguageLabel);
				dst.setBorder(BorderFactory.createLineBorder(GUIPreferences.borderColor, GUIPreferences.borderThickness));
				if (this.srcLangCombo.getSelectedItem().equals(
						NOT_SELECTED_STRING)) {
					remove(this.dstLangCombo);
					remove(dst);
				} else {
					if (this.dstLangCombo != null)
						remove(this.dstLangCombo);
					c.weighty = 0; // all components use horizontal available space
					c.gridheight = 1;
					c.gridwidth = 1;
					c.gridx = 0;
					c.gridy = 1;
					c.anchor = GridBagConstraints.NORTHWEST;
					c.fill = GridBagConstraints.NONE;
					c.gridwidth = 1;
					add(dst, c);						//JLabel
					
					c.gridx = 2;
					c.anchor = GridBagConstraints.NORTHEAST;
					this.dstLangCombo = new JComboBox(this.translations
							.get(this.srcLangCombo.getSelectedItem()));
					dstLangCombo.addItem(NOT_SELECTED_STRING);
					dstLangCombo.setActionCommand("dst_lang_selection_change");
					dstLangCombo
							.setSelectedIndex(dstLangCombo.getItemCount() - 1);
					dstLangCombo.addActionListener(this);
					add(this.dstLangCombo, c);						// JComboBox
						
					this.revalidate();
					setVisible(true);
				}
			} else if (command.equals("dst_lang_selection_change")) {
				if (this.dstLangCombo.getSelectedItem().toString().equals(
						"audio"))
					wcounter.setLanguage(this.srcLangCombo.getSelectedItem()
							.toString());
				else
					wcounter.setLanguage(this.srcLangCombo.getSelectedItem()
							.toString());

				c.gridx = 0;	c.gridy = 2;	c.gridheight = 1;	c.gridwidth = 1; c.weighty = 0;
				c.fill = GridBagConstraints.NONE; 	c.anchor = GridBagConstraints.NORTHWEST;
				jlb4 = new JLabel("Section");
				jlb4.setBorder(BorderFactory.createLineBorder(GUIPreferences.borderColor, GUIPreferences.borderThickness));
				add(jlb4, c);
				// get section values counter
				wcounter.setIndex("section");
				wcounter.setInputData(readSection());
				if (sectionSelectionJComboBox != null)
					remove(sectionSelectionJComboBox);
				
				c.gridx = 2; 	c.fill = GridBagConstraints.NONE; c.anchor = GridBagConstraints.NORTHEAST;
				sectionSelectionJComboBox = new JComboBox(wcounter
						.search("section"));
				sectionSelectionJComboBox.addItem(NOT_SELECTED_STRING);
				sectionSelectionJComboBox
						.setSelectedIndex(sectionSelectionJComboBox
								.getItemCount() - 1);
				add(sectionSelectionJComboBox, c);

				// Verbs do not have category index
				if ((this.srcLangCombo.getSelectedItem().toString().indexOf(
						"verb") == -1)
						&& (this.dstLangCombo.getSelectedItem().toString()
								.indexOf("verb") == -1)) {
					
					c.gridx = 0; 	c.gridy = 3; 	c.weighty = 0;	c.fill = GridBagConstraints.NONE;		c.anchor = GridBagConstraints.NORTHWEST;
					jlb2 = new JLabel("Argument/Category");
					jlb2.setBorder(BorderFactory.createLineBorder(GUIPreferences.borderColor, GUIPreferences.borderThickness));
					add(jlb2, c);
					// get section values counter
					wcounter.setIndex("category");
					wcounter.setInputData(readCategory());
					if (categorySelectionJComboBox != null)
						remove(categorySelectionJComboBox);
					c.gridx = 2;
					c.anchor = GridBagConstraints.NORTHEAST;
					categorySelectionJComboBox = new JComboBox(wcounter
							.search("category"));
					categorySelectionJComboBox.addItem(NOT_SELECTED_STRING);
					categorySelectionJComboBox
							.setSelectedIndex(categorySelectionJComboBox
									.getItemCount() - 1);
					add(categorySelectionJComboBox, c);
				}

				this.revalidate();
				setVisible(true);

			} else if (command.equals("Start")) {

				String quizStringClassToLoad;
				if (this.dstLangCombo.getSelectedItem().toString().equals(
						"audio")) {
					quizStringClassToLoad = new String().concat("audio" + "2"
							+ this.srcLangCombo.getSelectedItem().toString());
					System.out.println("audio " + quizStringClassToLoad);
				} else {
					quizStringClassToLoad = new String()
							.concat(this.srcLangCombo.getSelectedItem()
									.toString()
									+ "2"
									+ this.dstLangCombo.getSelectedItem()
											.toString());
					System.out.println("else " + quizStringClassToLoad);
				}

				System.out
						.println("class going to be loaded: net.verza.jdict.quiz."
								+ quizStringClassToLoad);

				// load the class that will handle the Quiz
				Class<?> toRun = Class.forName("net.verza.jdict.quiz."
						+ quizStringClassToLoad);
				log.debug("class going to be loaded: net.verza.jdict.quiz."
						+ quizStringClassToLoad);
				Class<?>[] c_arr = new Class[] {};
				Constructor<?> constr = toRun.getConstructor(c_arr);
				quiz = (QuizAbstract) constr.newInstance();
				iterations = new Integer((Integer) iterationJSpinner.getValue());
				quiz.setIterations(iterations);

				// Category Combo Box
				if (categorySelectionJComboBox != null) {
					String category = (String) categorySelectionJComboBox
							.getSelectedItem();
					if (!category.equals(NOT_SELECTED_STRING))
						quiz.setCategoryIndex(category, this.srcLangCombo
								.getSelectedItem().toString());
				}

				// Section Combo Box
				String section = (String) sectionSelectionJComboBox
						.getSelectedItem();
				if (!section.equals(NOT_SELECTED_STRING)) {
					quiz.setSectionIndex(section, this.srcLangCombo
							.getSelectedItem().toString());
				}
				quiz.load();
				new QuizInputGui(instance);

			}

		} catch (ClassNotFoundException e) {
			JOptionPane
					.showMessageDialog(null,
							"A ClassNotFoundException has occurred, please check the logs");
			log.error("ClassNotFoundException: " + e.getMessage());
		} catch (NoSuchMethodException e) {
			JOptionPane
					.showMessageDialog(null,
							"A NoSuchMethodException has occurred, please check the logs");
			log.error("NoSuchMethodException: " + e.getMessage());
		} catch (InstantiationException e) {
			JOptionPane
					.showMessageDialog(null,
							"A InstantiationException has occurred, please check the logs");
			log.error("InstantiationException: " + e.getMessage());
		} catch (IllegalAccessException e) {
			JOptionPane
					.showMessageDialog(null,
							"A IllegalAccessException has occurred, please check the logs");
			log.error("IllegalAccessException: " + e.getMessage());
		} catch (InvocationTargetException e) {
			JOptionPane
					.showMessageDialog(null,
							"A InvocationTargetException has occurred, please check the logs");
			log.error("InvocationTargetException: " + e.getMessage());
		} catch (FileNotFoundException e) {
			JOptionPane
					.showMessageDialog(null,
							"A FileNotFoundException has occurred, please check the logs");
			log.error("FileNotFoundException: " + e.getMessage());
		} catch (DynamicCursorException e) {
			JOptionPane
					.showMessageDialog(null,
							"A DynamicCursorException has occurred, please check the logs");
			log.error("DynamicCursorException: " + e.getMessage());
		} catch (DataNotFoundException e) {
			JOptionPane
					.showMessageDialog(null,
							"A DataNotFoundException has occurred, please check the logs");
			log.error("DataNotFoundException: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			JOptionPane
					.showMessageDialog(null,
							"A UnsupportedEncodingException has occurred, please check the logs");
			log.error("UnsupportedEncodingException: " + e.getMessage());
		} catch (DatabaseException e) {
			JOptionPane.showMessageDialog(null,
					"A DatabaseException has occurred, please check the logs");
			log.error("DatabaseException: " + e.getMessage());
		} catch (KeyNotFoundException e) {
			JOptionPane
					.showMessageDialog(null,
							"A KeyNotFoundException has occurred, please check the logs");
			log.error("KeyNotFoundException: " + e.getMessage());
			return;
		} catch (LinkIDException e) {
			JOptionPane.showMessageDialog(null,
					"LinkIDException occurred, check the log files");
			log.error("LinkIDException: " + e.getMessage());
			return;
		}

	}

}
