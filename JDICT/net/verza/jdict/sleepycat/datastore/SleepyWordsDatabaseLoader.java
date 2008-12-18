

package net.verza.jdict.sleepycat.datastore;

/**
 * @author ChristianVerdelli
 *
 */

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.log4j.Logger;
import jxl.read.biff.BiffException;
import net.verza.jdict.AudioFileLoader;
import net.verza.jdict.PropertiesLoader;
import net.verza.jdict.IWord;
import net.verza.jdict.dataloaders.ExcelLoader;
import net.verza.jdict.exceptions.LabelNotFoundException;
import com.sleepycat.je.DatabaseException;


public final class SleepyWordsDatabaseLoader {

	
	private String pathFile; 
	private static Logger log;
	private AudioFileLoader audioLoader;
	private Class<?> keyClass,valueClass;
	private Class<?>[] keyClassConstructorTypes,valueClassConstructorTypes;
	private Constructor<?> keyConstructor,valueConstructor;
	private SleepyWordsDatabaseWriter writer;

	
	public SleepyWordsDatabaseLoader(SleepyWordsDatabase db) {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class "+this.getClass().getName());
		writer = new SleepyWordsDatabaseWriter(db);
		audioLoader = new AudioFileLoader();
	}

	
	
	public void setFileName(String newPath) {
		
		pathFile = newPath;
	}


	
	public void loadDatabases() throws DatabaseException, 
			LabelNotFoundException, BiffException, IOException {

		loadWords();	
	}

	
	
	private int loadWords() throws LabelNotFoundException,
			IOException, BiffException {

		ExcelLoader dataloader;
		dataloader = new ExcelLoader(this.pathFile);
		dataloader.setSheetName(PropertiesLoader.getProperty("words.sheet_name"));
		Integer rowsNumber = new Integer(PropertiesLoader.getProperty("words.maxnumber"));
			
		/*
		 * find out the Method to call to set the property inside the Class
		 */	

		try {
			log.debug("instantiating key class: "+PropertiesLoader.getProperty("words.key_class")); 
			keyClass = Class.forName(PropertiesLoader.getProperty("words.key_class"));
			keyClassConstructorTypes= new Class[] {};
			keyConstructor = keyClass.getConstructor(keyClassConstructorTypes);	
			IWord array_keys[] = (IWord [])Array.newInstance(keyClass, rowsNumber);

			
			log.debug("instantiating value class: "+PropertiesLoader.getProperty("words.value_class"));			
			valueClass = Class.forName(PropertiesLoader.getProperty("words.value_class"));
			valueClassConstructorTypes = new Class[] {};
			valueConstructor = valueClass.getConstructor(valueClassConstructorTypes);
			IWord array_values[] = (IWord [])Array.newInstance(valueClass, rowsNumber);
			
			log.debug("calling constructor");
			
			for(int i=0;i<rowsNumber;i++){
				array_keys[i] = (IWord)keyConstructor.newInstance();
				array_values[i] = (IWord)valueConstructor.newInstance();
			}
						
			int counter = 0;
			List<SubnodeConfiguration> prop = PropertiesLoader.getHierarchicalProperty("words.fields.label");
			for (Iterator<SubnodeConfiguration> it = prop.iterator(); it.hasNext();) {
				HierarchicalConfiguration sub = (HierarchicalConfiguration) it
						.next();
				
				String attribute_method = "set"+sub.getString("class_attribute");
				String label = sub.getString("label_name");
				String type = sub.getString("class_type");
				String keyname = sub.getString("key_name");
				String audio_method = "set"+sub.getString("audio_attribute");
				String audio_directory = sub.getString("audio_directory");
				
				
				log.trace(	" method: " +attribute_method+
							" label: "+label+
							" type: "+type+
							" audio method "+audio_method+
							" audio directory "+audio_directory);


				Class<?>[] methodTypes = new Class[] { java.lang.String.class };
				Method keyMethod2Call,valueMethod2Call,audioMethod2Call;
				
				keyMethod2Call = keyClass.getMethod(attribute_method, methodTypes);
				valueMethod2Call = valueClass.getMethod(attribute_method, methodTypes);
				
				audioMethod2Call = null;
				if("primary".equals(keyname))	{
					audioMethod2Call = keyClass.getMethod(audio_method, byte[].class);
					audioLoader.setAudioFilesPath(audio_directory);
					audioLoader.loadAudioFiles();
				}
				
				// set the label to read 
				dataloader.setColumnName(label);
				// read all the words in the 
				Vector<String> v = dataloader.read();
				/* iterate through Vector and for each value calls the method*/
				Iterator<String> nums = v.iterator();
				
				
				log.debug("fields will be loaded as "+type);
				String value = new String();
				while(nums.hasNext()) {	
					value = nums.next();
					
					// if label has type value or key the value will
					// be set just in the key or value objects otherwise 
					// will be set in both	
					if(!"value".equals(type))	{
						log.debug("calling key method");
						keyMethod2Call.invoke(array_keys[counter], (Object)value);
						
						// if keyname is primary load audio stream
						if("primary".equals(keyname))	{
							log.info("setting audio stream");
							String audiofile = value.concat(".mp3");
							audioMethod2Call.invoke(array_keys[counter], (Object)audioLoader.get(audiofile));
						}
					}
					
					if(!"key".equals(type))	{
						log.debug("calling value method");
						valueMethod2Call.invoke(array_values[counter], (Object)value);
						
						// if keyname is primary load audio stream
						if("primary".equals(keyname))	{
							log.info("setting audio stream");
							String audiofile = value.concat(".mp3");
							audioMethod2Call.invoke(array_values[counter], (Object)audioLoader.get(audiofile));
						}
						
					}
					
					
					
					counter++;
				}
				
				//reset variable for the next loop
				keyMethod2Call = null;
				valueMethod2Call = null;
				counter = 0;
	
			}		
			
			writer.write(array_keys, array_values);
		
			
		} catch (ClassNotFoundException e) {
			log.error("ClassNotFoundException "+e.getMessage());
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			log.error("NoSuchMethodException "+e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			log.error("InstantiationException "+e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("IllegalAccessException "+e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("InvocationTargetException "+e.getMessage());
			e.printStackTrace();
		} catch (DatabaseException e) {
			log.error("DatabaseException "+e.getMessage());
			e.printStackTrace();
		}
		

		
		return 0;
	}
	

}
