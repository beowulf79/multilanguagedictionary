package net.verza.jdict.verbs.arabic;

/**
 * 
 */

import net.verza.jdict.exceptions.DataNotFoundException;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

/**
 * @author ChristianVerdelli
 * 
 */
public class ArabVerbShortVowels {

    public static final char[] FATHA = { 'A' };
    public static final char[] KASRA = { 'A' };
    public static final char[] DAMMA = { 'A' };
    public static final char[] SOUKUN = { 'A' };
    public static final char[] FATHAIN = { 'A' };
    public static final char[] KASRAIN = { 'A' };
    public static final char[] DAMMAIN = { 'A' };
    public static final char[] SHADDA = { 'A' };
    public static final char[] SHADDAFATHA = { SHADDA[0], FATHA[0] };
    public static final char[] SHADDAKASRA = { SHADDA[0], KASRA[0] };
    public static final char[] SHADDADAMMA = { SHADDA[0], DAMMA[0] };
    public static final char[] ALIF = { 'A' };
    public static final char[] ALIFMADDA = { 'A' };
    public static final char[] ALIFMAQSURA = { 'A' };
    public static final char[] YA = { 'A' };
    public static final char[] WAW = { 'A' };

    private static Logger log;

    private char[] pastVowels;
    private char[] presentVowels;

    ArabVerbConversionTable paradigmConverstionTable;

    /*
     * Default Constructor
     */
    public ArabVerbShortVowels() {
	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());
	pastVowels = null;
	presentVowels = null;
    }

    /*
     * 
     */
    public ArabVerbShortVowels(String paradigm) throws DataNotFoundException {
	this();
	log.debug("called ShortVowels constructor using paradigm " + paradigm);

	paradigmConverstionTable = new ArabVerbConversionTable();
	paradigmConverstionTable.setParadigm(paradigm);
	log.debug("going to call code2vowel for paradigm " + paradigm);
	pastVowels = code2vowel(paradigmConverstionTable.getPastVowels());
	presentVowels = code2vowel(paradigmConverstionTable.getPresentVowels());
    }

    /*
     * 
     */

    public char[] getPastVowels() {
	if (pastVowels != null) {
	    return pastVowels;
	}

	return null;
    }

    public char[] getPresentVowels() {
	if (presentVowels != null) {
	    return presentVowels;
	}

	return null;
    }

    private static char[] getMappedChar(char ch) {
	log.trace("called function getMappedChar with argument " + ch);

	switch (ch) {
	case '1':
	    log.trace("returning FATHA");
	    return (FATHA);
	case '2':
	    log.trace("returning DAMMA");
	    return (DAMMA);
	case '3':
	    log.trace("returning KASRA");
	    return (KASRA);
	case '4':
	    log.trace("returning SOUKUN");
	    return (SOUKUN);
	case '5':
	    log.trace("returning FATHAIN");
	    return (FATHAIN);
	case '6':
	    log.trace("returning KASRAIN");
	    return (KASRAIN);
	case '7':
	    log.trace("returning DAMMAIN");
	    return (DAMMAIN);
	case 'S':
	    log.trace("returning SHADDA");
	    return (SHADDA);
	case '8':
	    log.trace("returning SHADDAFATHA");
	    return (SHADDAFATHA);
	case '9':
	    log.trace("returning SHADDAKASRA");
	    return (SHADDAKASRA);
	case '0':
	    log.trace("returning SHADDADAMMA");
	    return (SHADDADAMMA);
	case 'A':
	    log.trace("returning ALIF");
	    return (ALIF);
	case 'M':
	    log.trace("returning ALIFMADDA");
	    return (ALIFMADDA);
	case 'Q':
	    log.trace("returning ALIFMAQSURA");
	    return (ALIFMAQSURA);
	case 'Y':
	    log.trace("returning YA");
	    return (YA);
	case 'W':
	    log.trace("returning WAW");
	    return (WAW);
	default:
	    log.trace("mapped char not found");
	    return (null);

	}
    }

    private char[] code2vowel(char[] code) {
	char[] vowels = { 0, 0, 0, 0, 0, 0, 0, 0 };
	try {
	    Writer out = new BufferedWriter(new OutputStreamWriter(System.out,
		    "UTF-8"));
	    int i;

	    log.trace("decoding code " + new String(code));

	    for (i = 0; i < code.length; i++) {
		char[] temp = getMappedChar(code[i]);
		vowels[i] = temp[0];
		// In the case of Shadda + Short vowels
		if (temp.length > 1) {
		    vowels[i++] = temp[1];
		}
	    }
	    vowels[i + 1] = '\0';
	    log.trace("short vowels array          " + new String(vowels));
	    out.write("short vowels array:    " + new String(vowels) + "\n");
	    out.flush();

	} catch (UnsupportedEncodingException e) {
	    System.out.println("exception");
	} catch (IOException e) {
	    System.out.println("exception");
	}

	return vowels;
    }

}
