package net.verza.jdict.dataloaders;

/**
 * @author Christian Verdelli
 *
 */

import java.io.*;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import org.apache.log4j.*;

public class LocalFileSystemAudioFileLoader implements IAudioFileLoader {

	private String audioFilesPath;
	private static Logger log;

	public LocalFileSystemAudioFileLoader(
			LanguageConfigurationClassDescriptor sub) {
		log = Logger.getLogger("net.verza.jdict");
		log.trace("called class " + this.getClass().getName());

		setAudioFilesPath(sub.audioPath);
	}

	// /////////////////////////////////////////////////////////////////////////////
	public void setAudioFilesPath(String newDir) {
		File tempdir = new File(newDir);
		if (!tempdir.isDirectory()) {
			log.error("The Audio Directory doesn't exist");
			return;
		}
		audioFilesPath = newDir;

	}

	/* (non-Javadoc)
	 * @see net.verza.jdict.IAudioFileLoader#get(java.lang.String)
	 */
	public byte[] get(String filename) throws UnsupportedEncodingException,
			FileNotFoundException, IOException {

		String absoluteFileName = audioFilesPath.concat(filename)
				.concat(".mp3");
		System.out.println("filename " + absoluteFileName);
		int MAX_FILE_SIZE = 10000;
		FileInputStream in = null;
		int lenghtFile;
		byte[] buf = new byte[MAX_FILE_SIZE];

		byte[] tmp = new File(absoluteFileName).getAbsolutePath().getBytes(
				"UTF-8");
		String filePathUTF8 = new String(tmp);
		// Get the file Name without the path
		//String fileName = filePathUTF8.substring(lastSlash + 1);
		//in = new FileInputStream(fileName);
		in = new FileInputStream(filePathUTF8);
		lenghtFile = in.available();
		log.trace("InputStream size is " + lenghtFile);
		buf = new byte[lenghtFile];
		in.read(buf);

		return buf;
	}

}
