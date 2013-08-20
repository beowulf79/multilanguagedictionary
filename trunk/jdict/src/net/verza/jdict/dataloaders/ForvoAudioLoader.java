package net.verza.jdict.dataloaders;

/**
 * @author Christian Verdelli
 *
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.verza.jdict.exceptions.AudioNotFoundException;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.PropertiesLoader;
import net.verza.jdict.utils.CookieManager;

import org.apache.log4j.Logger;

/**
 * ForvoAudioLoader is a class that downloads mp3 audio file at the site forvo.
 * Each word is stored at http://www.forvosite.domain/word/WORD_WANTED, where
 * WORD_WANTED can be any word of any the language supported by Forvo. es.
 * http://www.forvo.com/word/ciao or http://www.forvo.com/word/ballare. ; inside
 * this html is stored the link to download the mp3 file. is in the form "<a
 * href="/download/mp3/WORD_WANTED/LANGUAGE/NUMBER/", where WORD_WANTED is the
 * word es. mamma, language is a two chars prefix identity the language es. it,
 * and number is a number associated with the word. Because this number cannot
 * be know, it is necessary to parse the html to retrieve the link of the mp3
 * file. The downloaded is allowed only to logged users.
 * 
 * @author christianverdelli
 * 
 */
public class ForvoAudioLoader implements IAudioFileLoader {

    private final static String NETWORK_TIMEOUT_XML_MARKUP = "network_timeout";
    private final static String SITE_URL_XML_MARKUP = "forvo_audio_loader_url";
    private final static String SITE_LOGIN_URL_XML_MARKUP = "forvo_audio_loader_login_url";
    private final static String SITE_LOGIN_USERNAME_XML_MARKUP = "forvo_audio_loader_login_username";
    private final static String SITE_LOGIN_PASSWORD_XML_MARKUP = "forvo_audio_loader_login_password";
    private final static String SAVE2DISK_XML_MARKUP = "forvo_audio_loader_savetodisk";
    private final static String OVERWRITE_XML_MARKUP = "forvo_audio_loader_overwrite";

    private static Logger log;
    /**
     * stored in the propertiy file, this parameter controls if the downloaded
     * file have to be saved locally
     */
    private Boolean save2disk;
    /**
     * stored in property file, if save2disk is true, this parameter controls if
     * existing files have to be overwritten
     */
    private Boolean overWrite;
    /**
     * stored in property file, this is the URL of the site;
     * 
     */
    private String url;
    /*
     * stored in the property file, this parameter sets the timeout in seconds
     * to wait for the connection
     */
    private int timeout;
    /**
     * 
     */
    private String localAudioFolder;
    /**
     * If an audio file is found in the local folder, use this class to get the
     * files instead of downloading them
     */
    private LocalFileSystemAudioFileLoader localLoader;
    /**
     * helper class used to store forvo login cookies needed to download mp3
     * files
     */
    private CookieManager cookieHandler;

    /**
     * Gets the URL of the site (forvo) where to find words to download,
     * 
     * @param sub
     * @throws MalformedURLException
     * @throws IOException
     */
    //public ForvoAudioLoader(LanguageConfigurationClassDescriptor sub)
    public ForvoAudioLoader(String _audio_folder)
	    throws MalformedURLException, IOException {

	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());
	cookieHandler = new CookieManager();
	url = PropertiesLoader.getProperty(SITE_URL_XML_MARKUP);
	timeout = new Integer(PropertiesLoader
		.getProperty(NETWORK_TIMEOUT_XML_MARKUP));
	log.info("download url set to " + url.toString());
	save2disk = PropertiesLoader.getBoolean(SAVE2DISK_XML_MARKUP);
	overWrite = PropertiesLoader.getBoolean(OVERWRITE_XML_MARKUP);
	//setAudioFilesPath(sub.audioPath);
	setAudioFilesPath(_audio_folder);
	testConnection();
	login();

	localLoader = new LocalFileSystemAudioFileLoader(_audio_folder);

    }

    /**
     * Test connection to the forvo site. If it fails, an exception is thrown.
     * 
     * @throws MalformedURLException
     * @throws IOException
     */
    public void testConnection() throws MalformedURLException, IOException {
	URL urlObj = new URL(url);
	log.debug("testing url " + url.toString());
	URLConnection test = urlObj.openConnection(); // if fails to
	// connect throws IOException
	test.setConnectTimeout(timeout);
	test.connect();
	log.info("connection to " + url + " succeded");
    }

    /**
     * get a file either locally using localAudioFolder is found in the local
     * audio folder, or remotely downloading from forvo site.
     * 
     * @param filename
     *                The word requested
     * @return a byte array rapresentation of the audio file; null either if the
     * word in not found or the URL is invalid @
     * @exception MalformedURLException,
     *                    IOException
     * @see net.verza.jdict.IAudioFileLoaderl#get(java.lang.String)
     */
    public byte[] get(String filename) throws MalformedURLException,
	    IOException {
	log.trace("called function get with arg " + filename.toString());

	// if file already exists and overWrite is set to false call
	// LoaderFileSystemAudioLoader and skip downloading the file
	File fileObject = new File(localAudioFolder + filename + ".mp3");
	if ((fileObject.exists()) && (!overWrite)) {
	    log
		    .info("audio file already present, overWrite option set to "
			    + overWrite
			    + "; returning existing files using LocalFileSystemAudioLoader and avoid downloading it");
	    return localLoader.get(filename);
	}

	URL audioURL = retrieveAudioURL(filename);
	if ((audioURL == null) || ("".equals(audioURL))) {
	    log.warn("url not valid, skipping");
	    return null;
	}

	byte[] data = retrieveAudioStream(audioURL);
	if (save2disk)
	    save2File(data, fileObject);

	return data;

    }

    /**
     * Save the audiostream in the local file system
     * 
     * @param audiostream
     *                the byte array rapresentation of the file
     * @param fileObject
     *                the file where to store the audio
     * @throws FileNotFoundException
     * @throws IOException
     */

    private void save2File(byte[] audiostream, File fileObject)
	    throws FileNotFoundException, IOException {
	log.trace("called function save2File with arg "
		+ audiostream.toString() + " " + fileObject.toString());
	log.info("saving audio file in file " + fileObject.getCanonicalPath());
	FileOutputStream out = new FileOutputStream(fileObject);
	out.write(audiostream);
	out.flush();
	out.close();

    }

    /**
     * Sets the folder where downloaded file will be saved if saved if save2disk
     * is set true.
     * 
     * @param folder
     *                Path where the files will be saved
     * @throws AudioNotFoundException
     *                 if the directory doesn't exist and cannot be created
     */

    public void setAudioFilesPath(String folder) throws AudioNotFoundException {
	if (!new File(folder).isDirectory()) {
	    log.trace("called function setAudioFilesPath " + folder.toString());
	    log.warn("the audio folder  " + folder + " doesn't exist");
	    boolean success = (new File(folder)).mkdir();
	    if (success) {
		log.info("audio directory: " + folder + " created");
	    } else
		log.error("AudioNotFoundException; cannot create audio folder "
			+ folder);
	    throw new AudioNotFoundException("cannot create audio folder: "
		    + folder);
	}
	localAudioFolder = folder;
    }

    /**
     * Login on forvo site; the login is done by posting the username and
     * password to the login URL; the authentication information is stored in
     * the cookie phpsession; after receiving this cookie all subsequents
     * requests must carry this cookie otherwise the session will be broken.
     * 
     * @throws
     */
    private void login() {
	log.trace("called function login");
	String parameters = "login="
		+ PropertiesLoader.getProperty(SITE_LOGIN_USERNAME_XML_MARKUP)
		+ "&password="
		+ PropertiesLoader.getProperty(SITE_LOGIN_PASSWORD_XML_MARKUP);
	log.debug("parameters :" + parameters);

	try {
	    URL loginURL = new URL(PropertiesLoader.getProperty(
		    SITE_LOGIN_URL_XML_MARKUP, "http://www.forvo.com/login/"));

	    log.debug("login url " + loginURL);

	    HttpURLConnection login = (HttpURLConnection) loginURL
		    .openConnection();
	    login.setConnectTimeout(timeout);
	    login.setInstanceFollowRedirects(false);
	    login.setRequestMethod("POST");
	    login.setRequestProperty("Content-Type",
		    "application/x-www-form-urlencoded");
	    login.setDoOutput(true);
	    login.setUseCaches(false);
	    DataOutputStream wr = new DataOutputStream(login.getOutputStream());
	    wr.writeBytes(parameters);
	    wr.flush();
	    wr.close();
	    cookieHandler.storeCookies(login);

	} catch (MalformedURLException e) {
	    log.error("MalformedURLException " + e.getMessage());
	} catch (ProtocolException e) {
	    log.error("ProtocolException " + e.getMessage());
	    e.printStackTrace();
	} catch (IOException e) {
	    log.error("IOException " + e.getMessage());
	    e.printStackTrace();
	}

    }

    /**
     * Parse the html file of the given word and parse it to retrieve the link
     * of the mp3 file.
     * 
     * @param word
     *                word to retrieve the audio file
     * @return the link where to find the mp3 audio file of the word; null if no
     *         link is found
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     */
    private URL retrieveAudioURL(String word)
	    throws UnsupportedEncodingException, MalformedURLException,
	    IOException {
	log.trace("called function retrieveAudioURL with arg "
		+ word.toString());

	// URLEncoder.encode substitues white spaces using + instead %20 . we
	// need the %20
	URL regexURL = new URL(url
		+ (URLEncoder.encode(word, "UTF-8")).replace("+", "%20"));
	log.debug("regex URL is :" + regexURL.toString());

	// first get the HTML page to extract the link to the audio file
	HttpURLConnection urlConnection = (HttpURLConnection) regexURL
		.openConnection();
	urlConnection.setConnectTimeout(timeout);
	if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	    log.warn("audio file " + word + " not found");
	    return null;
	}

	BufferedReader html = new BufferedReader(new InputStreamReader(
		urlConnection.getInputStream()));
	String inputLine;

	String REGEX = "(/download/mp3/"
		+ (URLEncoder.encode(word, "UTF-8")).replace("+", "%20")
		+ "/ar/\\w+)";
	log.debug("regex is: " + REGEX);
	Pattern PATTERN = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
	Boolean found = false;
	while ((inputLine = html.readLine()) != null) {
	    // run the regular expression on each line until we find
	    // what we need
		log.trace("html inputLine is "+inputLine);
	    Matcher matcher = PATTERN.matcher(inputLine);
	    if (matcher.find()) {
		log.debug("found matching line :" + inputLine);

		regexURL = new URL(regexURL.getProtocol() + "://"
			+ regexURL.getHost() + matcher.group(1));
		log.debug("audio stream url " + regexURL);
		found = true;
		break;
	    }
	}

	if (!found) {
	    log.warn("download link not found for the word " + word);
	    return null;
	}

	return regexURL;
    }

    /**
     * Download the mp3 file.
     * 
     * @param audioURL
     *                The URL where downloading the file
     * @return the byte array rapresentation of the mp3 file
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     */
    private byte[] retrieveAudioStream(URL audioURL)
	    throws UnsupportedEncodingException, MalformedURLException,
	    IOException {
	log.trace("called function retrieveAudioStream with arg "
		+ audioURL.toString());
	byte[] data = null;

	HttpURLConnection urlConnection2 = (HttpURLConnection) audioURL
		.openConnection();
	urlConnection2.setConnectTimeout(timeout);
	// adding authentication cookies to the request; otherwise downloading
	// is not alloweb by forvo
	cookieHandler.setCookies(urlConnection2);

	InputStream raw = urlConnection2.getInputStream();
	InputStream in = new BufferedInputStream(raw);

	if (urlConnection2.getContentType().startsWith("text/")
		|| urlConnection2.getContentLength() == -1) {
	    log.warn("audio file at URL " + audioURL.toString()
		    + " is not binary. audio stream not downloaded");
	} else {
	    log.info("downloading audio file at URL " + audioURL.toString());

	    int lenght = urlConnection2.getContentLength();
	    log.debug(" server return code " + urlConnection2.getResponseCode()
		    + " contentLenght :" + lenght);
	    data = new byte[lenght];
	    int bytesRead = 0;
	    int offset = 0;
	    while (offset < lenght) {
		bytesRead = in.read(data, offset, data.length - offset);
		if (bytesRead == -1)
		    break;
		offset += bytesRead;
	    }
	    in.close();

	}

	return data;
    }

}
