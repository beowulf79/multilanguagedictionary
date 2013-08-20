package net.verza.jdict.dataloaders;

/**
 * @author Christian Verdelli
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.verza.jdict.exceptions.AudioNotFoundException;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;

import org.apache.log4j.Logger;

public class LocalFileSystemAudioFileLoader implements IAudioFileLoader {

    private String localAudioFolder;
    private static Logger log;

    //public LocalFileSystemAudioFileLoader(
	//    LanguageConfigurationClassDescriptor sub)
    public LocalFileSystemAudioFileLoader(String _audio_folder)
    	    throws AudioNotFoundException {
	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());

	//setAudioFilesPath(sub.audioPath);
	setAudioFilesPath(_audio_folder);
    }

    public void setAudioFilesPath(String folder) throws AudioNotFoundException {
	if (!new File(folder).isDirectory()) {
	    log.error("the audio Folder " + folder + " doesn't exist");
	    throw new AudioNotFoundException("cannot create audio folder: "
		    + folder);
	}
	localAudioFolder = folder;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.IAudioFileLoader#get(java.lang.String)
     */
    public byte[] get(String filename) throws UnsupportedEncodingException,
	    FileNotFoundException, IOException {

	String absoluteFileName = localAudioFolder.concat(filename).concat(
		".mp3");
	// if the file doesn't exist return null
	if (!new File(absoluteFileName).exists()) {
	    log.warn(" audio file not found: " + absoluteFileName);
	    return null;
	}

	int MAX_FILE_SIZE = 10000;
	FileInputStream in = null;
	int lenghtFile;
	byte[] buf = new byte[MAX_FILE_SIZE];
	byte[] tmp = new File(absoluteFileName).getAbsolutePath().getBytes(
		"UTF-8");

	String filePathUTF8 = new String(tmp);
	in = new FileInputStream(filePathUTF8);
	lenghtFile = in.available();
	log.debug("InputStream size is " + lenghtFile);
	buf = new byte[lenghtFile];
	in.read(buf);

	return buf;
    }

}
