package net.verza.jdict.properties;

import org.apache.log4j.Logger;

public class LanguageFieldConfigurationClassDescritor {

	private static Logger log;
	private String attributeName;
	private String inputLabel;
	private String key_type;
	private Boolean attributeHasAudio;
	private String audiosetmethod;	//method used to set audio into DirectoryObjects
	private String audioloaderclass; //class used to download/read audio stream/files
	private String audiodirectory;
	
	private String index_class_creator;
	private Boolean multivalue;
	private Integer excelColumn;
	private String excelFont;

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
		log.trace("called function setInputLabel with parameter  " + inputLabel);
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

	public String getAudioMethod() {
		log.trace("called function getAudioMethod; will return "
				+ this.audiosetmethod);
		return audiosetmethod;
	}

	public void setAudioMethod(String _audiomethod) {
		log.trace("called function setAudioMethod to with parameter  " + _audiomethod);
		this.audiosetmethod = _audiomethod;
	}
	
	
	public String getAudioDirectory() {
		log.trace("called function getAudioDirectory; will return "
				+ this.audiodirectory);
		return audiodirectory;
	}

	public void setAudioDirectory(String _audiodirectory) {
		log.trace("called function setAudioDirectory to with parameter  " + _audiodirectory);
		this.audiodirectory = _audiodirectory;
	}

	
	public String getAudioLoaderClass() {
		log.trace("called function getAudioLoaderClass; will return "
				+ this.audioloaderclass);
		return audioloaderclass;
	}

	public void setAudioLoaderClass(String _audioloaderclass) {
		log.trace("called function setAudioLoaderClass to with parameter  " + _audioloaderclass);
		this.audioloaderclass = _audioloaderclass;
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

	public Integer getExcelColumnInDump() {
		log.trace("called function getExcelColumnInDump; will return "
				+ this.excelColumn);
		return this.excelColumn;
	}

	public void setExcelColumnInDump(Integer excelColumnInDump) {
		log.trace("called function setExcelColumnInDump  with parameter   "
				+ excelColumnInDump);
		this.excelColumn = excelColumnInDump;
	}


	public void setExcelFont(String font) {
		log.trace("called function setExcelFont  with parameter   "
				+ font);
		this.excelFont = font;
	}
	
	public String getExcelFont() {
		log.trace("called function getExcelFont; will return "+ excelFont);
		return this.excelFont;
	}
	
	public String toString() {
		return " attributeName " + attributeName + " inputLabel " + inputLabel
				+ " key_type " + key_type + " attributeHasAudio "
				+ attributeHasAudio + " audiomethod " + audiosetmethod + " audioloadclass "+ audioloaderclass
				+ " audio directory "  +audiodirectory+  " index_class_creator "
				+ index_class_creator + " multivalue " + multivalue;
	}

}
