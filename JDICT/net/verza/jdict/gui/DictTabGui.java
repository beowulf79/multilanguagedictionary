package net.verza.jdict.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.nio.charset.Charset;
import net.verza.jdict.exceptions.*;
import net.verza.jdict.Configuration;
import net.verza.jdict.Dictionary;
import net.verza.jdict.sleepycat.datastore.SleepyWordsDatabase;
import net.verza.jdict.sleepycat.Factory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DatabaseEntry;


/**
 * @author Christian Verdelli
 */

public class DictTabGui extends JPanel {
	private static final long serialVersionUID = 1L;

	private static Logger log;

	private static DictTabGui instance = null;

	private Dictionary dit;

	private DictResultTable newContentPane;
	private DictTabActions handler;
	private GridBagConstraints c;
	private JFrame frame;
	private JComboBox languageSelection;
	private JTextField jtxf1;
	private JTextArea jtxa1;
	private static final String[] languagesArray = { "italian --> arabic",
			"arabic --> italian" };

	public DictTabGui() {
		super();
		try {
			handler = new DictTabActions(); // initialize events handler for
											// this class
			initComponents(); // initialize components graphics

			PropertyConfigurator.configure(Configuration.LOG4JCONF);
			log = Logger.getLogger("XMLCOMPARER_Logger");
			log.trace("called class " + this.getClass().getName());
			dit = Factory.getDictionary();
			
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (DynamicCursorException e) {
			e.printStackTrace();
		} catch (DataNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void initComponents() {

		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(new Insets(5, 5, 5, 5))); // 5 pixels gap to
															// the borders

		c = new GridBagConstraints(); // add some space between components to
										// avoid clutter
		c.insets = new Insets(2, 2, 2, 2); // anchor all components WEST
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1.0;
		c.weighty = 0.1;

		// add JComboBox to choose the languages of the lookup
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		languageSelection = new JComboBox(languagesArray);
		languageSelection.setSelectedIndex(0);
		add(languageSelection, c);

		// add JTextfield for the user input
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		jtxf1 = new JTextField("felice");
		add(jtxf1, c);

		// add JButton to send the search
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		JButton jbt1 = new JButton("search");
		jbt1.addActionListener(handler);
		add(jbt1, c);

		// Add the JTable
		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 3;
		c.gridwidth = 3;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		jtxa1 = new JTextArea();
		jtxa1.setColumns(20);
		jtxa1.setLineWrap(true);
		jtxa1.setRows(5);
		jtxa1.setWrapStyleWord(true);
		jtxa1.setEditable(false);
		add(jtxa1, c);
		setVisible(true);

	}

	public class DictTabActions implements ActionListener {

		private String command;
		JFileChooser fc;
		File openFile;
		File saveFile;
		String text;
		Charset cs;

		public DictTabActions() {
			command = new String();
			text = null;
		}

		public void actionPerformed(ActionEvent evt) {

			command = evt.getActionCommand();
			log.trace("received command " + command);
			Window w = Window.getInstance();
			text = jtxf1.getText();

			if (command.equals("search")) {
				log.trace("pressed botton \"search\" ");

				// user input empty, dialog opens up to
				while (text.length() == 0) {
					String s = (String) JOptionPane
							.showInputDialog("please insert the word to lookup for");
					// the user input is valid
					if ((text != null) && (s.length() > 0)) {
						jtxf1.setText(s);
						return;
					}
					return;
				}
				String userInput = null;
				try {

					// clear the content if previously used
					if (newContentPane != null) {
						log.trace("removing table");
						remove(newContentPane);
						newContentPane = null;
					}

					userInput = new String(jtxf1.getText().getBytes("UTF-8"));
					log.debug("user is looking up the word " + userInput);
					log.trace("selected lookup language "
							+ languageSelection.getSelectedItem());

					// set the language that must be used for the look-up
					if (languageSelection.getSelectedIndex() == 0) {
						dit.setSearchKey(SleepyWordsDatabase
								.getIT_sing_IndexDatabase(), new DatabaseEntry(
								userInput.getBytes("UTF-8")));
						dit.read();
						// building table with the results of the query
						log.debug("building table with data size "
								+ dit.getData().size());
						newContentPane = new DictResultTable(dit.getData());
					}

					else if (languageSelection.getSelectedIndex() == 1) {
						dit.setSearchKey(SleepyWordsDatabase
								.getAR_sing_IndexDatabase(), new DatabaseEntry(
								userInput.getBytes("UTF-8")));
						dit.read();
						
						// building table with the results of the query
						log.debug("building table with data size "
								+ dit.getKey().size());
						newContentPane = new DictResultTable(dit.getKey());
					}
					
					remove(jtxa1);
					add(newContentPane, c);
					w.repaint();

				} catch (UnsupportedEncodingException e) {
					log.error("UnsupportedEncodingException: " + e.getMessage());
					return;
				} catch (DatabaseException e) {
					log.error("DatabaseException: " + e.getMessage());
					return;
				} catch (DataNotFoundException e) {
					log.warn("not found in the directory");
					JOptionPane.showMessageDialog(frame,
							"not found in the dictionary");
					return;
				} catch (DynamicCursorException e) {
					log.error("DynamicCursorException: " + e.getMessage());
					return;
				}

				

			}
		}
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Window.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			log.error("couldn't find file: " + path);
			return null;
		}
	}

	/*
	 * Returns pointer to this instance; SingleTon Pattern
	 */
	public static DictTabGui getInstance() {
		if (instance == null) {
			instance = new DictTabGui();
		}
		return instance;
	}

}
