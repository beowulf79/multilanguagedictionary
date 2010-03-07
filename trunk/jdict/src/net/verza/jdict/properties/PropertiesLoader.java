package net.verza.jdict.properties;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

@SuppressWarnings( { "unchecked" })
public class PropertiesLoader {

    private static String propertyFilename = "conf/properties.xml";

    private static Logger log = null;
    private static XMLConfiguration config = null;

    public PropertiesLoader() {

	log = Logger.getLogger("jdict");
	log.info("loading properties from " + propertyFilename);
	log.trace("called class " + this.getClass().getName());

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

	log.trace("getProperty(" + prop_name + ")=" + return_value);
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

    public static Boolean getBoolean(String prop_name) {
	log.trace("asked for Boolean property " + prop_name);
	return config.getBoolean(prop_name);
    }

}
