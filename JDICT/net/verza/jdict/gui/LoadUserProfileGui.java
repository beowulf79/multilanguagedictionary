package net.verza.jdict.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import org.apache.log4j.Logger;

/**
 * @author ChristianVerdelli
 * 
 */

public class LoadUserProfileGui {

	private static Logger log;

	private static LoadUserProfileGui singleton = null;
	private Boolean isUserLoad;

	public LoadUserProfileGui() {
		super();
		singleton = this;
		isUserLoad = false;

		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("Initialazing Load User Profile class");

		initComponents();

	}

	public Boolean isUserLoad() {
		return isUserLoad;
	}

	private void initComponents() {

		JFrame frame = new JFrame();
		frame.setBounds(5, 111, 252, 140);

		JPanel jpnl = new JPanel(new BorderLayout());
		JScrollPane jsp1 = new JScrollPane(jpnl);

		jpnl.setBackground(Color.orange);
		jpnl.setLayout(new GridBagLayout());
		jpnl.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels gap
																// to the
																// borders
		frame.getContentPane().add(BorderLayout.CENTER, jsp1);

		GridBagConstraints c = new GridBagConstraints();
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
		JButton jbtn1 = new JButton("Lookup User");
		jpnl.add(jbtn1, c);

		c.gridx = 1;
		c.gridy = 0;

		frame.getContentPane().add(jsp1);
		frame.setVisible(true);
		frame.setSize(300, 500);

	}

	public static LoadUserProfileGui getInstance() {
		if (singleton == null)
			singleton = new LoadUserProfileGui();
		return singleton;
	}

}
