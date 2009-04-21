package net.verza.jdict.sleepycat.datastore;


import com.sleepycat.je.DatabaseException;
import java.io.IOException;
import net.verza.jdict.Configuration;
import org.apache.log4j.PropertyConfigurator;
import jxl.read.biff.BiffException;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.properties.LanguagesConfiguration;
import net.verza.jdict.properties.PropertiesLoader;
import net.verza.jdict.exceptions.KeyNotFoundException;

public class DatabaseLoaderTestClass {


	static String inputfile;
	
	private static SleepyDatabaseLoader loader;

	public static void main(String[] args) {
		

		
		if(args.length == 0 )	{
			System.err.println("no arguments received");
			System.exit(-1);
		}
			
		inputfile = args[0];
		System.err.println("import using file "+inputfile);
		
		PropertyConfigurator.configure(Configuration.LOG4JCONF);

		new PropertiesLoader();
		new LanguagesConfiguration();
		new SleepyBinding();
		

		load();

	}

	private static void load() {
		try {

			loader = new SleepyDatabaseLoader();
			loader
					.setFileName(inputfile);
			loader.loadDatabases();

		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LabelNotFoundException e) {
			e.printStackTrace();
		} catch (KeyNotFoundException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

	}

}
