package net.verza.jdict.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.nio.charset.Charset;
//import net.verza.jdict.Dictionary;
//import net.verza.jdict.sleepycat.Factory;
import net.verza.jdict.verbs.ArabVerbFormInterface;
import org.apache.log4j.Logger;
import java.lang.reflect.Constructor;

/**
 * @author Christian Verdelli
 */

public class VerbTabGui extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_TIME_INDEX = 0;
	private static final String PRESENT_LABEL = "Present";
	private static final String PAST_LABEL = "Past";
	private static final String IMPERATIVE_LABEL = "imperative";

	private static Logger log;

	//private Dictionary dit;
	private VerbResultTable vrJTable;
	private VerbTabActions handler;
	private GridBagConstraints c;

	String[] verbArray = { PRESENT_LABEL, PAST_LABEL, IMPERATIVE_LABEL };

	// Graphic Components Variable
	private JFrame frame;
	private JPanel jpnl1;
	private JTextArea jtxa1;
	private JTextField jtxf1;
	private JLabel jlb1;
	private JLabel jlb2;
	private JLabel jlb3;
	private JScrollPane scrlp1;
	private JButton jbt1;
	private JComboBox jcmbx1;
	private JSpinner jspr1;

	public VerbTabGui() {
		super();

		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("Starting...");
		handler = new VerbTabActions();
		// scrlp1 = new JScrollPane(jtxa1);
		initComponents();
	}

	private void initComponents() {

		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(new Insets(5, 5, 5, 5))); // 5 pixels gap to
															// the borders

		c = new GridBagConstraints();
		c.insets = new Insets(2, 2, 2, 2); // add some space between components
											// to avoid clutter
		c.anchor = GridBagConstraints.WEST; // anchor all components WEST
		c.fill = GridBagConstraints.NONE; // all components do not use all
											// available space
		c.weightx = 1.0;// all components use vertical available space
		c.weighty = 0.1;// all components use horizontal available space
		c.gridheight = 1; // all components are one cell size high
		c.gridwidth = 1; // all components are one cell size wide

		c.gridx = 0;
		c.gridy = 0;
		jlb1 = new JLabel("Type the verb here");
		add(jlb1, c);

		c.gridx = 1;
		jlb2 = new JLabel("Select the Paradigm");
		add(jlb2, c);

		c.gridx = 2;
		jlb3 = new JLabel("Select the Form");
		add(jlb3, c);

		// input text field
		c.gridx = 0;
		c.gridy = 1;

		jtxf1 = new JTextField("\u0625" + "\u0631" + "\u0633" + "\u0644"); // corresponds
																			// to
																			// Arabic
																			// IRSAL
		jtxf1.setColumns(8);
		add(jtxf1, c);

		// Spinner to select the paradigm code
		c.gridx = 1;
		c.anchor = GridBagConstraints.CENTER;
		jspr1 = new JSpinner();
		jspr1.setValue(new Integer(100));
		add(jspr1, c);

		// combo box for selecting the Time
		c.gridx = 2;
		c.anchor = GridBagConstraints.WEST;
		jcmbx1 = new JComboBox(verbArray);
		jcmbx1.setSelectedIndex(DEFAULT_TIME_INDEX);
		jcmbx1.addActionListener(handler);
		add(jcmbx1, c);

		// conjugate button
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		jbt1 = new JButton("Conjugate");
		jbt1.addActionListener(handler);
		add(jbt1, c);

		// add the JTextArea
		c.gridy = 3;
		c.gridheight = 3;
		c.gridwidth = 3;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		jtxa1 = new JTextArea();
		jtxa1.setColumns(20);
		jtxa1.setLineWrap(true);
		jtxa1.setRows(5);
		jtxa1.setWrapStyleWord(true);
		jtxa1.setEditable(false);
		add(jtxa1, c);

	}

	public class VerbTabActions implements ActionListener {

		private String command;
		JFileChooser fc;
		File openFile;
		File saveFile;
		String text;
		Charset cs;

		public VerbTabActions() {
			command = new String();
			text = null;

		}

		public void actionPerformed(ActionEvent evt) {

			command = evt.getActionCommand();
			String con = null;
			try {
				if (command.equals("Conjugate")) {
					con = ((String) jcmbx1.getSelectedItem());
					System.out.println("Select Form -"
							+ jcmbx1.getSelectedItem().toString());

					Class<?> toRun = Class.forName(con);
					Class<?>[] c_arr = new Class[] { char[].class, String.class };
					Constructor<?> constr = toRun.getConstructor(c_arr);
					ArabVerbFormInterface obj = (ArabVerbFormInterface) constr
							.newInstance(jtxf1.getText().toCharArray(), // The
																		// root
																		// of
																		// the
																		// verb
									jspr1.getValue().toString()); // The
																	// Paradigm

					vrJTable = new VerbResultTable(obj.get_all_Form());
					jpnl1.remove(jtxa1);
					scrlp1 = new JScrollPane(vrJTable);
					jpnl1.add(scrlp1, c);
					frame.repaint();
				}
			} catch (ClassNotFoundException e) {
				System.out.println(e);
			} catch (NoSuchMethodException e) {
				System.out.println(e);
			} catch (InstantiationException e) {
				System.out.println(e);
			} catch (IllegalAccessException e) {
				System.out.println(e);
			} catch (InvocationTargetException e) {
				System.out.println(e);
			}

		}
	}

}
