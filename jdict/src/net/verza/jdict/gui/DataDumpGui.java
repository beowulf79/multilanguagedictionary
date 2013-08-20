package net.verza.jdict.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import jxl.read.biff.BiffException;
import net.verza.jdict.dataloaders.LoaderOptionsStore;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DatabaseImportException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguagesConfiguration;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

public class DataDumpGui extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static Logger log;
	private static DataDumpGui singleton = null;
	private Dictionary dit;
	private LoaderOptionsStore optionsObj;
	private JFrame frame;
	private JFileChooser fileChooser;
	private GridBagConstraints c;
	private JTextArea textAreaLog;
	private int y_coordinate;
	private JComboBox typeSelectCombo;
	private JCheckBox sectionImportCheckBox, categoryImportCheckBox;
	private JButton startDumpButton;

	public DataDumpGui() {

		super();
		try {
			log = Logger.getLogger("jdict");
			log.trace("called class " + this.getClass().getName());
			optionsObj = new LoaderOptionsStore();
			dit = Factory.getDictionary();
			this.initComponents();
			this.createShowGUI();
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			log.error("DataNotFoundException " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("IOException " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		} catch (DatabaseException e) {
			e.printStackTrace();
			log.error("DatabaseException " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		} catch (DynamicCursorException e) {
			e.printStackTrace();
			log.error("DynamicCursorException " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		} catch (KeyNotFoundException e) {
			e.printStackTrace();
			log.error("KeyNotFoundException " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			log.error("IllegalArgumentException " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			log.error("ClassNotFoundException " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			log.error("NoSuchMethodException " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			log.error("InstantiationException " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			log.error("IllegalAccessException " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			log.error("InvocationTargetException " + e.getMessage());
			JOptionPane.showMessageDialog(frame, e.getMessage());
		}

	}

	private void initComponents() {

		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels
		// gap to
		// the borders

		c = new GridBagConstraints(); // add some space between
		// components to
		// avoid clutter
		c.insets = new Insets(2, 2, 2, 2); // anchor all components
		// WEST
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1.0;
		c.weighty = 0.1;

		c.gridx = 0;
		c.gridy = this.y_coordinate;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		JButton openButton = new JButton("Select file to dump content",
				Commons.createImageIcon("images/Open16.gif"));
		openButton.setActionCommand("savebutton");
		openButton.addActionListener(this);
		add(openButton, c);

		// Dump command button
		c.gridy = ++this.y_coordinate;
		add(new JSeparator(), c);
		c.gridx = 0;
		c.gridy = ++this.y_coordinate;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		startDumpButton = new JButton("dump !!");
		startDumpButton.setActionCommand("dump");
		startDumpButton.addActionListener(this);
		startDumpButton.setEnabled(false);
		add(startDumpButton, c);

		// Text Area used for logging
		c.gridy = ++this.y_coordinate;
		add(new JSeparator(), c);
		c.gridx = 0;
		c.gridy = ++this.y_coordinate;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.BOTH;
		textAreaLog = new JTextArea(5, 20);
		textAreaLog.setMargin(new Insets(5, 5, 5, 5));
		textAreaLog.setEditable(false);
		JScrollPane textAreaLogScrollPane = new JScrollPane(textAreaLog);
		add(textAreaLogScrollPane, c);

	}

	private void createShowGUI() {
		// Create and set up the window.
		frame = new JFrame("Window");
		frame.addWindowListener(new MainFrameCloser(this));
		frame.setTitle("Dump Window");
		frame.setSize(350, 350);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(this);
		frame.setBackground(GUIPreferences.backgroundColor);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	private void destroyInstance() {
		if (singleton != null)
			singleton = null;
	}

	public class MainFrameCloser extends java.awt.event.WindowAdapter {
		protected DataDumpGui frame = null;

		public MainFrameCloser(DataDumpGui frame) {
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

	private JFrame getJFrame() {
		return frame;
	}

	public static DataDumpGui getInstance() {
		if (singleton == null)
			singleton = new DataDumpGui();
		return singleton;
	}

	public void actionPerformed(ActionEvent ex) {

		String command = ex.getActionCommand();
		log.debug("command received " + command);

		if (this.optionsObj.getLabels().containsKey(command))
			this.optionsObj.setLabels(command,
					((JCheckBox) ex.getSource()).isSelected());

		else if (command == "savebutton") {
			int returnVal = fileChooser.showSaveDialog(DataDumpGui.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				this.optionsObj.setInputFile(file);
				textAreaLog.append("opening file for saving " + file.getName()
						+ ".\n");
				log.debug("choosed dump file " + file);
				this.startDumpButton.setEnabled(true);
			} else {
				textAreaLog.append("save command cancelled by user.\n");
			}

		} else if (command.equals("dump")) {

			try {
				dit.dumpDatabase(optionsObj);
				textAreaLog.append("dictionary dump finished.");
			} catch (IOException e) {
				e.printStackTrace();
				log.error("IOException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (DatabaseException e) {
				e.printStackTrace();
				log.error("DatabaseException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (KeyNotFoundException e) {
				e.printStackTrace();
				log.error("KeyNotFoundException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (BiffException e) {
				e.printStackTrace();
				log.error("BiffException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (LabelNotFoundException e) {
				e.printStackTrace();
				log.error("LabelNotFoundException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (DatabaseImportException e) {
				e.printStackTrace();
				log.error("DatabaseImportException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (LanguagesConfigurationException e) {
				e.printStackTrace();
				log.error("LanguagesConfigurationException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (SecurityException e) {
				e.printStackTrace();
				log.error("SecurityException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				log.error("IllegalArgumentException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				log.error("ClassNotFoundException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				log.error("NoSuchMethodException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (InstantiationException e) {
				e.printStackTrace();
				log.error("InstantiationException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				log.error("IllegalAccessException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				log.error("InvocationTargetException " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				log.error("Exception " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
			}
		}

	}

}
