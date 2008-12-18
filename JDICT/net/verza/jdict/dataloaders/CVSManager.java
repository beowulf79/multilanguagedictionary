package net.verza.jdict.dataloaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 
 */

/**
 * @author ChristianVerdelli
 * 
 */
public class CVSManager implements FileExtensionBinder {

	private final static String separator = "#";

	private Vector<String> data;

	public int read(String path, int numFields) {

		List records = new ArrayList();

		try {
			String theLine = null;
			// FileInputStream fis = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new FileReader(path));
			while ((theLine = br.readLine()) != null) {
				String[] theLineArray = theLine.split(separator);

				if (theLineArray.length > numFields) {
					System.out.println("Malformed line found in " + path);
					System.out.println("Line was: '" + theLine);
					/*
					 * System.out.println("length found was: " +
					 * theLineArray.length);
					 */// System.exit(-1);
				}
				records.add(theLineArray);
			}
			// Close the input stream handle
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println(path + " does not exist.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO Exception: " + e.toString());
			e.printStackTrace();
			System.exit(-1);
		}

		return 0;
	}

	public List<String> get() {

		return data;
	}

}
