
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import com.sleepycat.bind.tuple.StringBinding;
import net.verza.jdict.sleepycat.datastore.SleepyWordsDatabaseReader;
import net.verza.jdict.sleepycat.datastore.SleepyWordsDatabase;
import net.verza.jdict.exceptions.*;
import org.apache.log4j.*;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;

/**
 * @author Christian Verdelli
 * 
 */
public class DRIVER_SleepyDynamicReader {

	private static Logger log;

	
	public static void run() {
		
		SleepyWordsDatabase db;
		SleepyWordsDatabaseReader dr;
		
		try {
			
			db = new SleepyWordsDatabase();
			dr = new SleepyWordsDatabaseReader(db);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			
			/*
			 System.out.println("italian word: ");
			 dr.setSecondaryCursor(SleepyWordsDatabase.getIT_sing_IndexDatabase(),
					 				new DatabaseEntry(br.readLine().getBytes("UTF-8")));
			*/
			
			 
			 /*
			 System.out.println("arab word: ");
			 dr.setSecondaryCursor(SleepyWordsDatabase.getAR_sing_IndexDatabase(),
					 				new DatabaseEntry(br.readLine().getBytes("UTF-8")));
			 */
			 
			
			 System.out.println("section cursor: "); 
			 DatabaseEntry sect_entry = new DatabaseEntry();
			 StringBinding.stringToEntry(br.readLine(),sect_entry);
			 dr.setSecondaryCursor(SleepyWordsDatabase.getSection_IndexDatabase(),
					 				sect_entry);
			 
			 
			 System.out.println("category cursor: "); 
			 DatabaseEntry categ_entry = new DatabaseEntry();
			 StringBinding.stringToEntry(br.readLine(),categ_entry);
			 dr.setSecondaryCursor(SleepyWordsDatabase.getCategory_IndexDatabase(),
					 				categ_entry);
			 

			int count = dr.getSize();
			System.out.println("dr.getSize(): " + count);
			System.out.println("dr.read(): " + dr.read());
			for (int i = 0; i < count; i++) {
				System.out.println("----------------------------------");
				System.out.println("ItalianWord " + dr.getKey(i).getsingular());
				System.out.println("ArabWord " + dr.getData(i).getsingular());
				System.out.println("----------------------------------");
			}

			
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DataNotFoundException e) {
			e.printStackTrace();
		} catch (DynamicCursorException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	public static void main(String argv[]) {
		PropertyConfigurator.configure("../conf/log4j.properties");
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("Starting...");

		run();
	}

}
