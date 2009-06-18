package net.verza.jdict.sleepycat.datastore;




import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DatabaseEntry;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import net.verza.jdict.Configuration;
import net.verza.jdict.SearchableObject;
import org.apache.log4j.PropertyConfigurator;
import net.verza.jdict.properties.LanguagesConfiguration;
import net.verza.jdict.properties.PropertiesLoader;



public class DatabaseReaderTestClass {


	private static SleepyFactory factory;
	private static SleepyDatabaseReader reader;
	private static SleepyLinkidResolver linkidResolver;
	private static String key;
	private static String lang;
	private static Vector<SearchableObject>  wkey = new Vector<SearchableObject>();

	
	public static void main(String[] args) {
		try {

			PropertyConfigurator.configure(Configuration.LOG4JCONF);

			new PropertiesLoader();			
			new LanguagesConfiguration(); 
			new SleepyBinding();
			factory = SleepyFactory.getInstance();
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(System.in));

		        System.out.print("Enter the languge to lookup for: ");
		        lang = br.readLine() ;
		        if("".equals(lang)) read();
		        
			    System.out.print("Enter the data to look for: ");
			     key = br.readLine() ;
		        if("".equals(key)) read();
		        
		        else	{
			        System.out.print("Enter the index name to be used with the query (if null primary key is assumed) : ");
				    String index = br.readLine() ;
				    if("".equals(index)) read();
				    else  read(lang,key,index);
		        }	
		        
		        wkey = reader.getData();

		        
		        linkidResolver = new SleepyLinkidResolver(reader);
		        linkidResolver.setSearchableObject(wkey);
		        linkidResolver.iterateLinkid();
		        linkidResolver.toString();
    

			    
			}catch(Exception e)	{
				e.printStackTrace();
			}	

		
	}
	

	
	private static void read()	{
		
			try {
				reader = new SleepyDatabaseReader(factory.getDatabase(lang));
				reader.setDataBinding(SleepyBinding.getDataBinding());
				if(!"".equals(key))
					reader.setKey(key);
				
				System.out.println("database size "+reader.getSize());
				
				int count = reader.read();
				for (int i=0;  i < count; i++) {

					System.out.println("----------------------------------");
					System.out.println("Data " + reader.getData(i).toString());
					System.out.println("----------------------------------");
				}	
				
			} catch(DatabaseException e) {
					e.printStackTrace();
			} catch(DataNotFoundException e) {
					System.out.println("parola non trovata");
			} catch(DynamicCursorException e) {
					e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
					e.printStackTrace();
			} catch(KeyNotFoundException e) {
					e.printStackTrace();
			}

	}

	
	
	private static void read(String _language, String _key, String _index)	{
			System.out.println("reading using language "+ _language
									+ " key "+_key 
									+ " index "+ _index );
			try{
				
					DatabaseEntry theKey = null;
				 	if(_index.equals("section") ||
				 		_index.equals("category"))	{
				 		theKey = new DatabaseEntry();
				 		StringBinding.stringToEntry(_key,theKey);
					}else {
						theKey = new DatabaseEntry(_key.getBytes("UTF-8"));
					}
				 	System.out.println("KEY  "+ new String(theKey.getData(),"UTF-8")  );
					
					SleepyDatabase db = factory.getDatabase(_language);

					reader = new SleepyDatabaseReader(factory.getDatabase(_language));
					reader.setDataBinding(SleepyBinding.getDataBinding());
					reader.setSecondaryCursor(db.getIndex(_index), theKey );

					
					int count = reader.read	();
					for (int i=0;  i < count; i++) {
						System.out.println("----------------------------------");
						System.out.println("Data " + reader.getData(i).toString());
						System.out.println("----------------------------------");
					}

			        
			} catch(DatabaseException e) {
					e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
					e.printStackTrace();
			} catch(DataNotFoundException e) {
				System.out.println("parola non trovata");
			} catch(DynamicCursorException e) {
					e.printStackTrace();
			} catch(KeyNotFoundException e) {
					e.printStackTrace();
			}
			
			
		        
	}
			


	
}
