package net.verza.jdict.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.verza.jdict.exceptions.AudioNotFoundException;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.utils.AudioPlayer;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

/**
 * @author Christian Verdelli
 */

public class QuizInputGui extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private static Logger log;
    GridBagConstraints c;
    private JFrame frame;
    private JPanel jpnl;
    private JTextField jtxf4;
    private JTextField jtxf5;
    private JLabel jlb6;
    private JLabel jlb7;
    private JButton nextJButton;
    private JButton jbt4;
    private JButton playAudioButton;
    private QuizTabGui quiztab;
    private int position;

    public QuizInputGui(QuizTabGui _gui) {

	super();
	this.quiztab = _gui;
	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());

	initComponents(); // initialize components graphich
	createShowGUI();
    }

    private void createShowGUI() {
	frame = new JFrame("QuizInputGui");
	frame.setBounds(5, 111, 252, 140);
	frame.setBackground(GUIPreferences.backgroundColor);
	frame.getContentPane().add(jpnl);
	frame.setVisible(true);
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setSize(650, 380);
    }

    private void initComponents() {

	int y = 0;

	jpnl = new JPanel(new BorderLayout());
	jpnl.setBackground(GUIPreferences.backgroundColor);
	jpnl.setLayout(new GridBagLayout());
	jpnl.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

	jtxf4 = new JTextField();
	jlb7 = new JLabel("Answer");
	jtxf5 = new JTextField("-");
	nextJButton = new JButton("answer!");
	nextJButton.setActionCommand("next");
	jbt4 = new JButton("show results");
	playAudioButton = new JButton("play audio");
	jlb6 = new JLabel("Question");

	c = new GridBagConstraints();
	c.insets = new Insets(2, 2, 2, 2);// add some space between
	// components
	// to avoid clutter
	c.anchor = GridBagConstraints.WEST;// anchor all components
	// WEST
	c.weightx = 1.0;// all components use vertical available space
	c.weighty = 1.0;// all components use horizontal available space
	c.gridheight = 1;
	c.gridwidth = 1;

	// question label
	c.gridx = 0;
	c.gridy = ++y;
	c.fill = GridBagConstraints.NONE;
	jlb6.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(jlb6, c);

	// question
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	if (this.quiztab.dstLangCombo.getSelectedItem().toString().equals(
		"audio")) {
	    // question as audio JButton
	    playAudioButton.addActionListener(this);
	    playAudioButton.setIcon(new ImageIcon("../images/ascolta.gif"));
	    jpnl.add(playAudioButton, c);
	    jtxf5.setEditable(false); // disable until audio is
	    // played
	} else { // question as a string TextField
	    try {
		if (this.quiztab.quiz.getQuestion(0) instanceof String) {

		    jtxf4.setText(new String(((String) this.quiztab.quiz
			    .getQuestion(0)).getBytes(), "UTF-8"));
		    jtxf4.setEditable(false);
		    jtxf4.setColumns(10);
		    jpnl.add(jtxf4, c);
		}
		if (this.quiztab.quiz.getQuestion(0) instanceof byte[]) {

		}

	    } catch (UnsupportedEncodingException e) {
		log.error("UnsupportedEncodingException " + e.getMessage());
		e.printStackTrace();
	    }

	}

	// Notes Label
	c.gridy = ++y;
	c.gridx = 0;
	JLabel tipJLabel = new JLabel("notes");
	tipJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(tipJLabel, c);
	c.gridx = 1;
	JTextField jtxf6 = new JTextField(this.quiztab.quiz.getNotes(0));
	jtxf6.setEnabled(false);
	jtxf6.setColumns(10);
	jpnl.add(jtxf6, c);
	
	
	// Example Label
	c.gridy = ++y;
	c.gridx = 0;
	JLabel exampleJLabel = new JLabel("examples");
	tipJLabel.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(exampleJLabel, c);
	c.gridx = 1;
	JTextArea jtxa1 = new JTextArea(this.quiztab.quiz.getExamples(0));
	jtxa1.setEnabled(false);
	jtxa1.setRows(2);
	jtxa1.setColumns(20);
	jpnl.add(jtxa1, c);

	
	// Answer button label
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = ++y;
	c.fill = GridBagConstraints.NONE;
	jlb7.setBorder(BorderFactory.createLineBorder(
		GUIPreferences.borderColor, GUIPreferences.borderThickness));
	jpnl.add(jlb7, c);

	// answer text field
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	jtxf5.setEditable(true);
	jtxf5.setColumns(10);
	jpnl.add(jtxf5, c);

	// next button
	c.gridx = 1;
	c.gridy = ++y;
	nextJButton.addActionListener(this);
	jpnl.add(nextJButton, c);

	// Show result button
	c.gridx = 0;
	c.gridy = ++y;
	c.gridwidth = 2;
	jbt4.addActionListener(this);
	jbt4.setEnabled(false);
	jpnl.add(jbt4, c);

    }

    public void actionPerformed(ActionEvent evt) {
	String command = evt.getActionCommand();
	boolean quizEnded = false;

	try {

	    if (command.equals("play audio")) {
		if (quiztab.quiz.getQuestion(position) instanceof byte[]) {
		    log.debug("playing audio");
		    AudioPlayer audioPlayer = new AudioPlayer(
			    (byte[]) quiztab.quiz.getQuestion(position));
		    audioPlayer.play();
		    jtxf5.setEditable(true);
		}
	    }

	    if (command.equals("show results")) {
		jbt4.setEnabled(false);
		quiztab.showResults();

	    }

	    if (command.equals("next")) {
		log.debug("next button pressed");
		if (quizEnded) {
		    log.debug("quiz  ended");
		    position++;
		    if (position == (quiztab.iterations - 1)) {
			log.debug("disabling next button");
			nextJButton.setEnabled(false);
		    }

		    if (quiztab.quiz.getQuestion(position) instanceof String) {
			String qs = new String((String) quiztab.quiz
				.getQuestion(position));
			jtxf4.setText(new String(qs.getBytes(), "UTF-8"));
		    }
		    String as = new String((String) quiztab.quiz
			    .getUserAnswer(position));
		    jtxf5.setText(new String(as.getBytes(), "UTF-8"));

		} else if (!quizEnded) {
		    log.debug("quiz not ended");
		    String userAnswer = null;
		    if (jtxf5.getText().equals("")) {
			JOptionPane.showMessageDialog(new JFrame(),
				"answer cannot be null");
			return;
		    } else {
			userAnswer = new String(jtxf5.getText().getBytes(
				"UTF-8"));
			log.debug("user answer " + userAnswer);
			quiztab.quiz.userAnswer(position, userAnswer);
		    }

		    position++;

		    if (position == (quiztab.iterations)) { // Quizd
			// End
			log
				.debug("iterations end reached, disabling next button,enabling showresults button and setting quizEnded true");
			jtxf5.setEditable(false);
			nextJButton.setEnabled(false);
			playAudioButton.setEnabled(false);
			jbt4.setEnabled(true);
			quizEnded = true;

		    } else if (position < quiztab.iterations) {
			log.debug("position " + position
				+ "different then quiztab.iterations"
				+ quiztab.iterations);
			if (quiztab.quiz.getQuestion(position) instanceof String) {
			    String s = new String((String) quiztab.quiz
				    .getQuestion(position));
			    jtxf4.setText(new String(s.getBytes(), "UTF-8"));
			}
			jtxf5.setText("");
		    }
		}

	    }

	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DatabaseException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DynamicCursorException e) {
	    log.error(e.getMessage());
	    JOptionPane.showMessageDialog(new JFrame(),
		    "error with the database cursor " + e.getMessage());
	    e.printStackTrace();
	} catch (DataNotFoundException e) {
	    log.error(e.getMessage());
	    JOptionPane.showMessageDialog(new JFrame(), "data not found error "
		    + e.getMessage());
	} catch (AudioNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(new JFrame(), "audio error "
		    + e.getMessage());
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (KeyNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (Exception e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	}

    }

}
