package net.verza.jdict.dataloaders;

import java.io.File;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 * This class stores options needed by database loaders
 * 
 * 
 */

public class LoaderOptionsStore {

    private static Logger log;
    private File inputFile;
    private String typeOfImport;
    private HashMap<String, Boolean> labels;
    private Boolean section;
    private Boolean category;

    public LoaderOptionsStore() {
	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());
	this.labels = new HashMap<String, Boolean>();
    }

    public File getInputFile() {
	log.trace("called getter, returning " + inputFile.toString());
	return inputFile;
    }

    public void setInputFile(File inputFile) {
	log.trace("called setter with argument " + inputFile);
	this.inputFile = inputFile;
    }

    public String getTypeOfImport() {
	log.trace("called getter, returning " + typeOfImport);
	return typeOfImport;
    }

    public void setTypeOfImport(String typeOfImport) {
	log.trace("called setter with argument " + typeOfImport);
	this.typeOfImport = typeOfImport;
    }

    public HashMap<String, Boolean> getLabels() {
	log.trace("called getter, returnig hashmap of size " + labels.size());
	return labels;
    }

    public void setLabels(String key, Boolean value) {
	log.trace("called setter with argument key " + key + " and value "
		+ value);
	this.labels.put(key, value);
    }

    public Boolean getSection() {
	log.trace("called getter, returnig " + section);
	return section;
    }

    public void setSection(Boolean section) {
	log.trace("called setter with argument " + section);
	this.section = section;
    }

    public Boolean getCategory() {
	log.trace("called getter, returnig " + category);
	return category;
    }

    public void setCategory(Boolean category) {
	log.trace("called setter with argument " + category);
	this.category = category;
    }

}