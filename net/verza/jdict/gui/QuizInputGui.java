package net.verza.jdict.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.verza.jdict.AudioPlayer;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.AudioNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import org.apache.log4j.Logger;
import com.sleepycat.je.DatabaseException;

/**
 * @author Christian Verdelli
 */

public class QuizInputGui extends JPanel {
	private static final long serialVersionUID = 1L;

	
	private static Logger log;
	
	QuizInputHandler handler;
	GridBagConstraints c;

	// Graphic COMPONENTS instance variable
	private JFrame frame2;
	private JPanel jpnl2;
	private JTextField jtxf4;
	private JTextField jtxf5;
	private JLabel jlb6;
	private JLabel jlb7;
	private JButton previousJButton;
	private JButton nextJButton;
	private JButton jbt4;
	private JButton playAudioButton;


	// Instance variable


	private QuizInputHandler quiz_handler;
	private QuizTabGui quiztab;
	
	public QuizInputGui(QuizTabGui _gui) {

		super();
		this.quiztab = _gui;
		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("called class " + this.getClass().getName());
					
		initComponents(); // initialize components graphich

	}

	
	private void initComponents() {
		
		frame2 = new JFrame();
		frame2.setBounds(5, 111, 252, 140);
		jpnl2 = new JPanel(new BorderLayout());
		jpnl2.setBackground(Color.orange);
		jpnl2.setLayout(new GridBagLayout());
		jpnl2.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels
																	// gap to
		// the borders
		frame2.getContentPane().add(BorderLayout.CENTER, jpnl2);
		
	
		quiz_handler = new QuizInputHandler(); // Set the Action Listener

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
		if (this.quiztab.dstLangCombo.getSelectedItem().toString().equals("audio")) {
			// question as audio JButton
			playAudioButton.addActionListener(quiz_handler);
			playAudioButton.setIcon(new ImageIcon("../images/ascolta.gif"));
			jpnl2.add(playAudioButton, c);
			jtxf5.setEditable(false); // disable until audio is played
		} else { // question as a string TextField
			try {
				if (this.quiztab.quiz.getQuestion(0) instanceof String) {
					String s = (String) this.quiztab.quiz.getQuestion(0);
					jtxf4.setText(new String(s.getBytes(), "UTF-8"));
					jtxf4.setEditable(false);
					jtxf4.setColumns(10);
					jpnl2.add(jtxf4, c);
				}
				if (this.quiztab.quiz.getQuestion(0) instanceof byte[]) {
					
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

	public class QuizInputHandler implements ActionListener {

		private String command;
		private int position;
		private boolean quizEnded = false;
		
		public QuizInputHandler() {
			command = null;
			position = 0;
			jtxf4 = new JTextField();
			jlb7 = new JLabel("Your answer");
			jtxf5 = new JTextField("-");
			previousJButton = new JButton("previous");
			nextJButton = new JButton("next");
			jbt4 = new JButton("show results");
			playAudioButton = new JButton("play audio");
			jlb6 = new JLabel("Word to guess");

		}

		public void actionPerformed(ActionEvent evt) {
			command = evt.getActionCommand();
			

			try {

				if (command.equals("play audio")) {
					if (quiztab.quiz.getQuestion(position) instanceof byte[]) {
						log.debug("playing audio");
						AudioPlayer audioPlayer = new AudioPlayer((byte[]) quiztab.quiz
								.getQuestion(position));
						audioPlayer.play();
						jtxf5.setEditable(true);
					}
				}

				if (command.equals("show results")) {
					jbt4.setEnabled(false);
					quiztab.showResults();

				}

				if (command.equals("next")) {

					if (quizEnded) {
						System.out.println("quizEnded");
						position++;
						if (position == (quiztab.iterations - 1)) {
							System.out.println("disabilito next ed audio");
							nextJButton.setEnabled(false);
						}
							previousJButton.setEnabled(true);
						if (quiztab.quiz.getQuestion(position) instanceof String) {
							String qs = new String((String) quiztab.quiz
									.getQuestion(position));
							jtxf4.setText(new String(qs.getBytes(), "UTF-8"));
						}
						String as = new String((String) quiztab.quiz
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
							quiztab.quiz.userAnswer(position, userAnswer);
						}
						previousJButton.setEnabled(true); // enabling previous
															// button
						position++;
						if (position == (quiztab.iterations)) {
							log
									.trace("iterations end reached, disabling next button,enabling showresults button and setting quizEnded true");
							jtxf5.setEditable(false);
							nextJButton.setEnabled(false);
							playAudioButton.setEnabled(false);
							jbt4.setEnabled(true);
							quizEnded = true;
						} else if (position < quiztab.iterations) { //modificare iterations con quiz.getIterations();
							if (quiztab.quiz.getQuestion(position) instanceof String) {
								String s = new String((String) quiztab.quiz
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
						playAudioButton.setEnabled(true);
						nextJButton.setEnabled(true);
						if (quiztab.quiz.getQuestion(position) instanceof String) {
							String qs = new String((String) quiztab.quiz
									.getQuestion(position));
							jtxf4.setText(new String(qs.getBytes(), "UTF-8"));
						}
						String as = new String((String) quiztab.quiz
								.getUserAnswer(position));
						jtxf5.setText(new String(as.getBytes(), "UTF-8"));

					} else if (!quizEnded) {
						position--;
						nextJButton.setEnabled(true);
						if (position == 0) {
							previousJButton.setEnabled(false);
						} // IMPLEMENTARE CONTROLLO
						jtxf4.setText((String) quiztab.quiz.getQuestion(position));
						jtxf5.setText(quiztab.quiz.getUserAnswer(position));
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
			} catch (KeyNotFoundException e) {
				log.error(e.getMessage());
			}

		}
	}

	
	
	
}


