package net.verza.jdict;

/**
 * @author Christian Verdelli
 *
 */

import java.util.*;
import java.io.*;
import org.apache.log4j.*;

public class AudioFileLoader {

	private String audioFilesPath;
	private static Logger log;
	private Map<String, byte[]> dataTable;
	private File[] files;
	private int number_of_files;

	public AudioFileLoader() {
		log = Logger.getLogger("net.verza.jdict");
		log.trace("called class "+this.getClass().getName());
		dataTable = new HashMap<String, byte[]>();
		audioFilesPath = "";
		number_of_files = 0;
	}

	public AudioFileLoader(String audioDirectory) {
		setAudioFilesPath(audioDirectory);
	}

	// /////////////////////////////////////////////////////////////////////////////
	public int setAudioFilesPath(String newDir) {
		File tempdir = new File(newDir);
		if (!tempdir.isDirectory()) {
			log.error("The Audio Directory doesn't exist");
			return -1;
		}
		audioFilesPath = newDir;
		files = tempdir.listFiles(new FilenameFilter() {
			public boolean accept(File tempdir, String name) {
				return name.endsWith(".mp3");
			}
		});
		number_of_files = files.length;
		log.debug("Found " + number_of_files
				+ "files into the new Audio Directory " + audioFilesPath);

		return 0;
	}

	// /////////////////////////////////////////////////////////////////////////////
	public int loadAudioFiles() {

		for (int i = 0; i < number_of_files; i++) {
			try {
				byte[] tmp = files[i].getAbsolutePath().getBytes("UTF-8");
				String filePathUTF8 = new String(tmp);
				int lastSlash = filePathUTF8.lastIndexOf("/");
				// Get the file Name without the path
				String fileName = filePathUTF8.substring(lastSlash + 1);
				byte[] audio = getAudioData(files[i]);
				log.trace("Reading audio data of the file " + filePathUTF8);
				if (audio == null) {
					log.warn("Error giving audio data of the file"
							+ filePathUTF8);
					continue;
				}
				log.debug("audio size: " + audio.length);
				log.trace("pushing into the map " + fileName);
				dataTable.put(fileName, audio);
			} catch (UnsupportedEncodingException e) {
				log.error("Unsupported Encoding Exception: " + e.toString());
				e.printStackTrace();
			}
		}
		return 0;
	}

	// /////////////////////////////////////////////////////////////////////////////
	public byte[] getAudioData(File fileName) {
		int MAX_FILE_SIZE = 10000;
		FileInputStream in = null;
		int lenghtFile;
		byte[] buf = new byte[MAX_FILE_SIZE];

		try {
			in = new FileInputStream(fileName);
			lenghtFile = in.available();
			log.trace("InputStream size is " + lenghtFile);
			buf = new byte[lenghtFile];
			in.read(buf);
		} catch (FileNotFoundException e) {
			log.error(fileName + " does not exist.");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IO Exception: " + e.toString());
			e.printStackTrace();
		}

		return buf;
	}

	// /////////////////////////////////////////////////////////////////////////////
	public byte[] get(String filename) {
		if (!dataTable.containsKey(filename)) {
			log.warn("audio data not present for the filename " + filename);
			return null;
		}
		log.trace("Returning the audio data of the file " + filename);
		return (byte[]) dataTable.get(filename);
	}

	// /////////////////////////////////////////////////////////////////////////////
	public Map<String, byte[]> getAll() {
		if (dataTable.isEmpty()) {
			log.error("Error Fecthing Audio data");
			return null;
		}
		log.trace("Returing all the audio data. Size:" + dataTable.size());
		return dataTable;
	}

}
