package testClass.copy.verbs;


import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Set;
import net.verza.jdict.PropertiesLoader;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.log4j.Logger;


public class LanguagesConfiguration {
	
	private static Logger log;
	//private static HashMap<String,HierarchicalConfiguration>  languageMainConfigNodeMap;
	//private static HashMap<String,List<SubnodeConfiguration>>  languageFieldsConfigNodeMap;

	
	private static HashMap<String,LanguageConfigurationClassDescriptor> nuovo;
	
	
	public LanguagesConfiguration()	{
		
		log = Logger.getLogger("testClass.copy.verbs");
		log.trace("called class " + this.getClass().getName());
		
		nuovo = new HashMap<String,LanguageConfigurationClassDescriptor >();
		
		this.discoverLanguages();
		
	}
	
	
	private void discoverLanguages()	{

			log.trace("called function discoverLanguages");
			int counter=0;
			
			List<SubnodeConfiguration> prop = PropertiesLoader
					.getHierarchicalProperty("language");
			
			for (Iterator<SubnodeConfiguration> it = prop.iterator(); it
					.hasNext();) {

				HierarchicalConfiguration sub = (HierarchicalConfiguration) it
						.next();
				String nickname = sub.getString("nickname");
				String type = sub.getString("type");
				
				LanguageConfigurationClassDescriptor langDesc = new LanguageConfigurationClassDescriptor();
				langDesc.languageNickname = sub.getString("nickname");
				langDesc.type = sub.getString("type");
				langDesc.classQualifiedName = sub.getString("class");	
				langDesc.isEnabled = sub.getBoolean("enabled");
				langDesc.audioAttribute = sub.getString("audio_attribute");
				langDesc.audioPath = sub.getString("audio_directory");
				langDesc.excelSheet = sub.getString("excel_sheet");
				langDesc.setFields(discoverFields(counter));
				
				nuovo.put(nickname+type, langDesc  );
				
				log.debug("put into language config map language "+nickname
						+ " with Config Object  "+sub.toString() );
				//languageMainConfigNodeMap.put(nickname, sub);
				//languageFieldsConfigNodeMap.put(nickname, discoverFields(counter) );
				
				counter++;
			}
			log.trace("number of languages found in property file: " +counter);
			
	}
	
	
	private List<SubnodeConfiguration>  discoverFields(int _languageBookmark)	{
		log.trace("called function discoverLabels");
		
		List<SubnodeConfiguration> prop = PropertiesLoader
		.getHierarchicalProperty("language(" + _languageBookmark
				+ ").fields.label");
		
		
		
		log.debug("put into Fields Node config map Config Object size "+prop.size() );
		return prop;
		
	}
	
	
	public static LanguageConfigurationClassDescriptor getLanguageMainConfigNode(String _language)		{
		log.trace("called function getLanguageMainConfigNode with argument "+_language);
		
		return nuovo.get(_language);
	}
	
	
	/*public static HashMap<String,HierarchicalConfiguration> getLanguageMainConfigNode()		{
		log.trace("called function getLanguageMainConfigNode");
		
		return languageMainConfigNodeMap;
	}*/
	
	
	public static Set<String> getAvalaibleLanguages()	{
		
		return nuovo.keySet();
	}
	
	
	public  static List<SubnodeConfiguration> getFieldsConfigNode(String _language)		{
		log.trace("called function getFieldsConfigNode with argument "+_language);
		
		return getFieldsConfigNode(_language);
		
	}
	
	
	public  static HashMap<String,LanguageConfigurationClassDescriptor> getNuovo()	{
		log.trace("called function getNuovo");
		return  nuovo;
	}
	
}
