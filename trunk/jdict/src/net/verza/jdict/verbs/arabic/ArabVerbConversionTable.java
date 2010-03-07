package net.verza.jdict.verbs.arabic;

import net.verza.jdict.exceptions.DataNotFoundException;

import org.apache.log4j.Logger;

/**
 * @author ChristianVerdelli
 * 
 */
public class ArabVerbConversionTable {

    private static Logger log;

    private char[] past_vowels;
    private char[] present_vowels;

    public ArabVerbConversionTable() {
	log = Logger.getLogger("jdict");
	log.trace("ConversionTable contructor called");

	new ArabVerbPastParadigm2ShortVowels();
	new ArabVerbPresentParadigm2ShortVowels();
	past_vowels = new char[2];
	present_vowels = new char[2];
    }

    public char[] getPastVowels() {

	if (past_vowels != null) {
	    return past_vowels;
	}
	log.error("invalid past vowels");
	return null;
    }

    public char[] getPresentVowels() {
	if (present_vowels != null) {
	    return present_vowels;
	}
	log.error("invalid present vowels");
	return null;
    }

    public void setParadigm(String _paradigm) throws DataNotFoundException {
	log.debug("setting paradigm to " + _paradigm);

	if (_paradigm == null) {
	    log.error("invalid paradigm received, throwing exception ");
	    throw new DataNotFoundException(
		    "the paradigm code given is not valid");
	}

	past_vowels = ArabVerbPastParadigm2ShortVowels
		.getShortVowels(_paradigm).toCharArray();
	present_vowels = ArabVerbPresentParadigm2ShortVowels.getShortVowels(
		_paradigm).toCharArray();

    }

}
