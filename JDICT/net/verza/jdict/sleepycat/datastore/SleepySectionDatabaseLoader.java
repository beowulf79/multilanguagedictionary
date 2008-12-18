package net.verza.jdict.sleepycat.datastore;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;
import jxl.read.biff.BiffException;
import net.verza.jdict.PropertiesLoader;
import net.verza.jdict.dataloaders.ExcelLoader;
import net.verza.jdict.exceptions.LabelNotFoundException;

public final class SleepySectionDatabaseLoader {

	
	private SleepySectionDatabase dbHandler;
	private SleepySectionDatabaseWriter sectionWriter;
	private String pathFile;
	private List<Vector<String>> sectionTable;
	private static Logger log;

	
	public SleepySectionDatabaseLoader(SleepySectionDatabase db) {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class "+this.getClass().getName());
		dbHandler = db;
		sectionWriter = new SleepySectionDatabaseWriter(dbHandler
				.getSectionDatabase());
		sectionTable = new Vector<Vector<String>>();
	}


	public void setFileName(String newPath) throws IOException{
		log.info("setting source file "+newPath);
		pathFile = newPath;
	}


	public void loadDatabases() throws DatabaseException,
			LabelNotFoundException, BiffException, IOException {

		// loadFile opens a file that contains our data
		// and loads it into a list for us to work with. The sourceField
		// parameter represents the number of fields in the file.
		loadFile();
	
		log.trace("sectionTable.size " + sectionTable.size());
		Vector<String> keyColumnVector = sectionTable.get(0);
		Vector<String> dataColumnVector = sectionTable.get(1);
		for (int i = 0; i < keyColumnVector.size(); i++) {
			// To be able later to search for either key&value objects
			// are stored twice in the reverse order
			sectionWriter.writeData((String) keyColumnVector.get(i),
					(String) dataColumnVector.get(i));
		}

	}

	
	private int loadFile() throws LabelNotFoundException,
			IOException, BiffException {

		ExcelLoader dataloader;
		dataloader = new ExcelLoader(this.pathFile);
		dataloader.setSheetName(PropertiesLoader.getProperty("section.sheet_name"));
		
		/* load id field */
		dataloader.setColumnName(PropertiesLoader.getProperty("section.name_field"));
		sectionTable.add(dataloader.read());

		/* load name field */
		dataloader.setColumnName(PropertiesLoader.getProperty("section.index_field"));
		sectionTable.add(dataloader.read());


		return 0;
	}

	
}
