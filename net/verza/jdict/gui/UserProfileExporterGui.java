package net.verza.jdict.gui;

import com.sleepycat.je.DatabaseException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import javax.swing.JFrame;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.DatabaseImportException; 
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.log4j.Logger;
import jxl.read.biff.BiffException;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguagesConfiguration;
import net.verza.jdict.dataloaders.LoaderOptionsStore;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

import javax.swing.border.EmptyBorder;

public class UserProfileExporterGui extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static Logger log;
	private static UserProfileExporterGui singleton = null;
	private Dictionary dit;
	private LoaderOptionsStore optionsObj;

	// swing objects
	private JFrame frame;
	private JFileChooser fileChooser;
	private GridBagConstraints c;
	private JTextArea textAreaLog;
	private int y_coordinate;
	private JComboBox typeSelectCombo;
	private JCheckBox sectionImportCheckBox, categoryImportCheckBox;
	private JButton startImportButton;
	
	public UserProfileExporterGui() {

		super();

		try {

			log = Logger.getLogger("net.verza.jdict.gui");
			log.trace("called class " + this.getClass().getName());
			optionsObj = new LoaderOptionsStore();
			dit = Factory.getDictionary();
			this.initComponents();
			this.createShowGUI();

		} catch (DataNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (DynamicCursorException e) {
			e.printStackTrace();
		}

	}

	private void initComponents() {

		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels gap to
		// the borders

		c = new GridBagConstraints(); // add some space between components to
		// avoid clutter
		c.insets = new Insets(2, 2, 2, 2); // anchor all components WEST
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1.0;
		c.weighty = 0.1;

		c.gridx = 0;
		c.gridy = this.y_coordinate;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		JButton openButton = new JButton("Select file to Load", Commons
				.createImageIcon("images/Open16.gif"));
		openButton.setActionCommand("openbutton");
		openButton.addActionListener(this);
		add(openButton, c);

		c.gridy = ++this.y_coordinate;
		add(new JSeparator(), c);
		c.gridwidth = 1;
		c.fill = GridBagConstraints.WEST;
		c.gridy = ++this.y_coordinate;
		JLabel buildTypeLabel = new JLabel("Select type of import");
		add(buildTypeLabel, c);
		c.gridx = 1;
		String[] types = { "rebuild", "update" };
		typeSelectCombo = new JComboBox(types);
		typeSelectCombo.addActionListener(this);
		add(typeSelectCombo, c);

		c.gridy = ++this.y_coordinate;
		add(new JSeparator(), c);
		// Call function buildLanguageOptionList to dynamically show
		// only enabled languages
		this.buildLanguageOptionList();

		c.gridy = ++this.y_coordinate;
		add(new JSeparator(), c);

		// Category Table Import Option
		c.gridx = 0;
		c.gridy = ++this.y_coordinate;
		JLabel categoryImportLabel = new JLabel("Import category Table");
		add(categoryImportLabel, c);
		c.gridx = 1;
		categoryImportCheckBox = new JCheckBox();
		categoryImportCheckBox.setActionCommand("category");
		categoryImportCheckBox.addActionListener(this);
		add(categoryImportCheckBox, c);

		// Selection Table Import Option
		c.gridx = 0;
		c.gridy = ++this.y_coordinate;
		JLabel selectionImportLabel = new JLabel("Import Section Table");
		add(selectionImportLabel, c);
		c.gridx = 1;
		sectionImportCheckBox = new JCheckBox();
		sectionImportCheckBox.setActionCommand("section");
		sectionImportCheckBox.addActionListener(this);
		add(sectionImportCheckBox, c);

		// Import command button
		c.gridy = ++this.y_coordinate;
		add(new JSeparator(), c);
		c.gridx = 0;
		c.gridy = ++this.y_coordinate;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		startImportButton = new JButton("import !!");
		startImportButton.setActionCommand("import");
		startImportButton.addActionListener(this);
		startImportButton.setEnabled(false);
		add(startImportButton, c);

		// Text Area used for logging
		c.gridy = ++this.y_coordinate;
		add(new JSeparator(), c);
		c.gridx = 0;
		c.gridy = ++this.y_coordinate;
		c.gridwidth = 2;
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
		frame.setTitle("Import Options Chooser Window");
		frame.setSize(850, 750);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(this);
		// Display the window.
		frame.pack();
		frame.setVisible(true);

	}

	private void destroyInstance() {
		if (singleton != null)
			singleton = null;
	}

	public class MainFrameCloser extends java.awt.event.WindowAdapter {
		protected UserProfileExporterGui frame = null;

		public MainFrameCloser(UserProfileExporterGui frame) {
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

	public static UserProfileExporterGui getInstance() {
		if (singleton == null)
			singleton = new UserProfileExporterGui();
		return singleton;
	}

	private void buildLanguageOptionList() {

		HashMap<String, LanguageConfigurationClassDescriptor> ldesc = LanguagesConfiguration
				.getLanguageConfigurationBlock();
		LanguageConfigurationClassDescriptor sub = null;
		for (Iterator<String> it = ldesc.keySet().iterator(); it.hasNext();) {
			sub = (LanguageConfigurationClassDescriptor) ldesc.get(it.next());
			if (sub.isEnabled) {
				c.gridx = 0;
				c.gridy = ++this.y_coordinate;
				String lang = new String(sub.getLanguageNickname()
						+ sub.getType());
				JLabel languageLabel = new JLabel(lang);
				add(languageLabel, c);
				this.optionsObj.getLabels().put(lang, false);
				c.gridx = 1;
				JCheckBox languageImportCheckBox = new JCheckBox();
				languageImportCheckBox.setActionCommand(lang);
				languageImportCheckBox.addActionListener(this);
				add(languageImportCheckBox, c);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		log.trace("command received " + command);

		if (this.optionsObj.getLabels().containsKey(command))
			this.optionsObj.setLabels(command, ((JCheckBox) e.getSource())
					.isSelected());

		else if (command == "openbutton") {
			int returnVal = fileChooser
					.showOpenDialog(UserProfileExporterGui.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				this.optionsObj.setInputFile(file);
				textAreaLog.append("opening file " + file.getName() + ".\n");
				this.startImportButton.setEnabled(true);
			} else {
				textAreaLog.append("open command cancelled by user.\n");
			}

		} else if (command.equals("import")) {

			try {

				this.optionsObj.setTypeOfImport((String) this.typeSelectCombo
						.getSelectedItem());

				// if category check box has been selected import will  occur
				if(this.categoryImportCheckBox.isSelected()) {
					System.out.println("categoryImportCheckBox");
					int count = dit.loadCategoryDatabase(optionsObj);
					textAreaLog.append("imported section table "+
							count +" objects\n");
					
				}
				
				if(this.sectionImportCheckBox.isSelected()) {
					System.out.println("sectionImportCheckBox");
					int count = dit.loadSectionDatabase(optionsObj);
					textAreaLog.append("imported category table "+
							count + "objects\n");
				}	
				
				if (this.optionsObj.getLabels().size() > 0) {
					HashMap<String, Integer> resultMap = dit
							.loadDatabase(optionsObj);

					Iterator<String> it = resultMap.keySet().iterator();
					while (it.hasNext()) {
						String key = it.next();
						textAreaLog.append("imported language " + key + ": "
								+ resultMap.get(key) + " objects\n");
					}
				}
				
			} catch (IOException ex) {
				ex.printStackTrace();
				log.error("IOException "+ex.getMessage());
				JOptionPane.showMessageDialog(frame, ex.getMessage());
			} catch (DatabaseException ex) {
				ex.printStackTrace();
				log.error("DatabaseException "+ex.getMessage());
				JOptionPane.showMessageDialog(frame, ex.getMessage());
			} catch (KeyNotFoundException ex) {
				ex.printStackTrace();
				log.error("KeyNotFoundException "+ex.getMessage());
				JOptionPane.showMessageDialog(frame, ex.getMessage());
			} catch (BiffException ex) {
				ex.printStackTrace();
				log.error("BiffException "+ex.getMessage());
				JOptionPane.showMessageDialog(frame,ex.getMessage());
			} catch (LabelNotFoundException ex) {
				ex.printStackTrace();
				log.error("LabelNotFoundException "+ex.getMessage());
				JOptionPane.showMessageDialog(frame, ex.getMessage());
			} catch (DatabaseImportException ex) {
				ex.printStackTrace();
				log.error("DatabaseImportException "+ex.getMessage());
				JOptionPane.showMessageDialog(frame, ex.getMessage());
			}
		}

	}

}
