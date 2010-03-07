package net.verza.jdict.properties;

import org.apache.log4j.Logger;

public class LanguageFieldConfigurationClassDescritor {

    private static Logger log;
    private String attributeName;
    private String inputLabel;
    private String key_type;
    private Boolean attributeHasAudio;
    private String index_class_creator;
    private Boolean multivalue;

    public LanguageFieldConfigurationClassDescritor() {

	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());

    }

    public String getAttributeName() {
	log.trace("called function getAttributeName; will return "
		+ this.attributeName);
	return this.attributeName;
    }

    public void setAttributeName(String _attributeName) {
	log.trace("called function setAttributeName with parameter  "
		+ _attributeName);
	this.attributeName = _attributeName;
    }

    public String getInputLabel() {
	log.trace("called function getInputLabel; will return "
		+ this.inputLabel);
	return inputLabel;
    }

    public void setInputLabel(String inputLabel) {
	log
		.trace("called function setInputLabel with parameter  "
			+ inputLabel);
	this.inputLabel = inputLabel;
    }

    public String getKey_type() {
	log.trace("called function getKey_type; will return " + this.key_type);
	return key_type;
    }

    public void setKey_type(String key_type) {
	log.trace("called function setKey_type with parameter  " + key_type);
	this.key_type = key_type;
    }

    public Boolean getAttributeHasAudio() {
	log.trace("called function getAttributeHasAudio; will return "
		+ this.attributeHasAudio);
	return attributeHasAudio;
    }

    public void setAttributeHasAudio(Boolean audio) {
	log.trace("called function setAudio to with parameter  " + audio);
	this.attributeHasAudio = audio;
    }

    public String getIndex_class_creator() {
	log.trace("called function getIndex_class_creator; will return "
		+ this.index_class_creator);
	return this.index_class_creator;
    }

    public void setIndex_class_creator(String _indexc) {
	log.trace("called function setIndex_class_creator  with parameter  "
		+ _indexc);
	this.index_class_creator = _indexc;
    }

    public Boolean getMultivalue() {
	log.trace("called function getMultivalue; will return "
		+ this.multivalue);
	return this.multivalue;
    }

    public void setMultivalue(Boolean multivalue) {
	log.trace("called function setMultivalue  with parameter   "
		+ multivalue);
	this.multivalue = multivalue;
    }

    public String toString() {
	return " attributeName " + attributeName + " inputLabel " + inputLabel
		+ " key_type " + key_type + " attributeHasAudio "
		+ attributeHasAudio + " index_class_creator "
		+ index_class_creator + " multivalue " + multivalue;
    }

}
