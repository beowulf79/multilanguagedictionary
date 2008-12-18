package testClass.copy.verbs;


import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.DatabaseEntry;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;
import net.verza.jdict.sleepycat.datastore.SleepyWordsDatabase;



/**
 * @author ChristianVerdelli
 * 
 */
public class SetIndexKeyCreator extends MultiValueIndexKeyCreator  {

	SleepyWordsDatabase db;
	private static Logger log;
	private Class<?> Class2Load;
	private Constructor<?> constr;
	private SearchableObject obj;
	private String indexName,languageName,methodName;
	
	
	public SetIndexKeyCreator(String languageName, 
															String className, 
															String index_name) {
		
		log = Logger.getLogger("testClass.copy.verbs");
		log.trace("called class " + this.getClass().getName());
		
		this.languageName = languageName;
		this.indexName = index_name;
		// the setter method must always be the attribute name prefixed with the string set
		this.methodName = "get" + index_name;
		
		try {
			Class2Load = Class.forName(className);
			Class<?>[] c_arr = new Class[] {};
			constr = Class2Load.getConstructor(c_arr);
			obj = (SearchableObject) constr.newInstance();
			log.debug("instantied class "+obj.getClass().getName());

			
		} catch (ClassNotFoundException e) {
			log.error("ClassNotFoundException "+e.getMessage());
			e.printStackTrace();
		}  catch (NoSuchMethodException e) {
			e.printStackTrace();
		}  catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e)  {
			e.printStackTrace();
		}
	
	}

	
	
	public void createSecondaryKeys(SecondaryDatabase secondary,
																	DatabaseEntry key, 
																	DatabaseEntry data, 
																	Set result) {
		try {
			
			log.trace("called createSecondaryKey with data "+data.toString());
			obj = (SearchableObject)SleepyBinding.getDataBinding().entryToObject(data);

			Class<?>[] methodTypes = new Class[] { };
			Method Method2Call;
			
			Method2Call = Class2Load.getMethod(this.methodName, methodTypes);
			Set<String> value = (Set<String>) Method2Call.invoke(obj, (Object[])null);
			
			
			if (value.isEmpty() ) {
				log.error("empty set, index  " + indexName
						+ " for the entry will not be set");
			} else {
				
				
					log.debug("setting " + indexName
						+ " index for the entry with value "+value 
						+ " using the object " +obj.getClass().getName()
						+ " using method " +this.methodName);

					this.copyKeysToEntries((Set)value, result);
					
					
			}
			
			
			
		}  catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (KeyNotFoundException e) {
			e.printStackTrace();
		} 
		
	}	

	
	
	/**
	 * A utility method to copy a set of keys (Strings) into a set of
	 * DatabaseEntry objects.
	 */
	private void copyKeysToEntries(Set<String> keys, Set<DatabaseEntry> entries) {
		StringBinding keyBinding = new StringBinding();
		log.info("setting multi value " + indexName + " index for the entry");
		for (Iterator<String> i = keys.iterator(); i.hasNext();) {
			String s = (String) i.next();
			log.debug("index value " + s);
			DatabaseEntry entry = new DatabaseEntry();
			keyBinding.objectToEntry(s, entry);
			entries.add(entry);
		}
	}
	
	
}
