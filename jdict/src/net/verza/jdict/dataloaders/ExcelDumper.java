package net.verza.jdict.dataloaders;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.LabelCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableFont.FontName;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.properties.LanguageFieldConfigurationClassDescritor;
import net.verza.jdict.properties.PropertiesLoader;

import org.apache.log4j.Logger;

public class ExcelDumper {

	private File outputfile;
	private WritableCellFormat writableCellFormat;
	private WritableWorkbook workbook;
	private WritableSheet excelSheet;
	private String multivalueSeparator;

	private static Logger log;

	public ExcelDumper(File file) throws Exception {
		log = Logger.getLogger("jdict");
		log.trace("Default Contructor called ");
		outputfile = file;
		createWorkbook();
		multivalueSeparator = PropertiesLoader.getProperty(
				"multivalue_separator", "#");
		// setting default font
		setWritableFont(PropertiesLoader.getProperty("font_excel_default",
				"ARIAL"));
	}

	public void write(int column, int row, Integer value) throws IOException,
			WriteException {
		write(column, row, value.toString());
	}

	public void write(int column, int row, Integer[] values)
			throws IOException, WriteException {
		log.trace("writing at column " + column + " row " + row + " value "
				+ values);
		if (values == null) {
			log.error("skipping writing null into cell");
			return;
		}

		String tmp = new String();
		for (int i = 0; i < values.length; i++) {
			if (values[i]==null) {
				log.error("null value found in linkedid array, returning");
				return;
			}
			tmp = tmp + values[i] + multivalueSeparator;
		}
		tmp = tmp.substring(0, tmp.length() - 1); // remove last #

		Label label;
		label = new Label(column, row, tmp, writableCellFormat);
		excelSheet.addCell(label);
	}

	public void write(int column, int row, String value) throws IOException,
			WriteException {
		log.trace("writing at column " + column + " row " + row + " value "
				+ value);
		if ((value == null) || ("".equals(value))) {
			log.error("skiping writing null into cell");
			return;
		}

		Label label;
		label = new Label(column, row, value.toString(), writableCellFormat);
		excelSheet.addCell(label);
	}

	public void write(int column, int row, Set<String> values)
			throws IOException, WriteException {
		log.trace("writing at column " + column + " row " + row + " value "
				+ values);
		if ((values == null) || (values.size() == 0)) {
			log.error("skiping writing null into cell");
			return;
		}

		String tmp = new String();
		for (Iterator<String> sets = values.iterator(); sets.hasNext();) {
			tmp = tmp + (String) sets.next() + multivalueSeparator;
		}
		tmp = tmp.substring(0, tmp.length() - 1); // remove last #

		Label label;
		label = new Label(column, row, tmp, writableCellFormat);
		excelSheet.addCell(label);
	}

	private void createWorkbook() throws IOException {
		log.trace("createWorkbook called ");
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		workbook = Workbook.createWorkbook(outputfile, wbSettings);
	}

	public void createSheet(String sheetname) {
		log.trace("createSheet called with args " + sheetname);
		workbook.createSheet(sheetname, 0);
		excelSheet = workbook.getSheet(0);
	}

	public void createLabel(int column, String _label) throws WriteException {
		log.trace("createLabel called with args column:" + column
				+ " and label " + _label);
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);

		// Define the cell format
		WritableCellFormat times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = new WritableFont(
				WritableFont.TIMES, 10, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE);
		WritableCellFormat timesBoldUnderline = new WritableCellFormat(
				times10ptBoldUnderline);
		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);

		Label label;
		label = new Label(column, 0, _label, timesBoldUnderline);
		excelSheet.addCell(label);

	}

	public void setWritableFont(String _font) {
		log.trace("called method getWritableFont with argument " + _font);

		FontName font = WritableFont.createFont(_font);
		WritableFont writablefont = new WritableFont(font);
		writableCellFormat = new WritableCellFormat(writablefont);

	}

	public void close() throws IOException, WriteException {
		log.trace("called method close");

		workbook.write();
		workbook.close();
	}

}
