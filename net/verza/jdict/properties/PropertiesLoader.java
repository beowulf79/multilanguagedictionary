package net.verza.jdict.properties;

import java.util.*;
import org.apache.log4j.Logger;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.ConfigurationException;

@SuppressWarnings( { "unchecked" })
public class PropertiesLoader {

	private static String propertyFilename = "/Users/ChristianVerdelli/documents/workspace/jdict/conf/properties.xml";

	private static Logger log = null;
	private static XMLConfiguration config = null;

	public PropertiesLoader() {

		log = Logger.getLogger("net.verza.jdict.properties");
		log.info("loading properties from " + propertyFilename);

		loadConfiguration();

	}

	private static void loadConfiguration() {
		try {

			config = new XMLConfiguration(propertyFilename);
		} catch (ConfigurationException e) {
			log.fatal("Impossibile caricare il file di properties "
					+ propertyFilename, e);
			e.printStackTrace();
		}
	}

	public static String getProperty(String prop_name, String default_value) {
		String return_value = null;
		if (config == null) {
			log.fatal("properties file not loaded");
			return null;
		} else
			return_value = (String) config.getProperty(prop_name);
		return_value = (String) config.getString(prop_name);

		log.debug("getProperty(" + prop_name + ")=" + return_value);
		return return_value;
	}

	public static List<org.apache.commons.configuration.SubnodeConfiguration> getHierarchicalProperty(
			String prop_name) {
		log.trace("asked for property " + prop_name);
		return config.configurationsAt(prop_name);
	}

	public static String getProperty(String prop_name) {
		log.trace("asked for property " + prop_name);
		return getProperty(prop_name, "");
	}

}
