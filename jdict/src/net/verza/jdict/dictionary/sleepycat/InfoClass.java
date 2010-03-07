package net.verza.jdict.dictionary.sleepycat;

import org.apache.log4j.Logger;

public class InfoClass {

    private String index_name;
    private String index_class_creator;
    private Boolean isMultivalue;
    private static Logger log;

    public InfoClass(String _indexName, String _indexClassCreator,
	    Boolean _multi) {

	log = Logger.getLogger("dictionary");
	log.trace("class " + this.getClass().getName() + " initialized");
	this.index_name = _indexName;
	this.index_class_creator = _indexClassCreator;
	this.isMultivalue = _multi;

    }

    public String getIndexName() {
	return this.index_name;
    }

    public String getIndexClassCreatorName() {
	return this.index_class_creator;
    }

    public Boolean getIsMultivalue() {
	return this.isMultivalue;
    }

    public String toString() {

	return "index_name " + this.index_name + "index class creator "
		+ this.index_class_creator + "is multivalue "
		+ this.isMultivalue;

    }

}
