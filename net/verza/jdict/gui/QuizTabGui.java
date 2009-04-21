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
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguagesConfiguration;
import net.verza.jdict.quiz.QuizInterface;
import org.apache.log4j.Logger;
import com.sleepycat.je.DatabaseException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Christian Verdelli
 */

public class QuizTabGui extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_ARGUMENT_INDEX = 0;
	public static final int DEFAULT_ITERATIONS = 10;
	public static final String NOT_SELECTED_STRING = "---- Nothing Selected ----";
	public static Logger log;
	public static String[] languageArray;

	public static QuizTabGui instance = null;
	public QuizTabActions handler;

	// Graphic COMPONENTS instance variable
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
	public QuizInterface quiz;
	public Dictionary dit;
	public int iterations;
	public String src_lang,dst_lang;

	public QuizTabGui() {

		super();
		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("called class " + this.getClass().getName());
		quiz = null;
		instance = this;
		
		try {
			dit = Factory.getDictionary();
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

		handler = new QuizTabActions(); // initialize events handler for this class
		buildQuizMenu(); // builds the JComboBox 						
		initComponents(); // initialize components graphich

	}

	
	/*
	 * Costruisce il menu JComboBox di scelta del quiz.
	 * Ottiene i linguaggi definiti nell'oggetto LanguageConfiguration, e per ciascuno 
	 * essi se abilitato, ottiene i quiz disponibili e classi che li gestiscono; per ogni quiz verifica
	 * inoltre che il lingugaggio 'target' sia abilitata; 
	 */
	private void buildQuizMenu()	{	
		log.trace("inside function buildLanguageMenu");
		String[] tmp = new String[40];
		HashMap<String, LanguageConfigurationClassDescriptor> ldesc = LanguagesConfiguration
															.getLanguageConfigurationBlock();
		LanguageConfigurationClassDescriptor sub = null;
		
		int ext_counter=0;
		for (Iterator<String> it = ldesc.keySet().iterator(); it.hasNext();) {
			sub = (LanguageConfigurationClassDescriptor) ldesc.get(it
					.next());
		
			String nickname = sub.getLanguageNickname();
			String type = sub.getType();
			log.debug("languge " + nickname + " type " + type);
		
			// check if the language is enabled , if not skip to the next
			// language
			if (!sub.getIsEnabled()) {
				log.info("language " + nickname
						+" type " + type
						+ " not enabled, skipping to next language");
				continue;
			}
			//if audio is enabled add "audio2language" quiz to the JComboBox
			if(sub.getIsAudioEnabled())	{
				log.info("audio enabled for the language, adding quiz audio2"
							+nickname);
				tmp[ext_counter++] = "audio" 
									+"2"
									+nickname
									+""
									+type;
			}
			
			//read the property in the configuration file
			String[] translation = sub.getTranslations();
			// check if each  translation are enabled; if so add them to the JComboBox
			for(int int_counter=0;int_counter<translation.length;int_counter++)	{
				LanguageConfigurationClassDescriptor transl = 
					LanguagesConfiguration.getLanguageMainConfigNode(translation[int_counter]+type);
				log.debug("getting configuration of the language "+translation[int_counter]+type);
				if(transl.getIsEnabled())	{
					log.trace("adding to JcomboBox language "+translation[int_counter]
				                                             + type);
					tmp[ext_counter++] =  nickname 
											+ type
											+"2"
											+ transl.getLanguageNickname() 
											+ transl.getType();
				}	
			}
		}

		languageArray = new String[ext_counter];
        System.arraycopy(tmp, 0, languageArray, 0,
        		ext_counter);
	}
	
	
	private void initComponents() {

		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels gap to
															// the borders

		c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2); // add some space between components
											// to avoid clutter
		c.anchor = GridBagConstraints.WEST; // anchor all components WEST
		c.weightx = 0.1; // all components use vertical available space
		c.weighty = 0.1; // all components use horizontal available space

		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		jlb1 = new JLabel("Type");
		add(jlb1, c);

		c.gridx = 2;
		quizTypeSelectionJComboBox = new JComboBox(languageArray);
		quizTypeSelectionJComboBox.addActionListener(handler);
		quizTypeSelectionJComboBox.setSelectedIndex(DEFAULT_ARGUMENT_INDEX);
		add(quizTypeSelectionJComboBox, c);

		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		jlb2 = new JLabel("Argument/Category");
		add(jlb2, c);

		c.gridx = 2;
		categorySelectionJComboBox = new JComboBox(readCategory());
		categorySelectionJComboBox.addItem(NOT_SELECTED_STRING);
		categorySelectionJComboBox.setSelectedIndex(categorySelectionJComboBox
				.getItemCount() - 1);
		categorySelectionJComboBox.addActionListener(handler);
		add(categorySelectionJComboBox, c);

		c.gridx = 0;
		c.gridy = 2;
		c.fill = GridBagConstraints.NONE;
		jlb4 = new JLabel("Section");
		add(jlb4, c);

		c.gridx = 2;
		sectionSelectionJComboBox = new JComboBox(readSection());
		sectionSelectionJComboBox.addItem(NOT_SELECTED_STRING);
		sectionSelectionJComboBox.setSelectedIndex(sectionSelectionJComboBox
				.getItemCount() - 1);
		sectionSelectionJComboBox.addActionListener(handler);
		add(sectionSelectionJComboBox, c);

		c.gridx = 0;
		c.gridy = 3;
		c.fill = GridBagConstraints.NONE;
		jlb3 = new JLabel("Iterations");
		add(jlb3, c);

		c.gridx = 2;
		iterationJSpinner = new JSpinner();
		iterationJSpinner.setValue(DEFAULT_ITERATIONS);
		add(iterationJSpinner, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		jbt1 = new JButton("Start");
		jbt1.addActionListener(handler);
		add(jbt1, c);

		
		c.gridx = 0;
		c.gridy = 5;
		c.gridheight = 5;
		c.gridwidth = 4;
		c.weightx = 1.0;
		c.weighty = 1.0;
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

	public void showResults()	{
		try	{
			
			QuizResultTable qzJTable = new QuizResultTable(quiz.getStats());
			c.gridx = 0;
			c.gridy = 5;
			c.gridheight = 6;
			c.gridwidth = 3;
			c.fill = GridBagConstraints.BOTH;
			scrlp1.remove(jtxa1);
			remove(scrlp1);
			scrlp1 = new JScrollPane(qzJTable);
			add(scrlp1, c);
			Window.getInstance().repaint();
		
			// updating UserProfile object with generated QuizResult objecj
			UserProfile up = dit.getUser();
			up.addQuizStat(quiz.getStats());
			log.debug("updating profile of user " + up.toString());
			dit.writeUserDatabase(up);
		} catch (UnsupportedEncodingException e) {
			log.error("UnsupportedEncodingException "+e.getMessage());
			e.printStackTrace();
		} catch (DatabaseException e) {
			log.error("DatabaseException "+e.getMessage());
			e.printStackTrace();
		}

	}
	
	
	public class QuizTabActions implements ActionListener {

		
		public void actionPerformed(ActionEvent evt) {

			if (evt.getActionCommand().equals("Start")) {
				String quizString = new String((String)quizTypeSelectionJComboBox.getSelectedItem());
				src_lang = quizString.split("2")[0];
				log.trace("selected source language: "+src_lang);
				dst_lang  = quizString.split("2")[1];
				log.trace("selected destination language: "+dst_lang);
				
				try {	
					//load the class that will handle the Quiz
					Class<?> toRun = Class.forName("net.verza.jdict.quiz."+quizString);
					log.debug("class going to be loaded: net.verza.jdict.quiz."+quizString);
					Class<?>[] c_arr = new Class[] {  };
					Constructor<?> constr = toRun.getConstructor(c_arr);
					quiz = (QuizInterface) constr.newInstance();
					iterations = new Integer((Integer) iterationJSpinner.getValue());
					quiz.setIterations(iterations);
					
					//Category Combo Box
					String category = (String)categorySelectionJComboBox.getSelectedItem();
					if(!category.equals(NOT_SELECTED_STRING))
						quiz.setSectionIndex(category, src_lang) ;
					
					//Section Combo Box
					String section = (String)sectionSelectionJComboBox.getSelectedItem();
					if(!section.equals(NOT_SELECTED_STRING))	
						quiz.setSectionIndex(section, src_lang) ;
					
					if (quiz.load() == 0) {
						new QuizInputGui(instance);
					} else
						JOptionPane.showInputDialog("Please input a value");
					
					
				}catch (ClassNotFoundException e) {
					System.out.println(e);
				} catch (NoSuchMethodException e) {
					System.out.println(e);
				} catch (InstantiationException e) {
					System.out.println(e);
				} catch (IllegalAccessException e) {
					System.out.println(e);
				} catch (InvocationTargetException e) {
					System.out.println(e);
				} catch (FileNotFoundException e) {
					System.out.println(e);
				} catch (DynamicCursorException e) {
					System.out.println(e);
				} catch (DataNotFoundException e) {
					System.out.println(e);
				} catch (UnsupportedEncodingException e) {
					System.out.println(e);
				} catch (DatabaseException e) {
					System.out.println(e);
				} catch (KeyNotFoundException e) {
					log.error("DynamicCursorException: " + e.getMessage());
					return;
				}
			}

	
		}

	}

}
