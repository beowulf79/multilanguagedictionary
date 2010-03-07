package net.verza.jdict.dictionary.sleepycat;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import jxl.read.biff.BiffException;
import net.verza.jdict.dataloaders.ExcelLoader;
import net.verza.jdict.dataloaders.LoaderOptionsStore;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.properties.PropertiesLoader;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

public final class SleepyCategoryDatabaseLoader {

    private LoaderOptionsStore optionObj;
    private SleepyCategoryDatabase sleepyCategoryDatabase;
    private SleepyCategoryDatabaseWriter sleepyCategoryDatabaseWriter;
    private List<Vector<String>> categoryTable;
    private static Logger log;

    public SleepyCategoryDatabaseLoader(SleepyCategoryDatabase db) {
	log = Logger.getLogger("dictionary");
	log.trace("called class " + this.getClass().getName());
	sleepyCategoryDatabase = db;
	sleepyCategoryDatabaseWriter = new SleepyCategoryDatabaseWriter(
		sleepyCategoryDatabase);
	categoryTable = new Vector<Vector<String>>();
    }

    public int loadDatabases() throws DatabaseException,
	    LabelNotFoundException, BiffException, IOException {
	int successfullImportedCounter = 0;

	if (this.optionObj.getTypeOfImport().equals("rebuild")) {
	    log
		    .debug("rebuild option selected, closing and flushing the database");
	    this.sleepyCategoryDatabase.close();
	    this.sleepyCategoryDatabase.flushDatabase();
	    this.sleepyCategoryDatabase.open();
	}

	loadFile();

	// loading category Class here
	log.info("category database size " + categoryTable.size());
	Vector<String> keyColumnVector = categoryTable.get(0);
	Vector<String> dataColumnVector = categoryTable.get(1);
	for (int i = 0; i < keyColumnVector.size(); i++) {
	    // To be able later to search for either key&value
	    // objects
	    // are stored twice in the reverse order
	    if (-1 != sleepyCategoryDatabaseWriter.writeData(
		    (String) keyColumnVector.get(i), (String) dataColumnVector
			    .get(i)))
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
		.getProperty("category.sheet_name"));

	/* load id field */
	dataloader.setColumnName(PropertiesLoader
		.getProperty("category.name_field"));
	categoryTable.add(dataloader.read());

	/* load name field */
	dataloader.setColumnName(PropertiesLoader
		.getProperty("category.index_field"));
	categoryTable.add(dataloader.read());

	return 0;
    }

}
