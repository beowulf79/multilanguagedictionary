package net.verza.jdict.dictionary.sleepycat;

/**
 * @author ChristianVerdelli
 *
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import net.verza.jdict.dataloaders.ExcelDumper;
import net.verza.jdict.dataloaders.LoaderOptionsStore;
import net.verza.jdict.dictionary.Dictionary;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DatabaseImportException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LabelNotFoundException;
import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.exceptions.LinkIDException;
import net.verza.jdict.model.SearchableObject;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguageFieldConfigurationClassDescritor;
import net.verza.jdict.properties.LanguagesConfiguration;
import org.apache.log4j.Logger;
import com.sleepycat.je.DatabaseException;

public final class SleepyDatabaseDumper {

	private LoaderOptionsStore optionObj;
	private static Logger log;
	private ExcelDumper datadumper;
	private HashMap<String, Integer> importInfo;

	public SleepyDatabaseDumper() throws IllegalArgumentException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, SecurityException,
			FileNotFoundException, DatabaseException {
		log = Logger.getLogger("dictionary");
		log.trace("called class " + this.getClass().getName());
		this.importInfo = new HashMap<String, Integer>();
	}

	public void setOptionObject(LoaderOptionsStore _obj) {
		this.optionObj = _obj;
	}

	public HashMap<String, Integer> dumpDatabases() throws Exception {

		datadumper = new ExcelDumper(this.optionObj.getInputFile());

		this.iterateLanguages();

		return this.importInfo;

	}

	@SuppressWarnings("unchecked")
	private void iterateLanguages() throws DatabaseException,
			LabelNotFoundException, BiffException, IOException,
			KeyNotFoundException, DatabaseImportException,
			LanguagesConfigurationException, SecurityException,
			IllegalArgumentException, ClassNotFoundException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			DynamicCursorException, DataNotFoundException, LinkIDException,
			WriteException {

		Set<String> lang = LanguagesConfiguration.getAvalaibleLanguages();
		Iterator<String> it = lang.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();

			LanguageConfigurationClassDescriptor ldesc = LanguagesConfiguration
					.getLanguageMainConfigNode(key);

			log.info("creating sheet for language " + ldesc.getExcelSheet());
			datadumper.createSheet(ldesc.getExcelSheet());

			List<LanguageFieldConfigurationClassDescritor> fieldlist = ldesc
					.getFields();

			String nickname = ldesc.getLanguageNickname();
			String type = ldesc.getType();

			Class<?> IClass;
			IClass = Class.forName(ldesc.getClassQualifiedName());

			log.info("importing languge " + nickname + " type " + type);

			Dictionary dit = Factory.getDictionary();

			Vector<SearchableObject> objs = new Vector<SearchableObject>();
			objs = dit.read(nickname + type);

			Boolean labelscreated = false;

			for (int i = 0; i < objs.size(); i++) {
				SearchableObject sObj = objs.get(i);
				if (sObj == null)
					continue;

				for (Iterator<LanguageFieldConfigurationClassDescritor> fielditer = fieldlist
						.iterator(); fielditer.hasNext();) {
					LanguageFieldConfigurationClassDescritor tmp = (LanguageFieldConfigurationClassDescritor) fielditer
							.next();

					Method Method2Call;
					Class<?>[] methodTypes;

					String attribute_method = "get" + tmp.getAttributeName();
					Integer excelcolumn = tmp.getExcelColumnInDump();
					String label = tmp.getInputLabel();
					String excelFont = tmp.getExcelFont();
					datadumper.setWritableFont(excelFont);

					if (!labelscreated)
						log.info("creating label " + label);
					datadumper.createLabel(excelcolumn, label);

					Method2Call = IClass.getMethod(attribute_method, null);

					if ("primary".equals(tmp.getKey_type())) {
						datadumper.write(excelcolumn, i + 1,
								(Integer) Method2Call.invoke((objs.get(i))));

					} else if ("linkid".equals(tmp.getAttributeName())) {
						methodTypes = new Class[] { java.lang.String.class };
						Method2Call = IClass.getMethod(attribute_method,
								methodTypes);
						datadumper.write(excelcolumn, i + 1,
								(Integer[]) Method2Call.invoke((objs.get(i)),
										tmp.getInputLabel() + type));

					} else if ("section".equals(tmp.getAttributeName())
							|| "category".equals(tmp.getAttributeName())) {
						datadumper
								.write(excelcolumn, i + 1,
										(Set<String>) Method2Call.invoke((objs
												.get(i))));
					} else {
						// i+1 because at position 0 is for the label
						datadumper.write(excelcolumn, i + 1,
								(String) Method2Call.invoke((objs.get(i))));
					}

				}
				if (!labelscreated)
					labelscreated = true;

			}
		}
		log.info("languages iteration completed");
		datadumper.close();
	}

}
