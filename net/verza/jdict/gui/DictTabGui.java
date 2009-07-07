package net.verza.jdict.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.HashMap;
import java.util.Vector;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.*;
import net.verza.jdict.Configuration;
import net.verza.jdict.SearchableObject;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.sleepycat.je.DatabaseException;

/**
 * @author Christian Verdelli
 */

public class DictTabGui extends JPanel implements ActionListener {

	public static final String NOT_SELECTED_STRING = "---- Nothing Selected ----";
	private static final long serialVersionUID = 1L;
	private static Logger log;
	private static DictTabGui instance = null;
	private Dictionary dit;
	private GridBagConstraints c;
	private JFrame frame;
	private JComboBox srcLangCombo, dstLangCombo;
	private JTextField jtxf1;
	private HashMap<String, String[]> translations;
	private JTextArea jtxa1;

	public DictTabGui() {
		super();

		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("called class " + this.getClass().getName());
		translations = new HashMap<String, String[]>();
		initComponents(); // initialize components graphics
		PropertyConfigurator.configure(Configuration.LOG4JCONF);

	}

	/*
	 * Costruisce la mappa con i linguaggi enabled a per ogni linguaggio la
	 * lista dei linguaggi per i quali è disponibile la traduzioneJ.
	 * 
	 */
	private void buildLanguageMenu() {

		translations = LanguageSelection.buildLanguageMenu();

		// add JComboBox to choose the languages of the lookup
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		JLabel srcLangJLabel = new JLabel("Select language to lookup");
		srcLangJLabel.setBorder(BorderFactory.createLineBorder(GUIPreferences.borderColor, GUIPreferences.borderThickness));
		add(srcLangJLabel, c);
		c.gridx = 1;
		srcLangCombo = new JComboBox(translations.keySet().toArray());
		srcLangCombo.setActionCommand("src_lang_selection_change");
		srcLangCombo.addItem(NOT_SELECTED_STRING);
		srcLangCombo.setSelectedIndex(srcLangCombo.getItemCount() - 1);
		srcLangCombo.addActionListener(this);
		add(srcLangCombo, c);

	}

	private void initComponents() {

		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(new Insets(5, 5, 5, 5))); // 5 pixels gap to
		// the borders

		c = new GridBagConstraints(); // add some space between components to

		c.insets = new Insets(2, 2, 2, 2); // avoid clutter
		c.anchor = GridBagConstraints.NORTHEAST;// anchor all components WEST
		c.weightx = 0.1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		buildLanguageMenu(); // builds the JComboBox

		// add JLabel and JTextfield for the user input
		c.gridx = 0;
		c.gridy = 2;
		JLabel lookupTextJLabel = new JLabel("enter the word to lookup");
		lookupTextJLabel.setBorder(BorderFactory.createLineBorder(GUIPreferences.borderColor, GUIPreferences.borderThickness));
		add(lookupTextJLabel, c);
		c.gridx = 1;
		jtxf1 = new JTextField("felice");
		add(jtxf1, c);

		// add JButton to send the search
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		JButton jbt1 = new JButton("search");
		jbt1.addActionListener(this);
		add(jbt1, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.gridheight = 3;
		c.weighty = 0.1;
		c.fill = GridBagConstraints.BOTH;
		jtxa1 = new JTextArea();
		jtxa1.setColumns(5);
		jtxa1.setLineWrap(true);
		jtxa1.setRows(10);
		jtxa1.setWrapStyleWord(true);
		jtxa1.setEditable(false);
		add(jtxa1, c);

		setVisible(true);

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

	public void actionPerformed(ActionEvent evt) {

		String command = evt.getActionCommand();

		// add JComboBox to choose the languages of the lookup
		if (command.equals("src_lang_selection_change")) {
			JLabel dst = new JLabel("select translation to resolve");
			dst.setBorder(BorderFactory.createLineBorder(GUIPreferences.borderColor, GUIPreferences.borderThickness));
			if (this.srcLangCombo.getSelectedItem().equals(NOT_SELECTED_STRING)) {
				remove(this.dstLangCombo);
				remove(dst);
			} else {
				if (this.dstLangCombo != null)
					remove(this.dstLangCombo);

				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 1;
				c.gridheight = 1;
				c.gridwidth = 1;
				c.weightx = 0.1;
				c.weighty = 0;
				add(dst, c);
				c.gridx = 1;
				this.dstLangCombo = new JComboBox(this.translations
						.get(this.srcLangCombo.getSelectedItem()));
				dstLangCombo.addItem(NOT_SELECTED_STRING);
				dstLangCombo.setSelectedIndex(dstLangCombo.getItemCount() - 1);
				add(this.dstLangCombo, c);
				this.revalidate();
				setVisible(true);
			}
		}

		else if (command.equals("search")) {

			Vector<SearchableObject> objs = new Vector<SearchableObject>();

			try {
				dit = Factory.getDictionary();

				if (this.dstLangCombo.getSelectedItem().toString().indexOf(
						"audio") != -1)
					objs.add(dit.read(this.srcLangCombo.getSelectedItem()
							.toString(), jtxf1.getText().toString()));

				else
					objs = dit.read((String) srcLangCombo.getSelectedItem(),
							new String(jtxf1.getText().getBytes("UTF-8")),
							(String) dstLangCombo.getSelectedItem());

				if (objs == null) {
					JOptionPane.showMessageDialog(frame,
							"data not found in the dictionary");
					return;
				}

				JPanel internal = new JPanel();
				internal.setBackground(new Color(0.0f, 1.0f, 0.0f, 0.35f));
				internal.setLayout(new GridBagLayout());
				GridBagConstraints d = new GridBagConstraints();
				d.insets = new Insets(2, 2, 2, 2); // avoid clutter
				d.anchor = GridBagConstraints.NORTH;// anchor all components WEST
				d.fill = GridBagConstraints.HORIZONTAL;
				d.weightx = 1.0;
				d.weighty = 1.0;
				d.gridx = 0;
				d.gridy = 0;
				int y = 0;

				for (int i = 0; i < objs.size(); i++) {
					SearchableObject sObj = objs.get(i);
					if (sObj == null)
						continue;
					d.gridy = y++;
					JTable table = sObj.getTable();
					table.setRowHeight(30);
					internal.add(table, d);
					internal.add(new JSeparator(), d);
				}

				// Add the JPanel into the Frame
				c.gridx = 0;
				c.gridy = 4;
				c.gridwidth = 2;
				c.gridheight = 3;
				c.ipadx = 150;c.ipady = 150;	
				c.fill = GridBagConstraints.BOTH;

				remove(jtxa1);
				remove(internal);
				internal.revalidate();
				internal.setVisible(true);
				add(internal, c);

			} catch (UnsupportedEncodingException e) {
				log.error("UnsupportedEncodingException: " + e.getMessage());
				JOptionPane.showMessageDialog(frame, e.getMessage());
				return;
			} catch (DatabaseException e) {
				JOptionPane.showMessageDialog(frame, e.getMessage());
				log.error("DatabaseException: " + e.getMessage());
				return;
			} catch (DataNotFoundException e) {
				log.warn("not found in the directory");
				JOptionPane.showMessageDialog(frame, e.getMessage());
				return;
			} catch (DynamicCursorException e) {
				JOptionPane.showMessageDialog(frame, e.getMessage());
				log.error("DynamicCursorException: " + e.getMessage());
				return;
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(frame, e.getMessage());
				log.error("DynamicCursorException: " + e.getMessage());
				return;
			} catch (KeyNotFoundException e) {
				JOptionPane.showMessageDialog(frame, e.getMessage());
				log.error("DynamicCursorException: " + e.getMessage());
				return;
			} catch (LinkIDException e) {
				JOptionPane.showMessageDialog(frame, e.getMessage());
				log.error("LinkIDException: " + e.getMessage());
				return;
			}

		}
	}
}
