package net.verza.jdict.dataloaders;

/**
 * @author Christian Verdelli
 *
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import net.verza.jdict.exceptions.AudioNotFoundException;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.PropertiesLoader;

import org.apache.log4j.Logger;

public class HowJSayAudioLoader implements IAudioFileLoader {

    private final static String NETWORK_TIMEOUT_XML_MARKUP = "network_timeout";
    private final static String SITE_URL_XML_MARKUP = "howjsay_audio_loader_url";
    private final static String SAVE2DISK_XML_MARKUP = "howjsay_audio_loader_savetodisk";
    private final static String OVERWRITE_XML_MARKUP = "howjsay_audio_loader_overwrite";

    private static Logger log;
    private Boolean save2disk;
    private Boolean overWrite;
    private String url;

    /*
     * stored in the property file, this parameter sets the timeout in seconds
     * to wait for the connection
     */
    private int timeout;
    private String localAudioFolder;
    private LocalFileSystemAudioFileLoader localLoader;

    public HowJSayAudioLoader(LanguageConfigurationClassDescriptor sub)
	    throws MalformedURLException, IOException {

	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());
	url = PropertiesLoader.getProperty(SITE_URL_XML_MARKUP,
		"http://www.howjsay.com/mp3/");
	timeout = new Integer(PropertiesLoader
		.getProperty(NETWORK_TIMEOUT_XML_MARKUP));
	log.info("download url set to " + url.toString());
	save2disk = PropertiesLoader.getBoolean(SAVE2DISK_XML_MARKUP);
	overWrite = PropertiesLoader.getBoolean(OVERWRITE_XML_MARKUP);
	setAudioFilesPath(sub.audioPath);
	testConnection();
	localLoader = new LocalFileSystemAudioFileLoader(sub);

    }

    public void testConnection() throws MalformedURLException, IOException {
	URL urlObj = new URL(url);
	log.debug("testing url " + url.toString());
	URLConnection test = urlObj.openConnection(); // if fails to
	// connect
	// throws IOException
	test.setConnectTimeout(timeout);
	test.connect();
	log.info("connection to " + url + " succeded");
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.verza.jdict.IAudioFileLoaderl#get(java.lang.String)
     */
    public byte[] get(String filename) throws MalformedURLException,
	    IOException {
	// if file already exists and overWrite is set to true call
	// LoaderFileSystemAudioLoader
	File fileObject = new File(localAudioFolder + filename + ".mp3");
	if ((fileObject.exists()) && (!overWrite)) {
	    log
		    .warn("audio file already present, overWrite option set to "
			    + overWrite
			    + "; overwrite existing files using LocalFileSystemAudioLoader");
	    return localLoader.get(filename);
	}

	byte[] data = null;

	// URLEncoder.encode substitues white spaces using + instead %20 . we
	// need the %20
	URL url_ = new URL(url
		+ (URLEncoder.encode(filename, "UTF-8")).replace("+", "%20")
		+ ".mp3");

	log.debug("final URL is :" + url_.toString());
	URLConnection urlConnection = url_.openConnection();
	urlConnection.setConnectTimeout(timeout);
	String contentType = urlConnection.getContentType();
	int contentLength = urlConnection.getContentLength();
	if (contentType.startsWith("text/") || contentLength == -1) {
	    log.error("audio file " + filename
		    + " is not binary. audio stream not downloaded");
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
	    Save2File(data, fileObject);

	log.debug("returning the audio data of the file " + filename);
	return data;
    }

    private void Save2File(byte[] audiostream, File fileObject)
	    throws FileNotFoundException, IOException {

	log.debug("saving audio file in file " + fileObject.getCanonicalPath());
	FileOutputStream out = new FileOutputStream(fileObject);
	out.write(audiostream);
	out.flush();
	out.close();

    }

    public void setAudioFilesPath(String folder) throws AudioNotFoundException {
	if (!new File(folder).isDirectory()) {
	    log.warn("the audio folder  " + folder + " doesn't exist");
	    boolean success = (new File(folder)).mkdir();
	    if (success) {
		log.info("audio directory: " + folder + " created");
	    } else
		throw new AudioNotFoundException("cannot create audio folder: "
			+ folder);

	}
	localAudioFolder = folder;
    }

}
