package net.verza.jdict.sleepycat.datastore;

/**
 * @author ChristianVerdelli
 *
 */

import java.util.List;
import java.util.Vector;
import java.io.IOException;

import org.apache.log4j.Logger;

import jxl.read.biff.BiffException;
import net.verza.jdict.dataloaders.ExcelLoader;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.properties.PropertiesLoader;

import com.sleepycat.je.DatabaseException;

public final class SleepyCategoryDatabaseLoader {

	private SleepyCategoryDatabase dbHandler;
	private SleepyCategoryDatabaseWriter categoryWriter;
	private String pathFile; 
	private List<Vector<String>> categoryTable;
	private static Logger log;
	
	public SleepyCategoryDatabaseLoader(SleepyCategoryDatabase db) {
		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class "+this.getClass().getName());
		dbHandler = db;
		categoryWriter = new SleepyCategoryDatabaseWriter(dbHandler
				.getCategoryDatabase());
		categoryTable = new Vector<Vector<String>>();
	}

	
	public void setFileName(String newPath) {
		log.info("setting source file "+newPath);
		pathFile = newPath;
	}


	public void loadDatabases() throws DatabaseException,
			LabelNotFoundException, BiffException, IOException {


		loadFile();

		// loading category Class here
		log.trace("categoryTable.size " + categoryTable.size());
		Vector<String> keyColumnVector = categoryTable.get(0);
		Vector<String> dataColumnVector = categoryTable.get(1);
		for (int i = 0; i < keyColumnVector.size(); i++) {
			// To be able later to search for either key&value objects
			// are stored twice in the reverse order
			categoryWriter.writeData((String) keyColumnVector.get(i),
					(String) dataColumnVector.get(i));
		}

	}

	
	private int loadFile() throws LabelNotFoundException,
			IOException, BiffException {

		ExcelLoader dataloader;
		dataloader = new ExcelLoader(this.pathFile);
		dataloader.setSheetName(PropertiesLoader.getProperty("category.sheet_name"));
		
		/* load id field */
		dataloader.setColumnName(PropertiesLoader.getProperty("category.name_field"));
		categoryTable.add(dataloader.read());

		/* load name field */
		dataloader.setColumnName(PropertiesLoader.getProperty("category.index_field"));
		categoryTable.add(dataloader.read());

		return 0;
	}

}
