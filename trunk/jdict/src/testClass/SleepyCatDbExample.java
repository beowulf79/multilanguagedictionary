package testClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import net.verza.jdict.dataloaders.LoaderOptionsStore;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DatabaseImportException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.exceptions.LinkIDException;
import net.verza.jdict.model.SearchableObject;
import net.verza.jdict.properties.Configuration;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguageFieldConfigurationClassDescritor;
import net.verza.jdict.properties.LanguagesConfiguration;
import net.verza.jdict.properties.PropertiesLoader;
import net.verza.jdict.utils.Utility;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentLockedException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class SleepyCatDbExample {

	Dictionary dit;
	private static Logger log;
	private static final String log4j_filename = Configuration.LOG4JCONF;

	public SleepyCatDbExample() {
	}

	private void open() throws EnvironmentLockedException, DatabaseException,
			FileNotFoundException, UnsupportedEncodingException,
			IllegalArgumentException, DataNotFoundException,
			DynamicCursorException, KeyNotFoundException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, SecurityException, LinkIDException,
			LanguagesConfigurationException {

		dit = Factory.getDictionary();

	}

	private void read(String lang, String lookupkey) throws DatabaseException,
			UnsupportedEncodingException, SecurityException,
			IllegalArgumentException, FileNotFoundException,
			DynamicCursorException, DataNotFoundException,
			KeyNotFoundException, LinkIDException,
			LanguagesConfigurationException, ClassNotFoundException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {

		Vector<SearchableObject> objs = new Vector<SearchableObject>();
		objs.add(dit.read(lang, lookupkey));

		for (int i = 0; i < objs.size(); i++) {
			SearchableObject sObj = objs.get(i);
			if (sObj == null)
				continue;

			System.out.println(sObj.toString());
		}

	}

	private void dump(String lang) throws BiffException,
			LabelNotFoundException, IOException, DatabaseImportException,
			Exception {

		Vector<SearchableObject> objs = new Vector<SearchableObject>();
		objs = (dit.read(lang));

		for (int i = 0; i < objs.size(); i++) {
			SearchableObject sObj = objs.get(i);
			if (sObj == null)
				continue;

			System.out.println(sObj.toString());
		}
	}

	private void export(String filename) throws BiffException,
			LabelNotFoundException, IOException, DatabaseImportException,
			Exception {

		LoaderOptionsStore optionsObj = new LoaderOptionsStore();
		optionsObj.setInputFile(new File(filename));
		dit.dumpDatabase(optionsObj);

	}

	private void write(Integer key, String value) throws DatabaseException {
		DatabaseEntry keyEntry = new DatabaseEntry(Utility.intToByteArray(key));
		DatabaseEntry valueEntry = new DatabaseEntry(value.getBytes());

		// database.put(null, keyEntry, valueEntry);

	}

	private void load() throws DatabaseException {
		/*
		 * Integer[] keys = { new Integer(4), new Integer(3), new Integer(1),
		 * new Integer(2) }; String[] values = { "quattro", "tre", "uno", "due"
		 * };
		 * 
		 * for (int i = 0; i < keys.length; i++) { System.out
		 * .println("loading key:" + keys[i] + " value:" + values[i]);
		 * DatabaseEntry keyEntry = new DatabaseEntry(Utility
		 * .intToByteArray(keys[i])); DatabaseEntry valueEntry = new
		 * DatabaseEntry(values[i].getBytes()); database.put(null, keyEntry,
		 * valueEntry); }
		 * 
		 * environment.sync();
		 */
	}

	public static void main(String[] args) throws IllegalArgumentException,
			SecurityException, DataNotFoundException, DynamicCursorException,
			KeyNotFoundException, ClassNotFoundException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException, LinkIDException,
			LanguagesConfigurationException {

		PropertyConfigurator.configure(log4j_filename);
		log = Logger.getLogger("jdict");
		log.trace("starting gui");

		new PropertiesLoader();
		new LanguagesConfiguration();

		SleepyCatDbExample example = new SleepyCatDbExample();
		try {
			example.open();

			/*
			 * System.out .println(
			 * "<------------------------------- LOAD ------------------------->"
			 * ); example.load();
			 */

			/*
			 * System.out .println(
			 * "<------------------------------- DUMP ------------------------->"
			 * ); example.dump("italianverb");
			 */

			System.out
					.println("<------------------------------- EXPORT ------------------------->");
			example.export("/tmp/output.xls");

			/*
			 * System.out
			 * .println("<-------------------- READ ----------------------->");
			 * example.read("italianverb", "camminare");
			 */

			/*
			 * System.out
			 * .println("<-------------------- WRITE  ----------------------->"
			 * ); example.write(5, "cinque");
			 */

		} catch (EnvironmentLockedException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (LabelNotFoundException e) {
			e.printStackTrace();
		} catch (DatabaseImportException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
