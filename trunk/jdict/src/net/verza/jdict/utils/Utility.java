/**
 * 
 */
package net.verza.jdict.utils;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * @author christianverdelli
 * 
 */
public class Utility {

    private static Logger log;

    public Utility() {

    }

    /**
     * converts a String array into an Integer Array
     */
    public static Integer[] StringAR2IntgerAR(String args[]) {
	log = Logger.getLogger("jdict");
	log.trace("called function StringAR2IntgerAR");
	Integer arr[] = new Integer[args.length];
	log.trace("Size of the String array to convert into Integer array");
	for (int i = 0; i < arr.length; i++) {
	    arr[i] = Integer.parseInt(args[i]);
	}

	return arr;
    }

    public static final byte[] intToByteArray(int value) {
	return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
		(byte) (value >>> 8), (byte) value };
    }

    public static int byteArrayToInt(byte[] intBytes) {
	ByteBuffer bb = ByteBuffer.wrap(intBytes);
	return bb.getInt();
    }

}
