package net.verza.jdict.sleepycat.datastore;

/**
 * @author ChristianVerdelli
 *
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import jxl.read.biff.BiffException;
import net.verza.jdict.AudioFileLoader;
import net.verza.jdict.SearchableObject;
import net.verza.jdict.dataloaders.ExcelLoader;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguageFieldConfigurationClassDescritor;
import net.verza.jdict.properties.LanguagesConfiguration;
import net.verza.jdict.properties.PropertiesLoader;
import com.sleepycat.je.DatabaseException;

public final class SleepyDatabaseLoader {

	private String pathFile;
	private static Logger log;
	private AudioFileLoader audioLoader;
	private Class<?> IClass;
	private Class<?>[] ClassConstructorTypes;
	private Constructor<?> IConstructor;
	private SleepyDatabaseWriter writer;
	private SleepyFactory factory;
	private ExcelLoader dataloader;
	private Method Method2Call, audioMethod2Call;
	private String key_array[];
	private SearchableObject class_array[];
	private Integer rowsNumber;
	private Class<?>[] methodTypes;
	private List<String> translations;
	private String excel_sheet;

	
	public SleepyDatabaseLoader() {
		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName());
		this.audioLoader = new AudioFileLoader();
		this.factory = SleepyFactory.getInstance();
		this.translations = new Vector<String>();
		this.rowsNumber = 0;
	}

	public void setFileName(String newPath) {
		pathFile = newPath;
		dataloader = new ExcelLoader(this.pathFile);
	}

	public void loadDatabases() throws DatabaseException,
			LabelNotFoundException, BiffException, IOException,
			KeyNotFoundException {

		this.iterateLanguages();

	}

	@SuppressWarnings("unchecked")
	private void iterateLanguages() throws DatabaseException,
			LabelNotFoundException, BiffException, IOException,
			KeyNotFoundException {

		try {

			HashMap<String, LanguageConfigurationClassDescriptor> ldesc = LanguagesConfiguration
					.getLanguageConfigurationBlock();
			LanguageConfigurationClassDescriptor sub = null;

			for (Iterator<String> it = ldesc.keySet().iterator(); it.hasNext();) {
				sub = (LanguageConfigurationClassDescriptor) ldesc.get(it
						.next());

				String nickname = sub.getLanguageNickname();
				String type = sub.getType();
				
				log.debug("languge " + nickname 
							+ " type " + type);

				// check if the language is enabled , if not skip to the next
				// language
				if (!sub.getIsEnabled()) {
					log.info("language " + nickname
							+ " not enabled, skipping to next language");
					continue;
				}

				// translations = (List<String>) sub.getList("translation");
				// log.info("available translations " + translations);

				writer = new SleepyDatabaseWriter(factory.getDatabase(nickname
						+ type));
				writer.setDataBinding(SleepyBinding.getDataBinding());

				String classname = sub.getClassQualifiedName();
				log.debug("instantiating class: " + classname);
				IClass = Class.forName(classname);
				ClassConstructorTypes = new Class[] {};
				IConstructor = IClass.getConstructor(ClassConstructorTypes);

				rowsNumber = new Integer(PropertiesLoader
						.getProperty("maxnumber"));
				key_array = new String[rowsNumber];
				class_array = (SearchableObject[]) Array.newInstance(IClass,
						rowsNumber);

				for (int i = 0; i < rowsNumber; i++) {
					class_array[i] = (SearchableObject) IConstructor
							.newInstance();
				}

				excel_sheet = sub.getExcelSheet();
				log.trace("setting sheet name to " + excel_sheet);
				dataloader.setSheetName(excel_sheet);
				
				
				this.iterateLabels(nickname + type, type);

				writer.write(key_array, class_array);

			}

		} catch (ClassNotFoundException e) {
			log.error("ClassNotFoundException " + e.getMessage());
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			log.error("NoSuchMethodException " + e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			log.error("InstantiationException " + e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException " + e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException " + e.getMessage());
			System.out.println("target exception " + e.getTargetException());
			e.printStackTrace();
		}

	}

	private void iterateLabels(String _language, String _type)
			throws LabelNotFoundException, BiffException, IOException,
			KeyNotFoundException {

		try {

			LanguageConfigurationClassDescriptor sub = LanguagesConfiguration
					.getLanguageConfigurationBlock().get(_language);

			List<LanguageFieldConfigurationClassDescritor> lista = sub
					.getFields();
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

				log.trace(" method: " + attribute_method + " label: " + label
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
						System.out.println("link id label to read " + mco);
					}
					methodTypes = new Class[] { java.lang.String.class,
							java.lang.String.class };
					Method2Call = IClass.getMethod(attribute_method,
							methodTypes);
				} else {
					methodTypes = new Class[] { java.lang.String.class };
					Method2Call = IClass.getMethod(attribute_method,
							methodTypes);

				}

				if (hasAudio) {
					String audio_method = "set" + sub.getAudioAttribute();
					String audio_directory = sub.getAudioPath();

					log.debug(" audio method " + audio_method
							+ " audio directory " + audio_directory);

					audioMethod2Call = IClass.getMethod(audio_method,
							byte[].class);
					audioLoader.setAudioFilesPath(audio_directory);
					audioLoader.loadAudioFiles();
					methodTypes = new Class[] { java.lang.String.class };
					Method2Call = IClass.getMethod(attribute_method,
							methodTypes);

				}

				readLabel(_type, label, isLinkid, hasAudio, isPrimaryKey);

			}

		} catch (NoSuchMethodException e) {
			log.error("NoSuchMethodException " + e.getMessage());
			e.printStackTrace();
		}

	}

	private Vector<String> readLabel(String _type, String label,
			Boolean isLinkID, Boolean loadaudio, Boolean primaryKey)
			throws LabelNotFoundException, BiffException, IOException,
			KeyNotFoundException {

		Vector<String> data = new Vector<String>();
		try {

			// set the label to read
			log.trace("setting label  to read  " + label);
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

				if (primaryKey) {
					log
							.debug("setting key inside keys array with key "
									+ value);
					key_array[counter] = value;

					// resizing keys and data arrays to the size of primary keys
					// read
					log.info("resizing keys and data arrays to the size of primary keys read");
					int resizeLenght = data.size();
					System.out.println("resizeLenght "+resizeLenght
										+"this.key_array "+this.key_array.length);
					String newKeysArray[] = new String[resizeLenght];
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

				}

				// load the audio stream
				if (loadaudio) {
					String audiofile = value.concat(".mp3");
					"/Users/ChristianVerdelli/documents/workspace/jdict/audio/italian/".concat(audiofile);
					if (audioLoader.get(audiofile) != null) {
						log.debug("setting audio stream with method "
								+ audioMethod2Call.getName() + " with value  "
								+ audiofile);
						audioMethod2Call.invoke(class_array[counter],
								(Object) audioLoader.get(audiofile));
					}
				}

				// if the attribute is multi value
				if (isLinkID) {
					log
							.debug("calling method " + Method2Call.getName()
									+ " with values " + label + _type + " and "
									+ value);
					Method2Call.invoke(class_array[counter], (Object) label
							+ _type, (Object) value);
					// if the attribute is single value
				} else {
					log.debug("calling method " + Method2Call.getName()
							+ " with value " + value + _type);
					Method2Call.invoke(class_array[counter], (Object) value);
				}

				counter++;
			}

		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException " + e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException " + e.getMessage());
			System.out.println("target exception " + e.getTargetException());
			e.printStackTrace();
		}

		return data;

	}

}
