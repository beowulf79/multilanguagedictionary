package net.verza.jdict.model;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.exceptions.LinkIDException;
import net.verza.jdict.gui.GUIPreferences;
import net.verza.jdict.properties.LanguagesConfiguration;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

public class EditorController extends JPanel implements ActionListener {

    public static final String NOT_SELECTED_STRING = "---- Nothing Selected ----";
    public static final String COMBO_LANGUAGE_CHANGE = "Language";
    public static final String NEXT_BUTTON_STRING = "Next";
    public static final String FINISH_BUTTON_STRING = "Finish";
    public static final String CONFIRM_MODIFY_NEW = "Proceed to modify new inserted object";
    public static final int EDITOR_POSITION = 2;
    public static final int BUTTONS_Y_POSITION = 3;
    private static final long serialVersionUID = 1L;
    private static Logger log;

    private SearchableObjectEditor searchableObjectEditor;
    private Dictionary dit;
    private static EditorController singleton = null;
    private JFrame frame;
    private JPanel panel;
    private GridBagConstraints c;
    private JButton next, finish;

    private Map<Integer, String> transactionMap;

    public EditorController() {
	super();
	singleton = this;
	log = Logger.getLogger("jdict");
	log.trace("Initialazing WordCreatorController class");
	transactionMap = new HashMap<Integer, String>();
	try {
	    dit = Factory.getDictionary();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DatabaseException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DataNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DynamicCursorException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (KeyNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (NoSuchMethodException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (InstantiationException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	}
    }

    public void setData(String language, Integer key) {
	log.trace("called method setData with language " + language
		+ " and object " + key);
	transactionMap.put(key, language);
	initComponents();
	createAndShowGUI();
    }

    public void setData(Map<Integer, String> map) {
	log.trace("called method setData with map of size " + map.size());
	transactionMap = map;
	initComponents();
	createAndShowGUI();
    }

    private void initComponents() {
	log.trace("called function initComponents");
	panel = new JPanel(new GridBagLayout());
	panel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
	panel.setBackground(GUIPreferences.backgroundColor);
	c = new GridBagConstraints();
	c.insets = new Insets(2, 2, 2, 2);
	c.anchor = GridBagConstraints.WEST;
	c.weightx = 0.1;
	c.weighty = 0.1;
	c.gridheight = 1;
	c.gridwidth = 1;

	try {
	    loadNextSearchableObject();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DatabaseException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DynamicCursorException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (DataNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (KeyNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	}

	c.gridx = 0;
	c.gridy = BUTTONS_Y_POSITION;
	c.anchor = GridBagConstraints.EAST;
	next = new JButton(NEXT_BUTTON_STRING);
	next.addActionListener(this);
	next.setActionCommand(NEXT_BUTTON_STRING);
	panel.add(next, c);

	c.anchor = GridBagConstraints.WEST;
	c.gridx = 1;
	finish = new JButton(FINISH_BUTTON_STRING);
	finish.setActionCommand(FINISH_BUTTON_STRING);
	finish.addActionListener(this);
	panel.add(finish, c);

    }

    private void createAndShowGUI() {
	log.trace("called functio createAndShowGUI");
	frame = new JFrame("EditorController");
	frame.addWindowListener(new MainFrameCloser(this));
	frame.setSize(500, 170);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.getContentPane().add(panel);
	frame.setBackground(GUIPreferences.backgroundColor);
	frame.setResizable(true);
	frame.setLocationRelativeTo(null);
	frame.pack();
	frame.setVisible(true);
    }

    public static EditorController getInstance() {
	if (singleton == null)
	    singleton = new EditorController();
	return singleton;
    }

    private void destroyInstance() {
	log.trace("called function destroyInstance");
	if (singleton != null)
	    singleton = null;
    }

    public class MainFrameCloser extends java.awt.event.WindowAdapter {
	protected EditorController frame = null;

	public MainFrameCloser(EditorController frame) {
	    super();
	    log.trace("called function MainFrameCloser");
	    this.frame = frame;
	}

	public void windowClosed(WindowEvent e) {
	    log.trace("called function windowClosed");
	    frame.destroyInstance();
	}
    }

    private void loadNextSearchableObject()
	    throws UnsupportedEncodingException, DatabaseException,
	    DynamicCursorException, DataNotFoundException, KeyNotFoundException {
	log.debug("called function loadNextSearchableObject");
	Map.Entry<Integer, String> pairs = null;
	Iterator<Map.Entry<Integer, String>> it = transactionMap.entrySet()
		.iterator();
	if (it.hasNext()) {
	    pairs = it.next();
	    transactionMap.remove(pairs.getKey());
	    try {
		String language = pairs.getValue();
		String classToLoadName = LanguagesConfiguration
			.getLanguageMainConfigNode(language).getEditorClass();

		SearchableObject searchableObject = dit.read(language, pairs
			.getKey().toString());
		if (searchableObject == null) {
		    log.error("cannot find searchable object in db " + language
			    + " using primary key " + pairs.getKey());
		    new DataNotFoundException(
			    "cannot find searchableobject in db");
		}
		log.debug("language " + language + " classToLoad "
			+ classToLoadName + " searchable object "
			+ searchableObject);

		Class<?> toRun = Class.forName(classToLoadName);
		Class<?>[] c_arr = new Class[] {
			net.verza.jdict.model.SearchableObject.class,
			String.class };
		Constructor<?> constr = toRun.getConstructor(c_arr);

		searchableObjectEditor = (SearchableObjectEditor) constr
			.newInstance(searchableObject, language);

		// add new searchableObjectEditor on the panel
		c.gridx = 0;
		c.gridy = EDITOR_POSITION;
		c.anchor = GridBagConstraints.EAST;
		searchableObjectEditor.initComponents();
		searchableObjectEditor.setBorder(BorderFactory
			.createTitledBorder(BorderFactory.createLineBorder(
				GUIPreferences.borderColor, 3), language));
		c.gridwidth = 2;
		panel.add(searchableObjectEditor, c);

	    } catch (HeadlessException exp) {
		JOptionPane.showMessageDialog(frame, exp.getMessage());
		log.error("HeadlessException: " + exp.getMessage());
		exp.printStackTrace();
	    } catch (IllegalArgumentException exp) {
		JOptionPane.showMessageDialog(frame, exp.getMessage());
		log.error("IllegalArgumentException: " + exp.getMessage());
		exp.printStackTrace();
	    } catch (IllegalAccessException exp) {
		JOptionPane.showMessageDialog(frame, exp.getMessage());
		log.error("IllegalAccessException: " + exp.getMessage());
		exp.printStackTrace();
	    } catch (InvocationTargetException exp) {
		JOptionPane.showMessageDialog(frame, exp.getMessage());
		log.error("InvocationTargetException: " + exp.getMessage());
		exp.printStackTrace();
	    } catch (LanguagesConfigurationException e1) {
		JOptionPane.showMessageDialog(frame, e1.getMessage());
		log.error("LanguagesConfigurationException " + e1.getMessage());
		e1.printStackTrace();
	    } catch (SecurityException e1) {
		JOptionPane.showMessageDialog(frame, e1.getMessage());
		log.error("SecurityException " + e1.getMessage());
		e1.printStackTrace();
	    } catch (ClassNotFoundException e1) {
		JOptionPane.showMessageDialog(frame, e1.getMessage());
		log.error("ClassNotFoundException " + e1.getMessage());
		e1.printStackTrace();
	    } catch (NoSuchMethodException e1) {
		JOptionPane.showMessageDialog(frame, e1.getMessage());
		log.error("NoSuchMethodException " + e1.getMessage());
		e1.printStackTrace();
	    } catch (InstantiationException e1) {
		JOptionPane.showMessageDialog(frame, e1.getMessage());
		log.error("InstantiationException " + e1.getMessage());
		e1.printStackTrace();
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (LinkIDException e) {
		e.printStackTrace();
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    }
	} else
	    next.setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
	log.trace("called function actionPerformed");
	try {
	    if (e.getActionCommand().equals(NEXT_BUTTON_STRING)) {

		log.trace("received command " + NEXT_BUTTON_STRING);
		// send write command and remove previous word from the panel
		searchableObjectEditor.write();
		panel.remove(searchableObjectEditor);
		loadNextSearchableObject();
		frame.pack();
		frame.validate();

	    } else if (e.getActionCommand().equals(FINISH_BUTTON_STRING)) {
		log.trace("received command " + FINISH_BUTTON_STRING);
		frame.dispose();
	    }
	} catch (DatabaseException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("DatabaseException: " + exp.getStackTrace());
	    exp.printStackTrace();
	} catch (HeadlessException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("HeadlessException: " + exp.getMessage());
	} catch (UnsupportedEncodingException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("UnsupportedEncodingException: " + exp.getMessage());
	} catch (KeyNotFoundException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("KeyNotFoundException: " + exp.getMessage());
	} catch (DynamicCursorException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("DynamicCursorException: " + exp.getMessage());
	} catch (DataNotFoundException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("DataNotFoundException: " + exp.getMessage());
	} catch (IllegalArgumentException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("IllegalArgumentException: " + exp.getMessage());
	    exp.printStackTrace();
	} catch (IllegalAccessException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("IllegalAccessException: " + exp.getMessage());
	    exp.printStackTrace();
	} catch (InvocationTargetException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("InvocationTargetException: " + exp.getMessage());
	    exp.printStackTrace();
	} catch (MalformedURLException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("MalformedURLException: " + exp.getMessage());
	    exp.printStackTrace();
	} catch (FileNotFoundException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("FileNotFoundException: " + exp.getMessage());
	    exp.printStackTrace();
	} catch (IOException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("IOException: " + exp.getMessage());
	    exp.printStackTrace();
	} catch (SecurityException exp) {
	    JOptionPane.showMessageDialog(frame, exp.getMessage());
	    log.error("SecurityException " + exp.getMessage());
	    exp.printStackTrace();
	} catch (ClassNotFoundException exp) {
	    exp.printStackTrace();
	    log.error("Exception " + exp.getMessage());
	    JOptionPane.showMessageDialog(null, exp.getMessage());
	} catch (NoSuchMethodException exp) {
	    exp.printStackTrace();
	    log.error("Exception " + exp.getMessage());
	    JOptionPane.showMessageDialog(null, exp.getMessage());
	} catch (InstantiationException exp) {
	    exp.printStackTrace();
	    log.error("Exception " + exp.getMessage());
	    JOptionPane.showMessageDialog(null, exp.getMessage());
	}

    }

}
