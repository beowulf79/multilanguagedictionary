package net.verza.jdict.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import java.util.Iterator;
import com.sleepycat.je.DatabaseException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.verza.jdict.IWord;
import net.verza.jdict.sleepycat.datastore.SleepyCategoryDatabase;
import net.verza.jdict.sleepycat.datastore.SleepyCategoryDatabaseReader;
import net.verza.jdict.sleepycat.SleepyEnvironment;
import net.verza.jdict.sleepycat.datastore.SleepySectionDatabase;
import net.verza.jdict.sleepycat.datastore.SleepySectionDatabaseReader;
import org.apache.log4j.Logger;


/**
 * @author ChristianVerdelli
 * 
 */

abstract class WordInfoDialogBox {

	private static Logger log;
	
	public WordInfoDialogBox() {
	}

	public static void DisplayWordInfo(Object obj) {
		SleepySectionDatabaseReader sectionReader = null;
		SleepyCategoryDatabaseReader categoryReader = null;
		try {
			log = Logger.getLogger("XMLCOMPARER_Logger");
			SleepyCategoryDatabase categoryDatabase = new SleepyCategoryDatabase(
					SleepyEnvironment.getInstance());
			categoryReader = new SleepyCategoryDatabaseReader(categoryDatabase);
			SleepySectionDatabase sectionDatabase = new SleepySectionDatabase(
					SleepyEnvironment.getInstance());
			sectionReader = new SleepySectionDatabaseReader(sectionDatabase);

			IWord word = (IWord) obj;
			log.trace("opening word info frame");
			JFrame wordInfoDialogBoxFramfe = new JFrame();
			wordInfoDialogBoxFramfe.setBounds(5, 111, 252, 140);
			JPanel jpnl = new JPanel(new BorderLayout());
			jpnl.setBackground(Color.blue);
			jpnl.setLayout(new GridBagLayout());
			jpnl.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5 pixels
																	// gap to
																	// the
																	// borders
			wordInfoDialogBoxFramfe.getContentPane().add(BorderLayout.CENTER,
					jpnl);

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(2, 2, 2, 2);// add some space between
												// components to avoid clutter
			c.anchor = GridBagConstraints.WEST;// anchor all components WEST
			c.weightx = 0.1;// all components use vertical available space
			c.weighty = 0.1;// all components use horizontal available space
			c.gridheight = 1;
			c.gridwidth = 1;

			c.gridx = 0;
			c.gridy = 0;
			JLabel jlbl1 = new JLabel("Singular/Past");
			jpnl.add(jlbl1, c);

			c.gridx = 1;
			JTextField jtxf1 = new JTextField(new String(word.getsingular()
					.getBytes(), "UTF-8"));
			jtxf1.setColumns(10);
			jtxf1.setEditable(false);
			jpnl.add(jtxf1, c);

			c.gridx = 0;
			c.gridy = 1;
			JLabel jlbl2 = new JLabel("Plural/Present");
			jpnl.add(jlbl2, c);

			c.gridx = 1;
			JTextField jtxf2 = new JTextField("not defined for this word");
			if(word.getplural() != null )
				jtxf2.setText(new String(word.getplural().getBytes(), "UTF-8"));
			jtxf2.setColumns(12);
			jtxf2.setEditable(false);
			jpnl.add(jtxf2, c);

			c.gridx = 0;
			c.gridy = 2;
			JLabel jlbl3 = new JLabel("Audio Stream Size");
			jpnl.add(jlbl3, c);

			c.gridx = 1;
			JTextField jtxf3 = new JTextField("0 bytes");
			if (word.getaudio() != null) {
				byte stream[] = (byte[]) word.getaudio();
				jtxf3.setText(new Integer(stream.length).toString() + " bytes");
			}
			jtxf3.setEditable(false);
			jtxf3.setColumns(10);
			jpnl.add(jtxf3, c);

			c.gridx = 0;
			c.gridy = 3;
			JLabel jlbl4 = new JLabel("Comment");
			jpnl.add(jlbl4, c);

			c.gridx = 1;
			JTextField jtxf4 = new JTextField("not defined for this word");
			jtxf4.setEditable(false);
			jtxf4.setColumns(12);
			if (word.getcomment() != null)
				jtxf4.setText(word.getcomment());
			jpnl.add(jtxf4, c);

			c.gridx = 0;
			c.gridy = 4;
			JLabel jlbl5 = new JLabel("Section");
			jpnl.add(jlbl5, c);

			c.gridx = 1;
			JTextField jtxf5 = new JTextField("not defined for this word");
			jtxf5.setColumns(12);
			jtxf5.setEditable(false);
			String section = new String();
			log.debug("section Set size " + word.getsection().size());
			if (word.getcategory().size() > 0)
				for (Iterator<String> it = word.getsection().iterator(); it
						.hasNext();) {
					section = section.concat(sectionReader.searchCategory(it
							.next())+", ");

				}
			
			
			if (word.getsection() != null )
				jtxf5.setText(section.toString());
			jpnl.add(jtxf5, c);

			c.gridx = 0;
			c.gridy = 5;
			JLabel jlbl6 = new JLabel("Category");
			jpnl.add(jlbl6, c);

			c.gridx = 1;
			JTextField jtxf6 = new JTextField("not defined for this word");
			jtxf6.setColumns(12);
			jtxf6.setEditable(false);
			String category = new String();
			log.debug("category set size " + word.getcategory().size());
			if (word.getcategory().size() > 0)
				for (Iterator<String> it = word.getcategory().iterator(); it
						.hasNext();) {
					category = category.concat(categoryReader.searchCategory(it
							.next())+", ");

				}
			
			if (word.getcategory() != null )
				jtxf6.setText(category);
			jpnl.add(jtxf6, c);


			wordInfoDialogBoxFramfe.setResizable(false);
			wordInfoDialogBoxFramfe.getContentPane().add(jpnl);
			wordInfoDialogBoxFramfe.pack();
			wordInfoDialogBoxFramfe.setVisible(true);
			wordInfoDialogBoxFramfe.setSize(300, 350);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
