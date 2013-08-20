package net.verza.jdict.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.log4j.Logger;

public class LanguageConfigurationClassDescriptor {

	private Logger log;
	public Boolean isEnabled;
	public Boolean isAudioEnabled;
	public String type;
	public String languageNickname;
	public String editorClass;
	public String audioLoaderClass;
	public String audioAttribute;
	public String audioPath;
	public String excelSheet;
	public String classQualifiedName;
	public String[] translations;
	public String quizAttribute;
	public List<LanguageFieldConfigurationClassDescritor> fields;

	public LanguageConfigurationClassDescriptor() {
		log = Logger.getLogger("jdict");
		log.trace("called class " + this.getClass().getName());
		fields = new ArrayList<LanguageFieldConfigurationClassDescritor>();
	}

	public Boolean isEnabled() {
		log.trace("get method getIsEnabled " + this.isEnabled);
		return isEnabled;
	}

	public void setIsEnabled(Boolean _enabled) {
		log.trace("called method setIsEnabled with argument " + _enabled);
		this.isEnabled = _enabled;
	}

	public Boolean isAudioEnabled() {
		log.trace("called method getIsAudioEnabled; will return "
				+ this.isAudioEnabled);
		return this.isAudioEnabled;
	}

	public void setIsAudioEnabled(Boolean _audio) {
		log.trace("called method setAudioEnabled with argument  " + _audio);
		this.isAudioEnabled = _audio;
	}

	public String getEditorClass() {
		return editorClass;
	}

	public void setEditorClass(String editorClass) {
		this.editorClass = editorClass;
	}

	public String getAudioLoaderClass() {
		return audioLoaderClass;
	}

	public void setAudioLoaderClass(String audioLoaderClass) {
		this.audioLoaderClass = audioLoaderClass;
	}

	public String getType() {
		log.trace("called method getType; will return " + this.type);
		return type;
	}

	public void setType(String _type) {
		log.trace("called method setType with argument " + _type);
		this.type = _type;
	}

	public String getLanguageNickname() {
		log.trace("called method getLanguageNickname; will return "
				+ this.languageNickname);
		return this.languageNickname;
	}

	public void setLanguageNickname(String _languageNickname) {
		log.trace("called method setLanguageNickname with argument "
				+ _languageNickname);
		this.languageNickname = _languageNickname;
	}

	public String getAudioAttribute() {
		log.trace("called method getAudioAttribute; will return "
				+ this.audioAttribute);
		return audioAttribute;
	}

	public void setAudioAttribute(String _audioAttribute) {
		log.trace("get method setAudioAttribute to " + _audioAttribute);
		this.audioAttribute = _audioAttribute;
	}

	public String getAudioPath() {
		log.trace("called method getAudioPath; will return " + this.audioPath);
		return audioPath;
	}

	public void setAudioPath(String _audioPath) {
		log.trace("called method setAudioPath with argument " + _audioPath);
		this.audioPath = _audioPath;
	}

	public String getExcelSheet() {
		log.trace("called method getExcelSheet; will return " + this.excelSheet);
		return excelSheet;
	}

	public void setExcelSheet(String _excelSheet) {
		log.trace("called method setExcelSheet with argument " + _excelSheet);
		this.excelSheet = _excelSheet;
	}

	public String getClassQualifiedName() {
		log.trace("called method getClassQualifiedName; will return "
				+ this.classQualifiedName);
		return this.classQualifiedName;
	}

	public void setClassQualifiedName(String _classQualifiedName) {
		log.trace("called method setClassQualifiedName with argument "
				+ _classQualifiedName);
		this.classQualifiedName = _classQualifiedName;
	}

	public String[] getTranslations() {
		log.trace("called method getTranslations; will return "
				+ this.translations);
		return this.translations;
	}

	public void setTranslations(String[] _trans) {
		log.trace("called method setTranslation with argument array of string having size "
				+ _trans.length);
		this.translations = _trans;
	}

	public String getQuizAttribute() {
		log.trace("called method getQuizAttribute; will return "
				+ this.quizAttribute);
		return this.quizAttribute;
	}

	public void setQuizAttribute(String _attr) {
		log.trace("called method setQuizAttribute with argument " + _attr);
		this.quizAttribute = _attr;
	}

	public List<LanguageFieldConfigurationClassDescritor> getFields() {
		log.trace("called method getFields; will return " + this.fields);
		return this.fields;
	}
	
	/*public String getField(String _fieldname)	{
		
	}*/
	
	public void setFields(List<SubnodeConfiguration> _fields) {

		LanguageFieldConfigurationClassDescritor tmp = null;

		for (Iterator<SubnodeConfiguration> it = _fields.iterator(); it
				.hasNext();) {
			HierarchicalConfiguration sub = (HierarchicalConfiguration) it
					.next();
			tmp = new LanguageFieldConfigurationClassDescritor();
			tmp.setAttributeName(sub.getString("class_attribute"));
			tmp.setKey_type(sub.getString("key_type"));
			tmp.setAttributeHasAudio((sub.getBoolean("audio_enabled", false)));
			tmp.setAudioMethod((sub.getString("audio_method")));
			tmp.setAudioDirectory((sub.getString("audio_directory")));
			tmp.setAudioLoaderClass((sub.getString("audio_loader_class")));
			tmp.setIndex_class_creator(sub.getString("index_class_creator"));
			tmp.setInputLabel(sub.getString("label_name"));
			tmp.setMultivalue(sub.getBoolean("multivalue", false));
			tmp.setExcelColumnInDump(sub
					.getInt("column_position_excel_dump", 0));
			tmp.setExcelFont(sub.getString("font_excel_dump",
					PropertiesLoader.getProperty("font_excel_default", "Arial")));
			log.trace("class_attribute " + sub.getString("class_attribute")
					+ " key type" + sub.getString("key_type")
					+ " Attribute has audio "
					+ sub.getBoolean("audio_enabled", false)
					+ " Index Class Key Creator "
					+ sub.getString("index_class_creator") + " Label Name "
					+ sub.getString("label_name") + " is Multivalue "
					+ sub.getBoolean("multivalue", false)
					+ " excel column in dump "
					+ +sub.getInt("column_position_excel_dump", 0));

			fields.add(tmp);

		}

	}

	public String toString() {

		String toReturn = null;

		toReturn = "isEnabled " + isEnabled + " isAudioEnabled "
				+ isAudioEnabled + " type " + type + " languageNickname "
				+ languageNickname + " audioAttribute " + audioAttribute
				+ " audioPath " + audioPath + " excelSheet " + excelSheet
				+ " classQualifiedName " + classQualifiedName;

		List<LanguageFieldConfigurationClassDescritor> lista = this.fields;
		for (Iterator<LanguageFieldConfigurationClassDescritor> it = lista
				.iterator(); it.hasNext();) {

			LanguageFieldConfigurationClassDescritor tmp = (LanguageFieldConfigurationClassDescritor) it
					.next();
			toReturn.concat("--  fields " + tmp.toString());
			
		}

		return toReturn;

	}

}
