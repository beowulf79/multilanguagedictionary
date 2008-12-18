package net.verza.jdict.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.verza.jdict.AudioPlayer;
import net.verza.jdict.Configuration;
import net.verza.jdict.Dictionary;
import net.verza.jdict.sleepycat.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.AudioNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.quiz.Arabic2ItalianQuiz;
import net.verza.jdict.quiz.Italian2ArabicQuiz;
import net.verza.jdict.quiz.Audio2ItalianQuiz;
import net.verza.jdict.quiz.Audio2ArabicQuiz;
import net.verza.jdict.quiz.QuizInterface;
import net.verza.jdict.sleepycat.datastore.SleepyWordsDatabase;
import net.verza.jdict.UserProfile;
import org.apache.log4j.Logger;
import com.sleepycat.je.DatabaseException;
import java.nio.charset.Charset;

/**
 * @author Christian Verdelli
 */

public class QuizTabGui extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_ARGUMENT_INDEX = 0;
	private static final int DEFAULT_ITERATIONS = 10;
	private static final String NOT_SELECTED_STRING = "---- Nothing Selected ----";
	private static Logger log;
	private static final String[] quizArray = { "italian2arabic",
			"arabic2italian", "audio2italian", "audio2arabic" };

	QuizTabActions handler;
	GridBagConstraints c;

	// Graphic COMPONENTS instance variable
	private JFrame frame2;
	private JPanel jpnl2;
	private JTextArea jtxa1;
	private JTextField jtxf4;
	private JTextField jtxf5;
	private JLabel jlb1;
	private JLabel jlb2;
	private JLabel jlb3;
	private JLabel jlb4;
	private JLabel jlb6;
	private JLabel jlb7;
	private JScrollPane scrlp1;
	private JButton jbt1;
	private JButton previousJButton;
	private JButton nextJButton;
	private JButton jbt4;
	private JButton jbt5;
	private JComboBox quizTypeSelectionJComboBox;
	private JComboBox categorySelectionJComboBox;
	private JComboBox sectionSelectionJComboBox;
	private JSpinner iterationJSpinner;

	// Instance variable
	private QuizResultTable qzJTable;
	private QuizInterface quiz;
	private Dictionary dit;
	private int iterations;

	public QuizTabGui() {

		super();
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());
		quiz = null;

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

		handler = new QuizTabActions(); // initialize events handler for this
										// class
		initComponents(); // initialize components graphich

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
		quizTypeSelectionJComboBox = new JComboBox(quizArray);
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

	private void openQuizWindow() {

		log.trace("quiz window open");
		QuizUserDataInput quiz_handler;

		frame2 = new JFrame();
		frame2.setBounds(5, 111, 252, 140);
		jpnl2 = new JPanel(new BorderLayout());
		jpnl2.setBackground(Color.orange);
		jpnl2.setLayout(new GridBagLayout());
		jpnl2.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels
																	// gap to
																	// the
																	// borders
		frame2.getContentPane().add(BorderLayout.CENTER, jpnl2);

		quiz_handler = new QuizUserDataInput(); // Set the Action Listener

		c = new GridBagConstraints();
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
		c.fill = GridBagConstraints.NONE;
		jpnl2.add(jlb6, c);

		// question
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		if (quizTypeSelectionJComboBox.getSelectedItem().equals("audio2arabic")
				|| (quizTypeSelectionJComboBox.getSelectedItem()
						.equals("audio2italian"))) {
			// question as audio JButton
			jbt5.addActionListener(quiz_handler);
			jbt5.setIcon(new ImageIcon("../images/ascolta.gif"));
			jpnl2.add(jbt5, c);
			jtxf5.setEditable(false); // disable until audio is played
		} else { // question as a string TextField
			try {
				if (quiz.getQuestion(0) instanceof String) {
					String s = (String) quiz.getQuestion(0);
					jtxf4.setText(new String(s.getBytes(), "UTF-8"));
					jtxf4.setEditable(false);
					jtxf4.setColumns(10);
					jpnl2.add(jtxf4, c);
				}
				if (quiz.getQuestion(0) instanceof byte[]) {
					
				}

			} catch (UnsupportedEncodingException e) {
				log.error("UnsupportedEncodingException " + e.getMessage());
				e.printStackTrace();
			}

		}

		// answer button label
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;

		jpnl2.add(jlb7, c);

		// answer text field
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		jtxf5.setEditable(true);
		jtxf5.setColumns(10);
		jpnl2.add(jtxf5, c);

		// previous button
		c.gridx = 0;
		c.gridy = 2;
		previousJButton.addActionListener(quiz_handler);
		previousJButton.setEnabled(false);
		jpnl2.add(previousJButton, c);

		// previous button
		c.gridx = 1;
		nextJButton.addActionListener(quiz_handler);
		jpnl2.add(nextJButton, c);

		// Show result button
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		jbt4.addActionListener(quiz_handler);
		jbt4.setEnabled(false);
		jpnl2.add(jbt4, c);

		frame2.getContentPane().add(jpnl2);
		frame2.pack();
		frame2.setVisible(true);
		frame2.setSize(300, 350);

	}

	public class QuizUserDataInput implements ActionListener {

		private String command;
		private int position;
		private boolean quizEnded = false;
		private Dictionary dit;
		
		public QuizUserDataInput() {
			command = null;
			position = 0;
			jtxf4 = new JTextField();
			jlb7 = new JLabel("Your answer");
			jtxf5 = new JTextField("-");
			previousJButton = new JButton("previous");
			nextJButton = new JButton("next");
			jbt4 = new JButton("show results");
			jbt5 = new JButton("play audio");
			jlb6 = new JLabel("Word to guess");
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
		}

		public void actionPerformed(ActionEvent evt) {
			command = evt.getActionCommand();
			Window w = Window.getInstance();

			try {

				if (command.equals("play audio")) {
					if (quiz.getQuestion(position) instanceof byte[]) {
						log.debug("playing audio");
						AudioPlayer audioPlayer = new AudioPlayer((byte[]) quiz
								.getQuestion(position));
						audioPlayer.play();
						jtxf5.setEditable(true);
					}
				}

				if (command.equals("show results")) {
					jbt4.setEnabled(false);
					qzJTable = new QuizResultTable(quiz.getStats());
					c.gridx = 0;
					c.gridy = 5;
					c.gridheight = 6;
					c.gridwidth = 3;
					c.fill = GridBagConstraints.BOTH;
					scrlp1.remove(jtxa1);
					remove(scrlp1);
					scrlp1 = new JScrollPane(qzJTable);
					add(scrlp1, c);
					w.repaint();

					// updating UserProfile object with generated QuizResult
					// objec
					UserProfile up = dit.getUser();
					up.addQuizStat(quiz.getStats());
					log.debug("updating profile of user " + up.toString());
					dit.writeUserDatabase(up);

				}

				if (command.equals("next")) {

					if (quizEnded) {
						position++;
						if (position == (iterations - 1))
							nextJButton.setEnabled(false);
						previousJButton.setEnabled(true);
						if (quiz.getQuestion(position) instanceof String) {
							String qs = new String((String) quiz
									.getQuestion(position));
							jtxf4.setText(new String(qs.getBytes(), "UTF-8"));
						}
						String as = new String((String) quiz
								.getUserAnswer(position));
						jtxf5.setText(new String(as.getBytes(), "UTF-8"));

					} else if (!quizEnded) {

						String userAnswer = null;
						if (jtxf5.getText().equals("")) {
							JOptionPane.showMessageDialog(new JFrame(),
									"answer cannot be null");
							return;
						} else {
							userAnswer = new String(jtxf5.getText().getBytes(
									"UTF-8"));
							log.debug("User answer " + userAnswer);
							quiz.userAnswer(position, userAnswer);
						}
						previousJButton.setEnabled(true); // enabling previous
															// button
						position++;
						if (position == (iterations)) {
							log
									.trace("iterations end reached, disabling next button,enabling showresults button and setting quizEnded true");
							jtxf5.setEditable(false);
							nextJButton.setEnabled(false);
							jbt4.setEnabled(true);
							quizEnded = true;
						} else if (position < iterations) { //modificare iterations con quiz.getIterations();
							if (quiz.getQuestion(position) instanceof String) {
								String s = new String((String) quiz
										.getQuestion(position));
								jtxf4
										.setText(new String(s.getBytes(),
												"UTF-8"));
							}
							jtxf5.setText("");
						}
					}

				} else if (command.equals("previous")) {

					if (quizEnded) {
						position--;
						if (position == 0)
							previousJButton.setEnabled(false);
						nextJButton.setEnabled(true);
						if (quiz.getQuestion(position) instanceof String) {
							String qs = new String((String) quiz
									.getQuestion(position));
							jtxf4.setText(new String(qs.getBytes(), "UTF-8"));
						}
						String as = new String((String) quiz
								.getUserAnswer(position));
						jtxf5.setText(new String(as.getBytes(), "UTF-8"));

					} else if (!quizEnded) {
						position--;
						nextJButton.setEnabled(true);
						if (position == 0) {
							previousJButton.setEnabled(false);
						} // IMPLEMENTARE CONTROLLO
						jtxf4.setText((String) quiz.getQuestion(position));
						jtxf5.setText(quiz.getUserAnswer(position));
						position--;
					}
				}

			} catch (UnsupportedEncodingException e) {
				log.error("UnsupportedEncodingException "+e.getMessage());
				e.printStackTrace();
			} catch (DatabaseException e) {
				log.error("DatabaseException "+e.getMessage());
				e.printStackTrace();
			} catch (DynamicCursorException e) {
				log.error(e.getMessage());
				JOptionPane.showMessageDialog(new JFrame(),
						"error with the database cursor " + e.getMessage());
				e.printStackTrace();
			} catch (DataNotFoundException e) {
				log.error(e.getMessage());
				JOptionPane.showMessageDialog(new JFrame(),
						"data not found error " + e.getMessage());
			} catch (AudioNotFoundException e) {
				JOptionPane.showMessageDialog(new JFrame(), "audio error "
						+ e.getMessage());
			} catch (FileNotFoundException e) {
				log.error(e.getMessage());
			}

		}
	}

	public class QuizTabActions implements ActionListener {

		private String command;
		JFileChooser fc;
		File openFile;
		File saveFile;
		String text;
		Charset cs;

		public QuizTabActions() {
			command = new String();
		}

		
		public void actionPerformed(ActionEvent evt) {


			command = evt.getActionCommand();
			if (command.equals("Start")) {
				try {
					int jcomboxIndex = quizTypeSelectionJComboBox.getSelectedIndex();
					log.trace("select quiz mode with index "+jcomboxIndex);
					if((jcomboxIndex == (new Integer(
							Configuration.ITALIAN2ARABIC)))
							) {

						quiz = new Italian2ArabicQuiz();
						quiz.setSecondaryDatbase(SleepyWordsDatabase
								.getAR_sing_IndexDatabase());

					}else if( (jcomboxIndex == (new Integer(
								Configuration.AUDIO2ARABIC))) )	{
						quiz = new Audio2ArabicQuiz();
						quiz.setSecondaryDatbase(SleepyWordsDatabase
								.getAR_sing_IndexDatabase());	
						
						
					}else if ( (jcomboxIndex == (new Integer(
							Configuration.ARABIC2ITALIAN))) ) {

						quiz = new Arabic2ItalianQuiz();
						quiz.setSecondaryDatbase(SleepyWordsDatabase
								.getIT_sing_IndexDatabase());

					}else if( (jcomboxIndex == (new Integer(
								Configuration.AUDIO2ITALIAN))) )	{
						quiz = new Audio2ItalianQuiz();
						quiz.setSecondaryDatbase(SleepyWordsDatabase
								.getIT_sing_IndexDatabase());
						
					} else {
						log.error("invalid Quiz type specified, exiting!");
						return;
					}

					quiz.setIterations((Integer) iterationJSpinner.getValue());
					if (!(categorySelectionJComboBox.getSelectedItem()
							.equals(NOT_SELECTED_STRING))) {
						// Translate the Category String to the Category ID by
						// looking
						// it in the Category Database
						String categoryString = dit
								.readCategoryDatabase((String) categorySelectionJComboBox
										.getSelectedItem());
						log
								.debug("setting category index to "
										+ categoryString);
						quiz.setCategoryIndex(categoryString);
					}
					if (!(sectionSelectionJComboBox.getSelectedItem()
							.equals(NOT_SELECTED_STRING))) {
						// Translate the Section String to the Category ID by
						// looking
						// it in the Section Database
						String sectionString = dit
								.readSectionDatabase((String) sectionSelectionJComboBox
										.getSelectedItem());
						log.debug("setting section index to " + sectionString);
						quiz.setSectionIndex(sectionString);
					}

					// Setting local variables to the number of iterations
					iterations = new Integer((Integer) iterationJSpinner.getValue());
					if (quiz.load() == 0) {
						openQuizWindow();
					} else
						JOptionPane.showInputDialog("Please input a value");

				} catch (DynamicCursorException e) {
					System.err.println(e.getMessage());
				} catch (DataNotFoundException e) {
					System.err.println(e.getMessage());
				} catch (FileNotFoundException e) {
					System.err.println(e.getMessage());
				} catch (UnsupportedEncodingException e) {
					System.out.println("Exception " + e);
				} catch (DatabaseException e) {
					System.out.println("Exception " + e);
				}

			}

		}

	}

}
