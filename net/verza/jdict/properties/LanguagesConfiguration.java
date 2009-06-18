


package net.verza.jdict.properties;

import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Set;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.log4j.Logger;

public class LanguagesConfiguration {

	private static Logger log;
	private static HashMap<String, LanguageConfigurationClassDescriptor> languageConfigurationBlock;

	
	public LanguagesConfiguration() {

		log = Logger.getLogger("net.verza.jdict.properties");
		log.trace("called class " + this.getClass().getName());

		languageConfigurationBlock = new HashMap<String, LanguageConfigurationClassDescriptor>();

		this.discoverLanguages();

	}

	private void discoverLanguages() {

		log.trace("called function discoverLanguages");
		int counter = 0;

		List<SubnodeConfiguration> prop = PropertiesLoader
				.getHierarchicalProperty("language");

		for (Iterator<SubnodeConfiguration> it = prop.iterator(); it.hasNext();) {

			HierarchicalConfiguration sub = (HierarchicalConfiguration) it
					.next();

				
			String nickname = sub.getString("nickname");
			String type = sub.getString("type");

			LanguageConfigurationClassDescriptor langDesc = new LanguageConfigurationClassDescriptor();
			langDesc.setLanguageNickname( sub.getString("nickname"));
			langDesc.setType( sub.getString("type"));
			langDesc.setClassQualifiedName( sub.getString("class"));
			langDesc.setIsEnabled( sub.getBoolean("enabled"));
			langDesc.setIsAudioEnabled( sub.getBoolean("audio", false));
			langDesc.setAudioAttribute( sub.getString("audio_attribute"));
			langDesc.setAudioPath( sub.getString("audio_directory"));
			langDesc.setExcelSheet(sub.getString("excel_sheet"));
			langDesc.setQuizAttribute( sub.getString("quiz_attribute"));
			langDesc.setTranslations(sub.getStringArray("translations")); // returns the comma separated elements as string array
			langDesc.setFields(discoverFields(counter));

			languageConfigurationBlock.put(nickname + type, langDesc);

			log.debug("put into language config map language&type " + nickname+""+type
					+ " with Config Object  " + sub.toString());
			//languageMainConfigNodeMap.put(nickname, sub);
			//languageFieldsConfigNodeMap.put(nickname, discoverFields(counter) );

			counter++;
		}
		log.trace("number of languages found in property file: " + counter);

	}

	private List<SubnodeConfiguration> discoverFields(int _languageBookmark) {
		log.trace("called function discoverLabels");

		List<SubnodeConfiguration> prop = PropertiesLoader
				.getHierarchicalProperty("language(" + _languageBookmark
						+ ").fields.label");

		log.debug("put into Fields Node config map Config Object size "
				+ prop.size());
		return prop;

	}

	public static LanguageConfigurationClassDescriptor getLanguageMainConfigNode(
			String _language) {
		log.trace("called function getLanguageMainConfigNode with argument "
				+ _language);

		return languageConfigurationBlock.get(_language);
	}

	public static Set<String> getAvalaibleLanguages() {

		return languageConfigurationBlock.keySet();
	}

	public static List<SubnodeConfiguration> getFieldsConfigNode(
			String _language) {
		log.trace("called function getFieldsConfigNode with argument "
				+ _language);

		return getFieldsConfigNode(_language);

	}

	public static HashMap<String, LanguageConfigurationClassDescriptor> getLanguageConfigurationBlock() {
		log.trace("called function getLanguageConfigurationBlock");
		return languageConfigurationBlock;
	}

}
