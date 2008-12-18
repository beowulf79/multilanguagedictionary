package testClass.copy.verbs;



import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DatabaseEntry;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.DataNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.util.Vector;
import java.util.HashMap;
import net.verza.jdict.Configuration;
import net.verza.jdict.PropertiesLoader;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import jxl.read.biff.BiffException;
import net.verza.jdict.exceptions.LabelNotFoundException;


public class VerbsDatabaseDriver {

	private static Logger log;



	private static SleepyVerbsDatabaseWriter writer;
	private static SleepyVerbsFactory factory;
	private static SleepyVerbsDatabaseReader reader;
	private static SleepyVerbsDatabaseLoader loader;
	private static SleepyLinkidResolver linkidResolver;
	private static String key;
	private static String lang;
	private static Vector<SearchableObject>  wkey = new Vector<SearchableObject>();
	private static Vector<SearchableObject> wdata = new Vector<SearchableObject>();
	private static HashMap<String,Vector<SearchableObject>> resultsMap = new HashMap<String,Vector<SearchableObject>>();

	
	public static void main(String[] args) {
		try {

			PropertyConfigurator.configure(Configuration.LOG4JCONF);

			log = Logger.getLogger("testClass.copy.verbs");
	
			new PropertiesLoader();			
			new LanguagesConfiguration(); 

			
			new SleepyBinding();
			factory = new SleepyVerbsFactory();
			

			//load();
			
			
			
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

		        
		        linkidResolver = new SleepyLinkidResolver();
		        linkidResolver.setVerb(wkey);
		        linkidResolver.iterateLinkid();
		        linkidResolver.toString();
    

			    
			}catch(Exception e)	{
				e.printStackTrace();
			}	

		
	}
	
	
	private static void load()	{
		try  {
			
		
		 	loader = new SleepyVerbsDatabaseLoader();
			loader.setFileName("/Users/christianverdelli/documents/workspace/jdict/misc/utilizzare_per_test.xls");
			loader.loadDatabases();
		
			} catch (BiffException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}catch (LabelNotFoundException e) {
				e.printStackTrace();
			} catch (KeyNotFoundException e) {
				e.printStackTrace();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			
	}
	
		
	private static void write()	{
		try  {
			
			

			Word ita_word = new Word();
			ita_word.setid("5");
			ita_word.setlinkid("english", "0#1");
			ita_word.setlinkid("arabic", "0");
			ita_word.setsingular("volo");
			ita_word.setnotes("nome italiano");
			ita_word.setsection("deltaplano#aero");
			ita_word.setaudio("audiotest".getBytes("UTF8"));
			ita_word.setcategory("pino#gino");
			
			Verb ita_verb = new Verb();
			
			ita_verb.setid("0");
			ita_verb.setlinkid("english", "0#1");
			ita_verb.setlinkid("arabic", "0");
			ita_verb.setinfinitive("volare");
			ita_verb.setnotes("verbo italiano");
			ita_verb.setsection("deltaplano#aero");
			ita_verb.setaudio("audiotest".getBytes("UTF8"));
			

			Verb ita_verb2 = new Verb();
			
			ita_verb2.setid("1");
			ita_verb2.setlinkid("english", "1#2");
			ita_verb2.setlinkid("arabic", "0");
			ita_verb2.setinfinitive("parlare");
			ita_verb2.setnotes("verbo italiano");
			ita_verb2.setsection("arabo contemporaneo#prova");
			ita_verb2.setaudio("audiotest".getBytes("UTF8"));
			
			
			log.trace("opening italian database");
			writer = new SleepyVerbsDatabaseWriter(factory.getDatabase("italian words"));
			writer.setDataBinding(SleepyBinding.getDataBinding());
			
			System.out.println("write key "+ita_word.getid()+" with data "+ita_word.toString());
			String id3 = new String(ita_word.getid().toString());
			writer.write(id3, ita_word);
			
			
			
			// scrive Verb ONE
			System.out.println("write key "+ita_verb.getid()+" with data "+ita_verb.toString());
			// tramite la chiamata alla classe helper SleepyBinding_NEW 
			// ottengo l'oggetto EntryBinding associato al linguaggio
			writer.setDatabase(factory.getDatabase("italian verbs"));
			String id = new String(ita_verb.getid().toString());
			writer.write(id, ita_verb);
			
			System.out.println("write key "+ita_verb2.getid()+" with data "+ita_verb2.toString());
			String id2 = new String(ita_verb2.getid().toString());
			writer.write(id2, ita_verb2);
			

	

			log.trace("opening english database");
			Verb eng_verb = new Verb();
			eng_verb.setid("0");
			eng_verb.setlinkid("italian", "0");
			eng_verb.setlinkid("arabic", "0");
			eng_verb.setinfinitive("fly");
			eng_verb.setnotes("english verb");
			eng_verb.setsection("jumbo#airplane");
			eng_verb.setaudio("audiotest".getBytes("UTF8"));

			
			Verb eng_verb2 = new Verb();
			eng_verb2.setid("1");
			eng_verb2.setlinkid("italian", "1");
			eng_verb2.setlinkid("arabic", "0");
			eng_verb2.setinfinitive("talk");
			eng_verb2.setnotes("english verb");
			eng_verb2.setsection("speak#learn english");
			eng_verb2.setaudio("audiotest".getBytes("UTF8"));
			
			Verb eng_verb3 = new Verb();
			eng_verb3.setid("2");
			eng_verb3.setlinkid("italian", "1#0");
			eng_verb3.setlinkid("arabic", "0");
			eng_verb3.setinfinitive("fish");
			eng_verb3.setnotes("english verb");
			eng_verb3.setsection("how to fish#fisherman friends");
			eng_verb3.setaudio("audiotest".getBytes("UTF8"));

			
			writer.setDatabase(factory.getDatabase("english"));
			
			// tramite la chiamata alla classe helper SleepyBinding_NEW 
			// ottengo l'oggetto EntryBinding associato al linguaggio
			writer.setDataBinding(SleepyBinding.getDataBinding());
			id = new String(eng_verb.getid().toString());
			System.out.println("write key "+eng_verb.getid()+" with data "+eng_verb.toString());
			writer.write(id, eng_verb);
			id = new String(eng_verb2.getid().toString());
			System.out.println("write key "+eng_verb2.getid()+" with data "+eng_verb2.toString());
			writer.write(id, eng_verb2);
			id = new String(eng_verb3.getid().toString());
			System.out.println("write key "+eng_verb3.getid()+" with data "+eng_verb3.toString());
			writer.write(id, eng_verb3);

			
			log.trace("opening arabic database");
			ArabVerb ara_verb = new ArabVerb();
			//IVerb ara_verb = new Verb();
			ara_verb.setid("0");
			ara_verb.setinfinitive("يطير");
			ara_verb.setlinkid("english", "0#1#2");
			ara_verb.setlinkid("italian", "0");
			ara_verb.setnotes("verbo arabo");
			ara_verb.setsection("badile#vanga");
			ara_verb.setaudio("audiotest".getBytes("UTF8"));
			// This method is just for the ArabVerb
			ara_verb.setpast("past");
			
			writer.setDatabase(factory.getDatabase("arabic"));
			System.out.println("write key "+ara_verb.getid()+" with data "+ara_verb.toString());
			// tramite la chiamata alla classe helper SleepyBinding_NEW 
			// ottengo l'oggetto EntryBinding associato al linguaggio
			writer.setDataBinding(SleepyBinding.getDataBinding());
			id = new String(ara_verb.getid().toString());
			writer.write(id, ara_verb);

	

			
		}catch (DatabaseException e) {
			e.printStackTrace();
		}catch (KeyNotFoundException e) {
			System.out.println("parola non trovata");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	

	
	private static void read()	{
		
		
			try {
				reader = new SleepyVerbsDatabaseReader(factory.getDatabase(lang));
				reader.setDataBinding(SleepyBinding.getDataBinding());
				if(!"".equals(key))
					reader.setKey(key);
				
				System.out.println("DB SIZE "+reader.getSize());
				
				
				int count = reader.read	();
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
			} catch(KeyNotFoundException e) {
					System.out.println("parola non trovata");
			} catch(UnsupportedEncodingException e) {
					e.printStackTrace();
			}

	}

	
	
	private static void read(String _language, String _key, String _index)	{
			System.out.println("reading using language "+ _language
									+ " key "+_key 
									+ " index "+ _index );
			try{
				
					DatabaseEntry theKey = null;
				 	if(_index.equals("section"))	{
				 		theKey = new DatabaseEntry();
				 		StringBinding.stringToEntry(_key,theKey);
					}else {
						theKey = new DatabaseEntry(_key.getBytes("UTF-8"));
					}
				 	System.out.println("KEY  "+ new String(theKey.getData(),"UTF-8")  );
					
					//DatabaseEntry theKey = new DatabaseEntry(Utility.intToByteArray( Integer.parseInt(key) ) );
					DatabaseEntry theData = new DatabaseEntry();
			        SleepyVerbsDatabase db = factory.getDatabase(_language);
			        

					reader = new SleepyVerbsDatabaseReader(factory.getDatabase(_language));
					reader.setDataBinding(SleepyBinding.getDataBinding());
					reader.setSecondaryCursor(db.getIndex(_index), 
																		theKey );

					
					int count = reader.read	();
					for (int i=0;  i < count; i++) {
						System.out.println("----------------------------------");
						System.out.println("Data " + reader.getData(i).toString());
						System.out.println("----------------------------------");
					}

			        
			} catch(DatabaseException e) {
					e.printStackTrace();
			} catch(KeyNotFoundException e) {
					e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
					e.printStackTrace();
			} catch(DataNotFoundException e) {
				System.out.println("parola non trovata");
			} catch(DynamicCursorException e) {
					e.printStackTrace();
			} 
			
			
		        
	}
			


	
}
