package testClass;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HowJSayTest {

    public static void main(String[] args) throws Exception {
	try {
	    URL yahoo = new URL("http://www.howjsay.com/mp3/car.mp3");
	    URLConnection yc = yahoo.openConnection();
	    BufferedReader in_ = new BufferedReader(new InputStreamReader(yc
		    .getInputStream()));
	    String contentType = yc.getContentType();
	    int contentLength = yc.getContentLength();
	    if (contentType.startsWith("text/") || contentLength == -1) {
		throw new IOException("This is not a binary file.");
	    }
	    InputStream raw = yc.getInputStream();
	    InputStream in = new BufferedInputStream(raw);
	    byte[] data = new byte[contentLength];
	    int bytesRead = 0;
	    int offset = 0;
	    while (offset < contentLength) {
		bytesRead = in.read(data, offset, data.length - offset);
		if (bytesRead == -1)
		    break;
		offset += bytesRead;
	    }

	    in.close();

	    String filename = new String();
	    filename = yahoo.getFile().substring(filename.lastIndexOf('/') + 1);
	    FileOutputStream out = new FileOutputStream("car.mp3");
	    out.write(data);
	    out.flush();
	    out.close();

	} catch (MalformedURLException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
    }

}
