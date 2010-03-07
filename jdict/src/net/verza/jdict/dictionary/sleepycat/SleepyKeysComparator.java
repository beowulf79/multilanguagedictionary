package net.verza.jdict.dictionary.sleepycat;

import java.io.UnsupportedEncodingException;
import java.util.Comparator;

public class SleepyKeysComparator implements Comparator {

    public SleepyKeysComparator() {
    }

    public int compare(Object d1, Object d2) {

	System.out.println("d1 " + d1 + " d2 " + d2);
	byte[] b1 = (byte[]) d1;
	byte[] b2 = (byte[]) d2;

	String s1 = null, s2 = null;
	try {
	    s1 = new String(b1, "UTF-8");
	    s2 = new String(b2, "UTF-8");

	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return s1.compareTo(s2);
    }
}
