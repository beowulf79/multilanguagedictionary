package net.verza.jdict.dictionary.sleepycat;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import jxl.read.biff.BiffException;
import net.verza.jdict.dataloaders.ExcelLoader;
import net.verza.jdict.dataloaders.IAudioFileLoader;
import net.verza.jdict.dataloaders.LoaderOptionsStore;
import net.verza.jdict.exceptions.DatabaseImportException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.model.SearchableObject;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguageFieldConfigurationClassDescritor;
import net.verza.jdict.properties.LanguagesConfiguration;
import net.verza.jdict.properties.PropertiesLoader;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

public final class SleepyDatabaseLoader {

    private LoaderOptionsStore optionObj;
    private static Logger log;
    private IAudioFileLoader audioLoader;
    private Class<?> IClass;
    private Class<?>[] ClassConstructorTypes;
    private Constructor<?> IConstructor;
    private SleepyDatabaseWriter writer;
    private SleepyFactory factory;
    private ExcelLoader dataloader;
    private Method Method2Call, audioMethod2Call;
    private Integer key_array[];
    private SearchableObject class_array[];
    private Integer rowsNumber;
    private Class<?>[] methodTypes;
    private List<String> translations;
    private String excel_sheet;
    private HashMap<String, Integer> importInfo;

    public SleepyDatabaseLoader() throws IllegalArgumentException,
	    ClassNotFoundException, NoSuchMethodException,
	    InstantiationException, IllegalAccessException,
	    InvocationTargetException, SecurityException,
	    FileNotFoundException, DatabaseException {
	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());
	this.factory = SleepyFactory.getInstance();
	this.translations = new Vector<String>();
	this.rowsNumber = 0;
	this.importInfo = new HashMap<String, Integer>();
    }

    public void setOptionObject(LoaderOptionsStore _obj) {
	this.optionObj = _obj;
    }

    public HashMap<String, Integer> loadDatabases() throws DatabaseException,
	    LabelNotFoundException, BiffException, IOException,
	    KeyNotFoundException, DatabaseImportException,
	    LanguagesConfigurationException, SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException {

	dataloader = new ExcelLoader(this.optionObj.getInputFile());
	this.iterateLanguages();

	return this.importInfo;

    }

    @SuppressWarnings("unchecked")
    private void iterateLanguages() throws DatabaseException,
	    LabelNotFoundException, BiffException, IOException,
	    KeyNotFoundException, DatabaseImportException,
	    LanguagesConfigurationException, SecurityException,
	    IllegalArgumentException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException {

	HashMap lang = this.optionObj.getLabels();
	Iterator it = lang.keySet().iterator();
	while (it.hasNext()) {
	    String key = (String) it.next();

	    if ((Boolean) lang.get(key)) {

		LanguageConfigurationClassDescriptor ldesc = LanguagesConfiguration
			.getLanguageMainConfigNode(key);

		String nickname = ldesc.getLanguageNickname();
		String type = ldesc.getType();

		log.info("importing languge " + nickname + " type " + type);

		// check if the language is enabled , if not skip to the
		// next
		// language
		if (!ldesc.isEnabled()) {
		    log.info("language " + nickname
			    + " not enabled, skipping to next language");
		    continue;
		}

		writer = new SleepyDatabaseWriter(factory.getDatabase(nickname
			+ type));
		// writer.setDataBinding(SleepyBinding.getDataBinding());
		factory.getDatabase(nickname + type).close();

		// close the database to avoid lock problems
		if (this.optionObj.getTypeOfImport().equals("rebuild")) {
		    // flushing the database if rebuild option has been
		    // selected
		    log
			    .debug("import type is rebuild; close and flush databases");
		    factory.getDatabase(nickname + type).flushDatabase();
		}
		// to write is available to a closed database
		factory.getDatabase(nickname + type).open();

		String classname = ldesc.getClassQualifiedName();
		log.debug("instantiating class: " + classname);
		IClass = Class.forName(classname);
		ClassConstructorTypes = new Class[] {};
		IConstructor = IClass.getConstructor(ClassConstructorTypes);

		rowsNumber = new Integer(PropertiesLoader
			.getProperty("maxnumber"));
		key_array = new Integer[rowsNumber];
		class_array = (SearchableObject[]) Array.newInstance(IClass,
			rowsNumber);

		for (int i = 0; i < rowsNumber; i++) {
		    class_array[i] = (SearchableObject) IConstructor
			    .newInstance();
		}

		excel_sheet = ldesc.getExcelSheet();
		log.debug("setting sheet name to " + excel_sheet);
		dataloader.setSheetName(excel_sheet);

		this.iterateLabels(nickname + type, type);

		int count = writer.write(key_array, class_array);
		this.importInfo.put(key, count);

	    }

	}

    }

    private void iterateLabels(String _language, String _type)
	    throws LabelNotFoundException, BiffException, IOException,
	    KeyNotFoundException, ClassNotFoundException,
	    IllegalAccessException, InstantiationException,
	    IllegalArgumentException, InvocationTargetException,
	    SecurityException, NoSuchMethodException {

	LanguageConfigurationClassDescriptor sub = LanguagesConfiguration
		.getLanguageConfigurationBlock().get(_language);

	List<LanguageFieldConfigurationClassDescritor> lista = sub.getFields();
	for (Iterator<LanguageFieldConfigurationClassDescritor> it = lista
		.iterator(); it.hasNext();) {
	    LanguageFieldConfigurationClassDescritor tmp = (LanguageFieldConfigurationClassDescritor) it
		    .next();

	    String attribute_method = "set" + tmp.getAttributeName();
	    String label = tmp.getInputLabel();

	    Boolean isPrimaryKey = false;
	    if ("primary".equals(tmp.getKey_type()))
		isPrimaryKey = true;

	    Boolean hasAudio = false;
	    if (tmp.getAttributeHasAudio())
		hasAudio = true;

	    Boolean isLinkid = false;
	    if ("linkid".equals(tmp.getAttributeName()))
		isLinkid = true;

	    log.debug(" method: " + attribute_method + " label: " + label
		    + " is Primary Key: " + isPrimaryKey
		    + " atttribute has audio: " + hasAudio + " is linkID: "
		    + isLinkid);

	    // Linkid is a Map attribute where the keys are the available
	    // translations
	    // and the value is the value of the column corresponding to the
	    // translation language
	    if (isLinkid) {
		Iterator<String> i = translations.iterator();
		while (i.hasNext()) {
		    String mco = (String) i.next();
		    log.debug("link id label to read " + mco);
		}
		methodTypes = new Class[] { java.lang.String.class,
			java.lang.String.class };
		Method2Call = IClass.getMethod(attribute_method, methodTypes);
	    } else if (isPrimaryKey) {
		methodTypes = new Class[] { java.lang.Integer.class };
		Method2Call = IClass.getMethod(attribute_method, methodTypes);

	    } else {
		methodTypes = new Class[] { java.lang.String.class };
		Method2Call = IClass.getMethod(attribute_method, methodTypes);

	    }

	    if (hasAudio) {
		String audio_method = "set" + sub.getAudioAttribute();
		String audio_directory = sub.getAudioPath();

		log.debug(" method to use to set audio " + audio_method
			+ "; audio directory where load audio from "
			+ audio_directory);

		audioMethod2Call = IClass.getMethod(audio_method, byte[].class);

		String audioLoaderClass = sub.getAudioLoaderClass();
		log.debug("using " + audioLoaderClass
			+ " as audio Loader Class");
		Class<?> AClass = Class.forName(audioLoaderClass);
		ClassConstructorTypes = new Class[] { net.verza.jdict.properties.LanguageConfigurationClassDescriptor.class };
		try {
		    IConstructor = AClass.getConstructor(ClassConstructorTypes);
		    audioLoader = (IAudioFileLoader) IConstructor
			    .newInstance(sub);

		    methodTypes = new Class[] { java.lang.String.class };
		    Method2Call = IClass.getMethod(attribute_method,
			    methodTypes);
		} catch (InvocationTargetException e) {
		    log
			    .error("InvocationTargetException with audio loader class, disabling audio flag; exception was "
				    + e.getCause());
		    hasAudio = false;

		}

	    }
	    readLabel(_type, label, isLinkid, hasAudio, isPrimaryKey);

	}

    }

    private Vector<String> readLabel(String _type, String label,
	    Boolean isLinkID, Boolean loadaudio, Boolean primaryKey)
	    throws LabelNotFoundException, BiffException, IOException,
	    KeyNotFoundException, IllegalArgumentException,
	    IllegalAccessException, InvocationTargetException {

	Vector<String> data = new Vector<String>();

	// set the label to read
	log.debug("label  to read is " + label);
	dataloader.setColumnName(label);
	// read all the words in the
	data = dataloader.read();
	/* iterate through Vector and for each value calls the method */
	Iterator<String> field = data.iterator();

	String value = new String();
	int counter = 0;
	while (field.hasNext()) {
	    value = field.next();
	    if (value == null) {
		counter++;
		continue;
	    }

	    // load the audio stream
	    if (loadaudio) {
		String audiofile = (value);
		if (audioLoader.get(audiofile) != null) {
		    log.debug("setting audio stream with method "
			    + audioMethod2Call.getName() + " with value  "
			    + audiofile);
		    audioMethod2Call.invoke(class_array[counter], audioLoader
			    .get(audiofile));
		}
	    }

	    // if the attribute is multi value
	    if (isLinkID) {
		log.debug("calling method " + Method2Call.getName()
			+ " with values " + label + _type + " and " + value);
		Method2Call.invoke(class_array[counter],
			(Object) label + _type, (Object) value);
		// if the attribute is single value
	    } else if (primaryKey) {

		log
			.debug("primary key; setting key inside keys array with key "
				+ value);
		key_array[counter] = new Integer(value);

		// resizing keys and data arrays to the size of primary keys
		// read
		int resizeLenght = data.size();
		log
			.info("going to resize keys and data arrays to the size of primary keys read: "
				+ resizeLenght);
		Integer newKeysArray[] = new Integer[resizeLenght];
		System.arraycopy(this.key_array, 0, newKeysArray, 0,
			resizeLenght);
		this.key_array = newKeysArray;

		SearchableObject newDataArray[] = (SearchableObject[]) Array
			.newInstance(IClass, resizeLenght);
		System.arraycopy(this.class_array, 0, newDataArray, 0,
			resizeLenght);
		this.class_array = newDataArray;
		log.debug("keys and data class array resized to "
			+ resizeLenght);
		log.debug("calling method " + Method2Call.getName()
			+ " with value " + value + " and type " + _type);

		Method2Call.invoke(class_array[counter], (Object) new Integer(
			value));

	    } else {
		log.debug("calling method " + Method2Call.getName()
			+ " with value " + value + " and type " + _type);
		Method2Call.invoke(class_array[counter], (Object) value);
	    }

	    counter++;
	}

	return data;

    }
}
