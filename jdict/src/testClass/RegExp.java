package testClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExp {

    public static void main(String[] args) throws Exception {

	String url = "خاتم";
	String[] aa = new String[3];
	aa[0] = "http:// forvo.com/word/" + url;
	aa[1] = "Download MP3 <a href=\"/download/mp3/" + url + "/ar/79938/\"";
	aa[2] = "stella";

	String REGEX = "(/download/mp3/" + url + "/ar/\\w+/)";
	Pattern PATTERN = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);

	for (int i = 0; i < aa.length; i++) {
	    Matcher matcher = PATTERN.matcher(aa[i]);
	    System.out.println("line " + aa[i]);
	    if (matcher.find()) {
		System.out.println("found matching line :" + aa[i] + " group "
			+ matcher.group(1));
	    }
	}
    }
}
