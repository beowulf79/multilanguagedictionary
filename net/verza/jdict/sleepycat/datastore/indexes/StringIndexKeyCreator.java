package net.verza.jdict.sleepycat.datastore.indexes;


import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.DatabaseEntry;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
import net.verza.jdict.SearchableObject;
import net.verza.jdict.sleepycat.datastore.SleepyDatabase;
import net.verza.jdict.sleepycat.datastore.SleepyBinding;
import net.verza.jdict.exceptions.KeyNotFoundException;

/**
 * @author ChristianVerdelli
 * 
 */
public class StringIndexKeyCreator extends IndexKeyCreator {

	SleepyDatabase db;
	private static Logger log;
	private Class<?> Class2Load;
	private Constructor<?> constr;
	private SearchableObject obj;
	private String indexName,methodName;

	
	public StringIndexKeyCreator(String _languageName, 
															String _className, 
															String _index_name) {
		
		
		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore.indexes");
		log.trace("called class " + this.getClass().getName()+
						" with arguments: language name "+_languageName+
						"class name "+_className+
						"index name "+_index_name);
		
		//this.languageName = _languageName;
		this.indexName = _index_name;
		// the setter method must always be the attribute name prefixed with the string set
		this.methodName = "get" + _index_name;
		
		try {
			Class2Load = Class.forName(_className);
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

	
	
	
	public boolean createSecondaryKey(SecondaryDatabase secondary,
																	DatabaseEntry key, 
																	DatabaseEntry data, 
																	DatabaseEntry result) {
		try {
			
			log.trace("called createSecondaryKey with data "+data.toString());
			obj = (SearchableObject)SleepyBinding.getDataBinding().entryToObject(data);
			
			Class<?>[] methodTypes = new Class[] { };
			Method Method2Call;
			
			Method2Call = Class2Load.getMethod(this.methodName, methodTypes);
			Object value = (Object) Method2Call.invoke(obj, (Object[])null);
			
			if (value == null) {
				log.warn("can't not set up index" + indexName
						+ " the entry");
			
			} else {
				
				String tmp = (String)value;
				result.setData( tmp.getBytes("UTF8") );

				log.debug("setting " + indexName
							+ " index for the entry with value "+tmp 
							+ " using method " +this.methodName);

			}
			
			
			
		}  catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (KeyNotFoundException e) {
			e.printStackTrace();
		} 
		
		return true;
	}	

	
	
	
}
