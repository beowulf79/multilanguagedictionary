package net.verza.jdict.model;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.verza.jdict.dataloaders.IAudioFileLoader;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.dictionary.sleepycat.SleepyFactory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.exceptions.LinkIDException;
import net.verza.jdict.gui.GUIPreferences;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguagesConfiguration;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

public class ArabWordEditor extends SearchableObjectEditor implements
	ActionListener {

    private static final long serialVersionUID = 1L;
    private static final String NOT_SELECTED_STRING = "---- Nothing Selected ----";
    private static final String SECTION_COMBO_COMMAND = "sectioncombochange";
    private static final String REMOVE_SECTION_BUTTON = "removesection";
    private static final String REMOVE_CATEGORY_BUTTON = "removecategory";
    private static final String CATEGORY_COMBO_COMMAND = "categorycombochange";
    private static final String SEARCH_BUTTON_COMMAND = "search words";
    private static final String CONNECT_WORDS_COMMAND = "connect words";
    private static final String REMOVE_WORDS_COMMAND = "remove words";
    private static final String LINKID_SEPARATOR = ": ";
    private static Logger log;
    private LanguageConfigurationClassDescriptor config;
    private ArabWord arabword;
    private Word tmpConnectedWord;
    private Map<SearchableObject, String> addMap, deleteMap;
    private Dictionary dit;
    private String mainObjectLanguage;
    private IAudioFileLoader audioLoader;

    private JPanel panel;
    private GridBagConstraints c;
    private JButton connectWordsButton;
    private JTextField singularText, pluralText, idText, searchText,
	    searchResult;
    private JTextArea notesArea;
    private JList sectionList, categoryList, linkIdList;
    private DefaultListModel sectionListModel, categoryListModel,
	    linkIdListModel;
    private JComboBox sectionCombo, categoryCombo, languageSelectorCombo;
    private JCheckBox loadAudio;

    public ArabWordEditor(String language) throws SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException,
	    LanguagesConfigurationException, UnsupportedEncodingException,
	    DatabaseException {
	super();
	log = Logger.getLogger("jdict");
	log
		.trace("called constructor WordEditor(LanguageConfigurationClassDescriptor languageConfiguration)");
	config = LanguagesConfiguration.getLanguageMainConfigNode(language);

	initializeAudioLoader();

	mainObjectLanguage = config.getLanguageNickname() + config.getType();
	if (arabword == null)
	    arabword = new ArabWord();
	try {
	    arabword.setid(SleepyFactory.getInstance().getDatabase(
		    mainObjectLanguage).getFreeId());

	    addMap = new HashMap<SearchableObject, String>();
	    deleteMap = new HashMap<SearchableObject, String>();

	    dit = Factory.getDictionary();
	} catch (FileNotFoundException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("FileNotFoundException " + e.getMessage());
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("UnsupportedEncodingException " + e.getMessage());
	    e.printStackTrace();
	} catch (DatabaseException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("DatabaseException " + e.getMessage());
	    e.printStackTrace();
	} catch (DataNotFoundException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("DataNotFoundException " + e.getMessage());
	    e.printStackTrace();
	} catch (DynamicCursorException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("DynamicCursorException " + e.getMessage());
	    e.printStackTrace();
	} catch (KeyNotFoundException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("KeyNotFoundException " + e.getMessage());
	    e.printStackTrace();
	}

    }

    public ArabWordEditor(SearchableObject _word, String language)
	    throws SecurityException, IllegalArgumentException,
	    ClassNotFoundException, NoSuchMethodException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException, LanguagesConfigurationException,
	    UnsupportedEncodingException, DatabaseException {
	this(language);
	log
		.trace("called constructor WordEditor(Word _word, LanguageConfigurationClassDescriptor languageConfiguration)");
	arabword = (ArabWord) _word;

    }

    public void initComponents() throws LanguagesConfigurationException {
	log.trace("called function initComponents");

	try {

	    panel = new JPanel(new GridBagLayout());
	    panel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));// 5
	    panel.setBackground(GUIPreferences.backgroundColor);
	    c = new GridBagConstraints();
	    c.insets = new Insets(2, 2, 2, 2);
	    c.anchor = GridBagConstraints.WEST;
	    c.weightx = 0.1;
	    c.weighty = 0.1;
	    c.gridheight = 1;
	    c.gridwidth = 1;
	    int y = 0;

	    c.gridx = 0;
	    c.gridy = y;
	    JLabel singularLabel = new JLabel("singular");
	    singularLabel
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(singularLabel, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    singularText = (("".equals(arabword.getsingular())) || (arabword
		    .getsingular() == null)) ? new JTextField(20)
		    : new JTextField(arabword.getsingular());
	    singularText
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(singularText, c);

	    c.gridx = 0;
	    c.gridy = y;
	    JLabel pluralLabel = new JLabel("plural");
	    pluralLabel
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(pluralLabel, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    pluralText = (("".equals(arabword.getplural())) || (arabword
		    .getplural() == null)) ? new JTextField(20)
		    : new JTextField(arabword.getplural());
	    pluralText
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(pluralText, c);

	    c.gridx = 0;
	    c.gridy = y;
	    JLabel sectionLabel = new JLabel("section");
	    sectionLabel
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(sectionLabel, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    sectionCombo = new JComboBox(dit.getSectionValue());
	    sectionCombo
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    sectionCombo.addItem(NOT_SELECTED_STRING);
	    sectionCombo.setSelectedItem(NOT_SELECTED_STRING);
	    sectionCombo.setActionCommand(SECTION_COMBO_COMMAND);
	    sectionCombo.addActionListener(this);
	    panel.add(sectionCombo, c);

	    c.gridx = 0;
	    c.gridy = y;
	    JLabel sectionListLabel = new JLabel("section");
	    sectionListLabel
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(sectionListLabel, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    sectionListModel = new DefaultListModel();
	    sectionList = (arabword.getsection() == null) ? new JList()
		    : new JList(arabword.getsection().toArray());
	    sectionList.setModel(sectionListModel);
	    sectionList
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(sectionList, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    JButton removeSectionButton = new JButton(
		    "remove selected section/s");
	    removeSectionButton.setActionCommand(REMOVE_SECTION_BUTTON);
	    removeSectionButton.addActionListener(this);
	    panel.add(removeSectionButton, c);

	    c.gridx = 0;
	    c.gridy = y;
	    JLabel categoryLabel = new JLabel("category");
	    categoryLabel
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(categoryLabel, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    categoryCombo = new JComboBox(dit.getCategoryValue());
	    categoryCombo
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    categoryCombo.addItem(NOT_SELECTED_STRING);
	    categoryCombo.setSelectedItem(NOT_SELECTED_STRING);
	    categoryCombo.setActionCommand(CATEGORY_COMBO_COMMAND);
	    categoryCombo.addActionListener(this);
	    panel.add(categoryCombo, c);

	    c.gridx = 0;
	    c.gridy = y;
	    JLabel categoryListLabel = new JLabel("category");
	    categoryListLabel
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(categoryListLabel, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    categoryListModel = new DefaultListModel();
	    categoryList = (arabword.getcategory() == null) ? new JList()
		    : new JList(arabword.getcategory().toArray());
	    categoryList.setModel(categoryListModel);
	    categoryList
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(categoryList, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    JButton removeCategoryButton = new JButton("remove category/s");
	    removeCategoryButton.setActionCommand(REMOVE_CATEGORY_BUTTON);
	    removeCategoryButton.addActionListener(this);
	    panel.add(removeCategoryButton, c);

	    c.gridx = 0;
	    c.gridy = y;
	    JLabel notesTextLabel = new JLabel("notes");
	    notesTextLabel
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(notesTextLabel, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    notesArea = (("".equals(arabword.getnotes())) || (arabword
		    .getnotes() == null)) ? new JTextArea(5, 20)
		    : new JTextArea(arabword.getnotes());
	    notesArea
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(notesArea, c);

	    c.gridx = 0;
	    c.gridy = y;
	    JLabel audioLoadLabel = new JLabel("load audio?");
	    audioLoadLabel
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(audioLoadLabel, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    loadAudio = new JCheckBox();
	    loadAudio
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(loadAudio, c);
	    add(panel);

	    c.gridx = 0;
	    c.gridy = y;
	    JLabel linkIdLabel = new JLabel("connected words");
	    linkIdLabel
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(linkIdLabel, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    linkIdListModel = new DefaultListModel();
	    linkIdList = new JList();
	    buildLinkIdList();
	    linkIdList.setModel(linkIdListModel);
	    panel.add(linkIdList, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    JButton deleteConnctedWords = new JButton("remove");
	    deleteConnctedWords.setActionCommand(REMOVE_WORDS_COMMAND);
	    deleteConnctedWords.addActionListener(this);
	    panel.add(deleteConnctedWords, c);

	    c.gridx = 0;
	    c.gridy = y;
	    JLabel searchLabel = new JLabel("search");
	    searchLabel
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(searchLabel, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    searchText = new JTextField(20);
	    searchText
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(searchText, c);

	    c.gridx = 0;
	    c.gridy = y;
	    JLabel languageLabel = new JLabel("select language");
	    languageLabel
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(languageLabel, c);

	    c.gridx = 1;
	    c.gridy = y++;
	    String[] translations = config.getTranslations();
	    languageSelectorCombo = new JComboBox(translations);
	    languageSelectorCombo.addItem(NOT_SELECTED_STRING);
	    languageSelectorCombo.setSelectedItem(NOT_SELECTED_STRING);
	    languageSelectorCombo.addActionListener(this);
	    panel.add(languageSelectorCombo, c);

	    c.gridx = 0;
	    c.gridy = y++;
	    JButton searchButton = new JButton("search");
	    searchButton.setActionCommand(SEARCH_BUTTON_COMMAND);
	    searchButton.addActionListener(this);
	    panel.add(searchButton, c);

	    c.gridx = 0;
	    c.gridy = y++;
	    searchResult = new JTextField(20);
	    searchResult.setEditable(false);
	    searchResult
		    .setBorder(BorderFactory.createLineBorder(
			    GUIPreferences.borderColor,
			    GUIPreferences.borderThickness));
	    panel.add(searchResult, c);

	    c.gridx = 0;
	    c.gridy = y++;
	    connectWordsButton = new JButton("connect  words");
	    connectWordsButton.setActionCommand(CONNECT_WORDS_COMMAND);
	    connectWordsButton.setEnabled(true);
	    connectWordsButton.addActionListener(this);
	    panel.add(connectWordsButton, c);

	} catch (UnsupportedEncodingException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("UnsupportedEncodingException " + e.getMessage());
	    e.printStackTrace();
	} catch (DatabaseException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("DatabaseException " + e.getMessage());
	    e.printStackTrace();
	} catch (DynamicCursorException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("DynamicCursorException " + e.getMessage());
	    e.printStackTrace();
	} catch (DataNotFoundException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("DataNotFoundException " + e.getMessage());
	    e.printStackTrace();
	} catch (KeyNotFoundException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("KeyNotFoundException " + e.getMessage());
	    e.printStackTrace();
	}

    }

    public void buildLinkIdList() throws LanguagesConfigurationException,
	    UnsupportedEncodingException, DatabaseException,
	    DynamicCursorException, DataNotFoundException, KeyNotFoundException {
	log.trace("called function buildLinkIdList");

	for (java.util.Map.Entry<String, Integer[]> entry : arabword
		.getlinkid().entrySet()) {
	    String language = entry.getKey();
	    // add only enabled languages
	    if (!LanguagesConfiguration.getLanguageMainConfigNode(language)
		    .isEnabled()) {
		log.debug("skipping disabled language " + mainObjectLanguage);
		continue;
	    }

	    log.debug("adding key " + language + " and value "
		    + entry.getValue() + " to the array");
	    Integer ids[] = entry.getValue();
	    for (Integer item : ids) {
		log.debug("searching language " + language + " and id " + item);
		Word obj;
		try {
		    obj = (Word) dit.read(language, item.toString());
		    if (obj == null) {
			log.error("broken link with language " + language
				+ " and id " + item);
			continue;
		    }
		    log.debug("adding to list word " + obj.getsingular());
		    linkIdListModel.addElement(language + LINKID_SEPARATOR
			    + obj.getsingular());
		} catch (SecurityException e) {
		    e.printStackTrace();
		    log.error("Exception " + e.getMessage());
		    JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (IllegalArgumentException e) {
		    e.printStackTrace();
		    log.error("Exception " + e.getMessage());
		    JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		    log.error("Exception " + e.getMessage());
		    JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (LinkIDException e) {
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

	}

    }

    public SearchableObject getSearchableObject() {
	return this.arabword;
    }

    public void write() throws KeyNotFoundException, DynamicCursorException,
	    DataNotFoundException, DatabaseException, IllegalArgumentException,
	    IllegalAccessException, InvocationTargetException,
	    MalformedURLException, FileNotFoundException, IOException {
	log.trace("called method write");

	if ((singularText.getText() == null)
		|| ("".equals(singularText.getText()))) {
	    log.error("singular field is null or empty");
	    throw new DatabaseException("singular cannot be empty");
	}
	arabword.setsingular(singularText.getText());
	arabword.setplural(pluralText.getText());

	if (loadAudio.isSelected())
	    arabword.setaudio(getAudio(arabword.getsingular()));

	if ((notesArea.getText() != null) || (notesArea.getText() != ""))
	    arabword.setnotes(notesArea.getText());

	// linkedId, section and category gets search at each jcombo change and
	// not here
	connectedWords();

	log.info("creating/updating searchable object " + arabword.toString());
	try {
	    dit.write(SleepyFactory.getInstance().getDatabase(
		    config.getLanguageNickname() + config.getType()), arabword);
	} catch (SecurityException e) {
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
	}

    }

    /**
     * @throws KeyNotFoundException
     * @throws UnsupportedEncodingException
     * @throws DynamicCursorException
     * @throws DataNotFoundException
     * @throws DatabaseException
     */
    private void connectedWords() throws KeyNotFoundException,
	    UnsupportedEncodingException, DynamicCursorException,
	    DataNotFoundException, DatabaseException {
	log.trace("called function updateConnectedWords");

	// Handle add Map
	log.debug("handling add map; size " + addMap.size());
	for (java.util.Map.Entry<SearchableObject, String> entryAdd : addMap
		.entrySet()) {
	    log.debug("updating linked of searchable object with language "
		    + mainObjectLanguage + ", id "
		    + arabword.getid().toString());
	    entryAdd.getKey().addlinkid(mainObjectLanguage,
		    arabword.getid().toString());
	    try {
		dit.write(SleepyFactory.getInstance().getDatabase(
			entryAdd.getValue()), entryAdd.getKey().getid(),
			entryAdd.getKey());
	    } catch (SecurityException e) {
		e.printStackTrace();
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (IllegalArgumentException e) {
		e.printStackTrace();
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (FileNotFoundException e) {
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
	    log.info("successfully connected searchable object "
		    + entryAdd.getValue().toString());
	}

	// Handle delete Map
	log.debug("handling delete map; size " + deleteMap.size());
	for (java.util.Map.Entry<SearchableObject, String> entryDel : deleteMap
		.entrySet()) {

	    log.debug("updating linkied of searchable object key:"
		    + entryDel.getKey() + ", value:" + entryDel.getValue());
	    entryDel.getKey().removelinkid(mainObjectLanguage,
		    arabword.getid().toString());
	    try {
		dit.write(SleepyFactory.getInstance().getDatabase(
			entryDel.getValue()), entryDel.getKey().getid(),
			entryDel.getKey());
	    } catch (SecurityException e) {
		e.printStackTrace();
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (IllegalArgumentException e) {
		e.printStackTrace();
		log.error("Exception " + e.getMessage());
		JOptionPane.showMessageDialog(null, e.getMessage());
	    } catch (FileNotFoundException e) {
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
	    log.info("successfully disconnected searchable object "
		    + entryDel.getValue().toString());

	}

    }

    private void initializeAudioLoader() throws ClassNotFoundException,
	    SecurityException, NoSuchMethodException, IllegalArgumentException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException {
	String audio_method = "set" + config.getAudioAttribute();
	String audio_directory = config.getAudioPath();

	log.debug(" method to use to set audio " + audio_method
		+ "; audio directory where load audio from " + audio_directory);

	Class<?> audioLoaderClass;
	Class<?>[] constructorParams;
	Constructor<?> IConstructor;

	String audioLoaderClassName = config.getAudioLoaderClass();
	log.debug("using " + audioLoaderClassName + " as audio Loader Class");

	audioLoaderClass = Class.forName(audioLoaderClassName);
	// get an instance
	constructorParams = new Class[] { net.verza.jdict.properties.LanguageConfigurationClassDescriptor.class };
	IConstructor = audioLoaderClass.getConstructor(constructorParams);
	audioLoader = (IAudioFileLoader) IConstructor.newInstance(config);

    }

    private byte[] getAudio(String audio) throws IllegalArgumentException,
	    IllegalAccessException, InvocationTargetException,
	    MalformedURLException, FileNotFoundException, IOException {
	log.trace("called function getAudio with audio " + audio);
	if ((audio != null) || ("".equals(audio)))
	    throw new IOException("cannot load null/empty audio stream");
	return (byte[]) audioLoader.get(audio);
    }

    public void actionPerformed(ActionEvent evt) {

	try {
	    if (evt.getActionCommand().equals(SECTION_COMBO_COMMAND)) {
		log.debug("adding to section  "
			+ sectionCombo.getSelectedItem().toString());
		String sectionId = null;

		sectionId = dit.readSectionDatabase(sectionCombo
			.getSelectedItem().toString());

		log.debug("adding section id to the searchable object "
			+ sectionId);
		arabword.addsection(sectionId);
		sectionListModel.addElement(sectionCombo.getSelectedItem());
		sectionList.setSelectedIndex(sectionList.getSelectedIndex());
		sectionList
			.ensureIndexIsVisible(sectionList.getSelectedIndex());

	    } else if (evt.getActionCommand().equals(REMOVE_SECTION_BUTTON)) {
		Object[] objArray = (Object[]) sectionList.getSelectedValues();
		log.debug("got " + objArray.length
			+ " from section list to delete");
		for (int i = 0; i < objArray.length; i++) {
		    String sectionToRemove = (String) objArray[i];
		    log.debug("section to remove is " + sectionToRemove);
		    arabword.removesection(sectionToRemove);
		    sectionListModel.removeElement(objArray[i]);
		}

	    } else if (evt.getActionCommand().equals(CATEGORY_COMBO_COMMAND)) {
		log.debug("adding to category  "
			+ categoryCombo.getSelectedItem().toString());
		String categoryId = null;
		try {
		    categoryId = dit.readCategoryDatabase(categoryCombo
			    .getSelectedItem().toString());
		} catch (UnsupportedEncodingException e) {
		    log.error("UnsupportedEncodingException " + e.getMessage());
		    e.printStackTrace();
		} catch (DatabaseException e) {
		    log.error("DatabaseException " + e.getMessage());
		    e.printStackTrace();
		}
		log.debug("adding section id to the searchable object "
			+ categoryId);
		arabword.addcategory(categoryId);
		categoryListModel.addElement(categoryCombo.getSelectedItem());
		categoryList.setSelectedIndex(categoryList.getSelectedIndex());
		categoryList.ensureIndexIsVisible(categoryList
			.getSelectedIndex());

	    } else if (evt.getActionCommand().equals(REMOVE_CATEGORY_BUTTON)) {
		Object[] objArray = (Object[]) categoryList.getSelectedValues();
		log.debug("got " + objArray.length
			+ " from category list to delete");
		for (int i = 0; i < objArray.length; i++) {
		    String categoryToRemove = (String) objArray[i];
		    log.debug("category to remove is " + categoryToRemove);
		    arabword.removesection(categoryToRemove);
		    categoryListModel.removeElement(objArray[i]);
		}

	    } else if (evt.getActionCommand().equals(CONNECT_WORDS_COMMAND)) {
		String language = (languageSelectorCombo.getSelectedItem()
			.toString() + config.getType());
		log.debug("setting linkId with word "
			+ tmpConnectedWord.toString() + " of language "
			+ languageSelectorCombo.getSelectedItem()
			+ config.getType());

		// first set the id of the connected object in the word
		log
			.debug("updating linkid of main object with searchable object having language "
				+ languageSelectorCombo.getSelectedItem()
				+ config.getType()
				+ " and id "
				+ tmpConnectedWord.getid());
		arabword
			.addlinkid(languageSelectorCombo.getSelectedItem()
				+ config.getType(), tmpConnectedWord.getid()
				.toString());

		// then add in the linkid JList the new connected word
		log.debug("add element to the list "
			+ tmpConnectedWord.toString());
		linkIdListModel.addElement(languageSelectorCombo
			.getSelectedItem()
			+ config.getType()
			+ LINKID_SEPARATOR
			+ tmpConnectedWord.getsingular());

		// add the connect Words to the Map; the key is the
		// language+type and the value
		// is the connected word itself
		log.debug("add element to the map "
			+ tmpConnectedWord.toString());
		log.debug(" map size" + addMap.size());
		addMap.put(tmpConnectedWord, language);
		linkIdList.setSelectedIndex(linkIdList.getSelectedIndex());
		linkIdList.ensureIndexIsVisible(linkIdList.getSelectedIndex());

	    } else if (evt.getActionCommand().equals(REMOVE_WORDS_COMMAND)) {
		// get the searchable objects from the list; cannot cast
		// to string here as it throws exception, so i'll do it later
		Object[] objArray = (Object[]) linkIdList.getSelectedValues();
		log.debug("got " + objArray.length
			+ " from connected words list to delete");
		for (int i = 0; i < objArray.length; i++) {
		    String tmp2 = (String) objArray[i];
		    String[] tmp = tmp2.split(LINKID_SEPARATOR);
		    String language = tmp[0];
		    String key = tmp[1];
		    SearchableObject obj = (SearchableObject) dit.read(
			    language, key);
		    if (obj == null) {
			log
				.error("connected word not found, can't disconnect word ");
			continue;
		    }
		    log
			    .debug("removing linkid of main object with searchable object having language "
				    + language + " and id " + obj.getid());
		    arabword.removelinkid(language, obj.getid().toString());
		    log.debug("removing from list");
		    linkIdListModel.removeElement(objArray[i]);
		}

	    } else if (evt.getActionCommand().equals(SEARCH_BUTTON_COMMAND)) {

		String language = (languageSelectorCombo.getSelectedItem()
			.toString() + config.getType());
		tmpConnectedWord = (Word) dit.read(language, searchText
			.getText());
		if (tmpConnectedWord == null) {
		    JOptionPane.showMessageDialog(null,
			    "data not found in the dictionary");
		    connectWordsButton.setEnabled(false);
		    return;
		}
		connectWordsButton.setEnabled(true);
		searchResult.setText(tmpConnectedWord.getsingular());
	    }
	} catch (UnsupportedEncodingException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("UnsupportedEncodingException " + e.getMessage());
	    e.printStackTrace();
	} catch (DatabaseException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("DatabaseException " + e.getMessage());
	    e.printStackTrace();
	} catch (DynamicCursorException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("DynamicCursorException " + e.getMessage());
	    e.printStackTrace();
	} catch (DataNotFoundException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("DataNotFoundException " + e.getMessage());
	    e.printStackTrace();
	} catch (KeyNotFoundException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("KeyNotFoundException " + e.getMessage());
	    e.printStackTrace();
	} catch (LanguagesConfigurationException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    log.error("LanguagesConfigurationException " + e.getMessage());
	    e.printStackTrace();
	} catch (SecurityException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    log.error("Exception " + e.getMessage());
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (LinkIDException e) {
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

}
