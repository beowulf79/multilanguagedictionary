package testClass;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.verza.jdict.properties.Configuration;
import net.verza.jdict.utils.CookieManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ForvoTest {

    private static Logger log;
    private static final String log4j_filename = Configuration.LOG4JCONF;
    static CookieManager cm;

    public static void main(String[] args) {

	PropertyConfigurator.configure(log4j_filename);
	System.out.println("log4j_filename " + log4j_filename);
	log = Logger.getLogger("jdict");
	System.out.println("log " + log.toString());
	log.info("Starting...");
	cm = new CookieManager();
	ForvoTest forvo = new ForvoTest();
	forvo.login();
	forvo.get();
    }

    private void get() {
	System.out.println("get");

	String filename = "زجاجة";

	try {
	    URL url_ = new URL("http://forvo.com/word/" + filename);
	    HttpURLConnection urlConnection = (HttpURLConnection) url_
		    .openConnection();
	    BufferedReader html = new BufferedReader(new InputStreamReader(
		    urlConnection.getInputStream()));

	    String inputLine;
	    String REGEX = "(/download/mp3/"
		    + (URLEncoder.encode(filename, "UTF-8"))
			    .replace("+", "%20") + "/ar/\\w+/)";
	    System.out.println("regex is: " + REGEX);
	    Pattern PATTERN = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
	    while ((inputLine = html.readLine()) != null) {
		// run the regular expression on each line until we find
		// what we
		// need
		System.out.println("line " + inputLine);
		Matcher matcher = PATTERN.matcher(inputLine);
		if (matcher.find()) {
		    System.out.println("found matching line :" + inputLine);

		    url_ = new URL(url_.getProtocol() + "://" + url_.getHost()
			    + matcher.group(1));
		    System.out.println("audio stream url " + url_);

		}
	    }

	    HttpURLConnection urlConnection2 = (HttpURLConnection) url_
		    .openConnection();
	    // adding authentication cookies to the request
	    cm.setCookies(urlConnection2);

	    byte[] data = null;
	    InputStream raw = urlConnection2.getInputStream();
	    InputStream in = new BufferedInputStream(raw);

	    if (urlConnection2.getContentType().startsWith("text/")
		    || urlConnection2.getContentLength() == -1) {
		System.out.println("audio file " + filename
			+ " is not binary. audio stream not downloaded");
	    } else
		System.out.println("downloading audio file " + filename);

	    int lenght = urlConnection2.getContentLength();
	    System.out.println("getContentLength() " + lenght);
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

	    FileOutputStream out = new FileOutputStream(new File(
		    "/Users/christianverdelli/Desktop/java.mp3"));
	    out.write(data);
	    out.flush();
	    out.close();

	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void login() {

	System.out.println("login");
	String parameters = "login=addisabeba&password=JuveBravo1";

	try {
	    URL loginURL = new URL("http://www.forvo.com/login/");

	    HttpURLConnection login = (HttpURLConnection) loginURL
		    .openConnection();

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

	    cm.storeCookies(login);

	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (ProtocolException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
