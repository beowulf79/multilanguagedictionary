package net.verza.jdict.utils;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * @author ChristianVerdelli
 * 
 */
public class FileUtil {

    // Passo alla funzione il percorso del file e restituisce l'estensione
    public static String checkFileType(String path2file) throws IOException {
	int returnValue;
	String absoluteChar2Match = ".";
	Logger log = Logger.getLogger("jdict");
	log.trace("called static function checkFileType");

	if (path2file == null)
	    throw new IOException();

	returnValue = path2file.lastIndexOf(absoluteChar2Match);

	if (returnValue != -1) {
	    return path2file.substring(returnValue + 1);
	} else {
	    log.error("invalid extension");
	    throw new IOException("invalid extension");

	}

    }

}