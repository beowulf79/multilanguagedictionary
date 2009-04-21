package net.verza.jdict.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguagesConfiguration;
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

public class DictTabGui extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static Logger log;
	private static DictTabGui instance = null;
	private Dictionary dit;
	private DictTabActions handler;
	private GridBagConstraints c;
	private JFrame frame;
	private JComboBox languageSelection;
	private JTextField jtxf1;
	private static String[] languagesArray;
	Graphics g;
	
	public DictTabGui() {
		super();

		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("called class " + this.getClass().getName());
		
		handler = new DictTabActions(); // initialize events handler for this class
		buildLanguageMenu(); // builds the JComboBox 
		initComponents(); // initialize components graphics
		PropertyConfigurator.configure(Configuration.LOG4JCONF);
		
    	
	}

	public void paintComponent(Graphics _g)
    {
	  g = _g;
      super.paintComponent(g);

    }
	
	/*
	 * Costruisce il menu JComboBox di scelta dei linguaggi.
	 * Ottiene i linguaggi definiti nell'oggetto LanguageConfiguration, e per ciascuno di 
	 * essi se abilitato, ottiene le traduzioni disponibili;  per ogni traduzioni disponibile
	 * controlla che sia abilitata; in caso positivo inserisce linguaggio-> traduzione nel
	 * menu JComboBox di scelta. 
	 */
	private void buildLanguageMenu()	{	
		log.trace("inside function buildLanguageMenu");
		String[] tmp = new String[30];
		HashMap<String, LanguageConfigurationClassDescriptor> ldesc = LanguagesConfiguration
															.getLanguageConfigurationBlock();
		LanguageConfigurationClassDescriptor sub = null;
		
		int ext_counter=0;
		for (Iterator<String> it = ldesc.keySet().iterator(); it.hasNext();) {
			sub = (LanguageConfigurationClassDescriptor) ldesc.get(it
					.next());
		
			String nickname = sub.getLanguageNickname();
			String type = sub.getType();
			log.debug("languge " + nickname + " type " + type);
		
			// check if the language is enabled , if not skip to the next
			// language
			if (!sub.getIsEnabled()) {
				log.info("language " + nickname
						+" type " + type
						+ " not enabled, skipping to next language");
				continue;
			}
			//read the property in the configuration file
			String[] translation = sub.getTranslations();
			// check if each  translation are enabled; if so add them to the JComboBox
			for(int int_counter=0;int_counter<translation.length;int_counter++)	{
				LanguageConfigurationClassDescriptor transl = 
					LanguagesConfiguration.getLanguageMainConfigNode(translation[int_counter]+type);
				log.debug("getting configuration of the language "+translation[int_counter]+type);
				if(transl.getIsEnabled())	{
					log.trace("adding to JcomboBox language "+translation[int_counter]
				                                             + type);
					tmp[ext_counter++] =  nickname 
											+ type
											+"->"
											+ transl.getLanguageNickname() 
											+ transl.getType();
				}	
			}
		}

		languagesArray = new String[ext_counter];
        System.arraycopy(tmp, 0, languagesArray, 0,
        		ext_counter);
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
		jtxf1 = new JTextField("happy");
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

	
	public class DictTabActions implements ActionListener {

		JPanel jpnl;

		public DictTabActions() {
			jpnl = new JPanel();
		}

		public void actionPerformed(ActionEvent evt) {
			
			try {
				
				String[] tmp = new String((String)languageSelection.getSelectedItem()).split("->");
				String src_lang = tmp[0];
				String dst_lang = tmp[1];
				dit = Factory.getDictionary();
				Vector<SearchableObject> objs = dit.read(src_lang, 
														new String(jtxf1.getText().getBytes("UTF-8")), // JText string to look for
														dst_lang);
				if(objs == null) {
					JOptionPane.showMessageDialog(frame,"data not found in the dictionary");
					return;
				}
				
				if (jpnl != null) {
					remove(jpnl);
					jpnl = new JPanel();
				}
				int i;
				for ( i = 0; i < objs.size(); i++) {
					SearchableObject sObj = objs.get(i);
					if(sObj == null) continue;
					jpnl.add(sObj.getTable());
					jpnl.add(new JSeparator());
				}
				
				// Add the JPanel into the Frame
				c.gridx = 0;
				c.gridy = 3;
				c.weightx = 1.0;
				c.weighty = 1.0;
				c.fill = GridBagConstraints.BOTH;
				add(jpnl,c);
				
			} catch (UnsupportedEncodingException e) {
				log.error("UnsupportedEncodingException: " + e.getMessage());
				return;
			} catch (DatabaseException e) {
				log.error("DatabaseException: " + e.getMessage());
				return;
			} catch (DataNotFoundException e) {
				log.warn("not found in the directory");
				JOptionPane.showMessageDialog(frame,
						e.getMessage());
				return;
			} catch (DynamicCursorException e) {
				log.error("DynamicCursorException: " + e.getMessage());
				return;
			} catch (FileNotFoundException e) {
				log.error("DynamicCursorException: " + e.getMessage());
				return;
			} catch (KeyNotFoundException e) {
				log.error("DynamicCursorException: " + e.getMessage());
				return;
			}
			
			
	}

	}
	


	
	

}
