package net.verza.jdict.dataloaders;

/**
 * @author ChristianVerdelli
 *
 */

import java.util.*;
import java.io.*;

import jxl.*;
import jxl.read.biff.BiffException;
import net.verza.jdict.exceptions.LabelNotFoundException;
import org.apache.log4j.*;

public class ExcelLoader {

	private File file;
	private Workbook readWorkbook;
	private Sheet sheet;
	private String sheetName;
	private String columnName;
	private int columnsNumber; // number of fields in the excel file
	private static Logger log;

	public ExcelLoader(File fileObj) {
		log = Logger.getLogger("net.verza.jdict.dataloaders");
		log.trace("Default Contructor called ");
		this.file = fileObj;
		columnsNumber = 0;
		PropertyConfigurator.configure("../conf/log4j.properties");

	}

	public void setSheetName(String s) {
		log.trace("setting sheet name to " + s);
		sheetName = s;
	}

	public void setColumnName(String label) {
		this.columnName = label;
	}

	public int setColumnsNumber(int newValue) {
		if (newValue <= 0) {
			return -1;
		}
		log.trace("setting column number to " + newValue);
		this.columnsNumber = newValue;
		return 0;
	}

	public int getColumnsNumber() {
		return this.columnsNumber;
	}

	public Vector<String> read() throws LabelNotFoundException, IOException,
			BiffException {

		log.trace("called function read");

		this.readWorkbook = Workbook.getWorkbook(file);

		sheet = readWorkbook.getSheet(this.sheetName);
		if (sheet == null) { // Non esiste nessun foglio sheetName
			log.error("sheet " + this.sheetName + "not found");
		}
		log.debug("found sheet :  " + sheet.getName());

		int[] coords = findLabel(this.columnName);
		
		return readField(coords[0], coords[1], sheet, false);
		
	}

	/*
	 * Riceve in ingresso delle coordinate dell'etichetta Language e popola un
	 * vettore con gli utenti trovati in quella colonna.
	 */
	private Vector<String> readField(int x, int y, Sheet s, boolean nullCheck)
			throws UnsupportedEncodingException {

		int y_coord = y;
		int x_coord = x + 1;// the x given as parameter is the Label, for the
		// data i have to add 1
		Cell tmp;
		Vector<String> v = new Vector<String>();
		nullCheck = true;

		int numberOfRows = s.getRows();
		log.trace("rows in the Sheet " + numberOfRows);
		log
				.debug("fetching data from coordinates " + (x_coord + 1)
						+ " - " + y);

		while (x_coord != numberOfRows) {
			tmp = s.getCell(y_coord, x_coord);

			// if empty add null to the vector to keep the order in the vector with the other labels
			if ((tmp.getType().equals(CellType.EMPTY))
					|| (tmp.getContents().equalsIgnoreCase(null))) {
				v.addElement(null);
				log.trace("empty cell found");
				x_coord++;
				continue;
			}

			log.trace("cell font " + tmp.getCellFormat().getFont().getName()
					+ " - type " + tmp.getType());
			log.trace("cell content " + tmp.getContents());

			v.addElement(tmp.getContents());
			//	v.addElement( new String(tmp.getContents().getBytes("UTF-8" ))  );

			x_coord++;
		}

		log.trace("returing vector of size " + v.size() + " to the caller");
		return v;

	}

	private int[] findLabel(String label) throws LabelNotFoundException {

		log.trace("Called function findLabel with arg " + label);

		int x_coord;
		int y_coord;

		LabelCell startingCell = sheet.findLabelCell(label);
		if (startingCell == null) {
			log.warn("label " + label + "not found");
			throw new LabelNotFoundException("label " + label + " not found in the excel file ");
		}

		log.trace("label found " + startingCell.getString());

		// Ottengo le coordinate riga/colonna dell'etichetta cercata in
		// precedenza
		// Nel Debug uso +1 perche JXL parte dallo 0
		x_coord = startingCell.getRow();
		y_coord = startingCell.getColumn();
		log.trace("label " + label + " found in the position " + " x:"
				+ (x_coord + 1) + " y:" + (y_coord + 1));

		Cell tmp = sheet.getCell(y_coord, x_coord);
		log.debug("Content " + tmp.getContents());

		int[] retVal = { x_coord, y_coord };
		return (retVal);
	}

}
