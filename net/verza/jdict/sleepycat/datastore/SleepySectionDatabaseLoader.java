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
import net.verza.jdict.dataloaders.ExcelLoader;
import net.verza.jdict.dataloaders.LoaderOptionsStore;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.properties.PropertiesLoader;

public final class SleepySectionDatabaseLoader {

	private LoaderOptionsStore optionObj;
	private SleepySectionDatabase sleepySectionDatabase;
	private SleepySectionDatabaseWriter sleepySectionDatabaseWriter;
	private List<Vector<String>> sectionTable;
	private static Logger log;

	public SleepySectionDatabaseLoader(SleepySectionDatabase db) {
		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName());
		sleepySectionDatabase = db;
		sleepySectionDatabaseWriter = new SleepySectionDatabaseWriter(
				sleepySectionDatabase);
		sectionTable = new Vector<Vector<String>>();
	}

	public int loadDatabases() throws DatabaseException,
			LabelNotFoundException, BiffException, IOException {
		int successfullImportedCounter = 0;
		
		if (this.optionObj.getTypeOfImport().equals("rebuild")) {
			this.sleepySectionDatabase.close();
			this.sleepySectionDatabase.flushDatabase();
			this.sleepySectionDatabase.open();
		}

		loadFile();

		log.trace("sectionTable.size " + sectionTable.size());
		Vector<String> keyColumnVector = sectionTable.get(0);
		Vector<String> dataColumnVector = sectionTable.get(1);
		for (int i = 0; i < keyColumnVector.size(); i++) {
			// To be able later to search for either key&value objects
			// are stored twice in the reverse order
			if(-1 != sleepySectionDatabaseWriter.writeData((String) keyColumnVector
					.get(i), (String) dataColumnVector.get(i)) )
				successfullImportedCounter++;
		}

		return successfullImportedCounter;
	}

	public void setOptionObject(LoaderOptionsStore _obj) {
		this.optionObj = _obj;
	}

	private int loadFile() throws LabelNotFoundException, IOException,
			BiffException {

		ExcelLoader dataloader;
		dataloader = new ExcelLoader(this.optionObj.getInputFile());
		dataloader.setSheetName(PropertiesLoader
				.getProperty("section.sheet_name"));

		/* load id field */
		dataloader.setColumnName(PropertiesLoader
				.getProperty("section.name_field"));
		sectionTable.add(dataloader.read());

		/* load name field */
		dataloader.setColumnName(PropertiesLoader
				.getProperty("section.index_field"));
		sectionTable.add(dataloader.read());

		return 0;
	}

}
