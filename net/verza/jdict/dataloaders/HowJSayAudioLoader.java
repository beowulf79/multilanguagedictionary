package net.verza.jdict.dataloaders;

/**
 * @author Christian Verdelli
 *
 */

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.PropertiesLoader;
import org.apache.log4j.*;

public class HowJSayAudioLoader implements IAudioFileLoader {

	private final static String LL = "howjsay_audio_loader_url";
	private final static String DD = "howjsay_audio_loader_savetodisk";
	private final static String GG = "howjsay_audio_loader_overwrite";

	private static Logger log;
	private Boolean save2disk;
	private Boolean overWrite;
	private String url;
	private String folder2save;

	public HowJSayAudioLoader(LanguageConfigurationClassDescriptor sub) {

		log = Logger.getLogger("net.verza.jdict");
		log.trace("called class " + this.getClass().getName());
		url = PropertiesLoader.getProperty(LL, "http://www.howjsay.com/mp3/");
		save2disk = PropertiesLoader.getBoolean(DD);
		overWrite = PropertiesLoader.getBoolean(GG);
		folder2save = sub.audioPath;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.verza.jdict.IAudioFileLoaderl#get(java.lang.String)
	 */
	public byte[] get(String filename) throws MalformedURLException,
			IOException {

		byte[] data = null;

		URL urlObj = new URL(url + filename + ".mp3");
		URLConnection urlConnection = urlObj.openConnection();
		String contentType = urlConnection.getContentType();
		int contentLength = urlConnection.getContentLength();
		if (contentType.startsWith("text/") || contentLength == -1) {
			log.warn("audio file is not binary. error!");
			return null;
		}

		InputStream raw = urlConnection.getInputStream();
		InputStream in = new BufferedInputStream(raw);
		data = new byte[contentLength];
		int bytesRead = 0;
		int offset = 0;
		while (offset < contentLength) {
			bytesRead = in.read(data, offset, data.length - offset);
			if (bytesRead == -1)
				break;
			offset += bytesRead;
		}

		in.close();
		if (save2disk)
			Save2File(data, filename);

		log.trace("Returning the audio data of the file " + filename);
		return data;
	}

	private void Save2File(byte[] audiostream, String filename)
			throws FileNotFoundException, IOException {

		File fileObject = new File(folder2save + filename + ".mp3");
		log.info("saving audio file in file " + fileObject.getCanonicalPath());
		if ( (fileObject.exists()) && (!overWrite)) 
			return;
		
		FileOutputStream out = new FileOutputStream(fileObject);
		out.write(audiostream);
		out.flush();
		out.close();

	}

}
