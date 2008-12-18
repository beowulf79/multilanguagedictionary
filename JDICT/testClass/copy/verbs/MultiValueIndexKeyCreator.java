package testClass.copy.verbs;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryMultiKeyCreator;
import java.util.Set;


public abstract class MultiValueIndexKeyCreator  implements SecondaryMultiKeyCreator {

	
	public abstract void createSecondaryKeys(SecondaryDatabase secondary,
			DatabaseEntry key, DatabaseEntry data,  Set result);

}