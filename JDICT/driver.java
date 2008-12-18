
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import jxl.read.biff.BiffException;
import net.verza.jdict.Configuration;
import net.verza.jdict.sleepycat.SleepyConnector;
import net.verza.jdict.UserProfile;
import net.verza.jdict.Word;
import net.verza.jdict.Dictionary;
import net.verza.jdict.IWord;
import net.verza.jdict.PropertiesLoader;
import net.verza.jdict.exceptions.*;
import net.verza.jdict.quiz.Italian2ArabicQuiz;
import net.verza.jdict.quiz.QuizResult;
import net.verza.jdict.sleepycat.datastore.SleepyWordsDatabase;
import net.verza.jdict.verbs.ArabVerbFormInterface;
import org.apache.log4j.*;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import java.io.FileNotFoundException;

/**
 * @author Christian Verdelli
 * 
 */

public class driver {

	private static final int EXIT_FAILURE = 1;
	private static Logger log;
	private static Dictionary dit;

	public static void usage() {
		System.out
				.println("usage: java "
						+ "driver "
						+ "quiz <italiano2arabo|arabo2italiano|audio2arabo>  iterations  user |   "
						+ "dizionario <insert|retrieve|searchCategory|searchSection|print|load|loadCategory|loadSection> Chiave [valore1] [valore2]");
		System.exit(EXIT_FAILURE);
	}

	public static void main(String argv[]) {
		// TODO Auto-generated method stub
		if (argv.length < 1) {
			usage();
			return;
		}

		PropertyConfigurator.configure(Configuration.LOG4JCONF);
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("Starting...");

		PropertiesLoader pl = new PropertiesLoader();
		String mode = argv[0];

		try {
			dit = new SleepyConnector();

			if (mode.equalsIgnoreCase("dizionario")) {

				String action = argv[1];

				boolean doSearch_Italian = false;
				boolean doSearch_Arabic_Singular = false;
				boolean doSearch_Arabic_Plural = false;
				boolean doSearchCategory = false;
				boolean doSearchSection = false;
				boolean doPrint = false;
				boolean doLoad = false;
				boolean doCategoryLoad = false;
				boolean doSectionLoad = false;
				boolean doUserLoad = false;
				boolean doUserGet = false;

				if (action.equalsIgnoreCase("search_Italian")) {
					doSearch_Italian = true;
				} else if (action.equalsIgnoreCase("search_Arabic_Sing")) {
					doSearch_Arabic_Singular = true;
				} else if (action.equalsIgnoreCase("search_Arabic_Plur")) {
					doSearch_Arabic_Plural = true;
				} else if (action.equalsIgnoreCase("search_Category")) {
					doSearchCategory = true;
				} else if (action.equalsIgnoreCase("search_Section")) {
					doSearchSection = true;
				} else if (action.equalsIgnoreCase("print")) {
					doPrint = true;
				} else if (action.equalsIgnoreCase("load")) {
					doLoad = true;
				} else if (action.equalsIgnoreCase("loadCategory")) {
					doCategoryLoad = true;
				} else if (action.equalsIgnoreCase("loadSection")) {
					doSectionLoad = true;
				} else if (action.equalsIgnoreCase("loaduser")) {
					doUserLoad = true;
				} else if (action.equalsIgnoreCase("getuser")) {
					doUserGet = true;
				} else {
					usage();
				}

				if (doSearch_Italian) {
					log.info("Query Mode");
					BufferedReader br = new BufferedReader(
							new InputStreamReader(System.in));
					try {
						System.out.print("Enter the word to look for: ");
						dit.setSearchKey(SleepyWordsDatabase
								.getIT_sing_IndexDatabase(), new DatabaseEntry(
								br.readLine().getBytes("UTF-8")));
						dit.read();
						IWord key = dit.getKey(0);
						Vector<Word> data = dit.getData();
						System.out.println("KEY-" + key.toString());
						System.out.println("found data of size " + data.size());
						for (int i = 0; i < data.size(); i++) {
							IWord w = (IWord) data.get(i);
							System.out.println(w.getsingular());
						}
					} catch (IOException ioe) {
						log.error("IO error trying to read the answer!");
						System.exit(1);
					}

				} else if (doSearch_Arabic_Singular) {
					log.info("Query Mode");
					BufferedReader br = new BufferedReader(
							new InputStreamReader(System.in));
					try {
						System.out.print("Enter the word to look for: ");
						dit.setSearchKey(SleepyWordsDatabase
								.getAR_sing_IndexDatabase(), new DatabaseEntry(
								br.readLine().getBytes("UTF-8")));
						dit.read();
						IWord key = dit.getKey(0);
						Vector<Word> data = dit.getData();
						System.out.println("KEY-" + key.toString());
						System.out.println("found data of size " + data.size());
						for (int i = 0; i < data.size(); i++) {
							IWord w = (IWord) data.get(i);
							System.out.println(w.getsingular());
						}
					} catch (IOException ioe) {
						log.error("IO error trying to read the answer!");
						System.exit(1);
					}

				} else if (doSearch_Arabic_Plural) {
					log.info("Query Mode");
					BufferedReader br = new BufferedReader(
							new InputStreamReader(System.in));
					try {
						System.out.print("Enter the word to look for: ");
						dit.setSearchKey(SleepyWordsDatabase
								.getAR_plur_IndexDatabase(), new DatabaseEntry(
								br.readLine().getBytes("UTF-8")));
						dit.read();
						IWord key = dit.getKey(0);
						Vector<Word> data = dit.getData();
						System.out.println("KEY-" + key.toString());
						System.out.println("found data of size " + data.size());
						for (int i = 0; i < data.size(); i++) {
							IWord w = (IWord) data.get(i);
							System.out.println(w.getsingular());
						}
					} catch (IOException ioe) {
						log.error("IO error trying to read the answer!");
						System.exit(1);
					}

				} else if (doSearchCategory) {
					log.info("Category Query Mode");
					BufferedReader br = new BufferedReader(
							new InputStreamReader(System.in));
					try {

						System.out.println("category cursor: ");
						DatabaseEntry categ_entry = new DatabaseEntry();
						StringBinding.stringToEntry(br.readLine(), categ_entry);
						dit.setSearchKey(SleepyWordsDatabase
								.getCategory_IndexDatabase(), categ_entry);
						dit.read();
					} catch (IOException ioe) {
						log.error("IO error trying to read the answer!");
						System.exit(1);
					}

				} else if (doSearchSection) {
					log.info("Section Query Mode");
					BufferedReader br = new BufferedReader(
							new InputStreamReader(System.in));
					try {
						System.out.println("category cursor: ");
						DatabaseEntry categ_entry = new DatabaseEntry();
						StringBinding.stringToEntry(br.readLine(), categ_entry);
						dit.setSearchKey(SleepyWordsDatabase
								.getSection_IndexDatabase(), categ_entry);
						dit.read();
					} catch (IOException ioe) {
						log.error("IO error trying to read the answer!");
						System.exit(1);
					}

				} else if (doPrint) { // Printing Database
					log.info("Print Mode");
					dit.printDatabase();

				} else if (doLoad) { // Bulk Loading of DB
					log.info("Import Mode");
					dit.loadDatabase(argv[2]);

				} else if (doCategoryLoad) { // Bulk Loading of DB
					System.out.println("Category Import Mode");
					log.info("Category Import Mode");
					dit.loadCategoryDatabase(argv[2]);

				} else if (doSectionLoad) { // Bulk Loading of DB
					System.out.println("Section Import Mode");
					log.info("Section Import Mode");
					dit.loadSectionDatabase(argv[2]);

				} else if (doUserLoad) { // Bulk Loading of DB
					log.info("User Import Mode");
					String username = null;
					String password = null;
					try {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(System.in));
						System.out.print("Enter the User Name to insert: ");
						username = br.readLine();
						System.out
								.print("Enter the User's new password to insert: ");
						password = br.readLine();
					} catch (IOException ioe) {
						log.error("IO error trying to read the answer!");
						System.exit(1);
					}
					UserProfile up = new UserProfile(username, password);
					dit.writeUserDatabase(up);
				} else if (doUserGet) { // Bulk Loading of DB
					log.info("User Get Mode");
					String username = null;
					try {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(System.in));
						System.out.print("Enter the User Name to search: ");
						username = br.readLine();
					} catch (IOException ioe) {
						log.error("IO error trying to read the answer!");
						System.exit(1);
					}
					UserProfile up = dit.readUsersDatabase(username);
					System.out.println(up.toString());
					System.out.println("Size " + up.getQuizStat().size());
					for (int i = 0; i < up.getQuizStat().size(); i++) {
						QuizResult q = up.getQuizStat(i);
						System.out.println(q.toString());
					}
				}
			}

			else if (argv[0].equalsIgnoreCase("verbs")) {
				log.info("Verb Mode");

				// example with paradigm 9
				//char[] root = { 'آ', 'ر', 'س', 'ل' };
				// example with paradigm 8
				// char[] root = {'ب', 'ا', 'د', 'ل'};
				// exaple with paradigm 15
				// char[] root = {'س' , 'ت' , 'ر' , 'ش' , 'د'};
				// exampple with paradigm 14
				// char[] root = {'ح' , 'م' , 'ر' };
				// example with paradigm 20
				// char[] root = {'ح' , 'ب' };
				// example with paradigm 21
				// char[] root = {'ت' , 'ق' , 'ا' , 'ل'};
				// example with paradigm 13/4
				String tmp = new String("إتّفق");
				System.out.println("tmp String size "+tmp.length()  );
				System.out.println("tmp toarraychar size "+tmp.toCharArray().length  );
		//		char[] root =  {'إ' , 'ت' , 'ف' , 'ق' };
	//			System.out.println("char size "+root.length  );
				char[] root = tmp.toCharArray();
				
				log.debug("c");
				// ArabVerb av = new ArabVerb(root,"9");
				/*
				 * Present pr = new Present(root,"9"); Past ps = new
				 * Past(root,"9"); System.out.println("PRES
				 * -"+pr.get_I_P_SING()); System.out.println("PAST
				 * -"+pr.get_I_P_SING());
				 */
				try {
					String con = "net.verza.jdict.verbs.ArabVerbPast";
					Class toRun = Class.forName(con);
					Class[] c_arr = new Class[] { char[].class, String.class };
					Constructor constr = toRun.getConstructor(c_arr);
					ArabVerbFormInterface obj = (ArabVerbFormInterface) constr
							.newInstance(root, "13/4");
					System.out.println("PAST -" + new String( obj.get_I_P_PLUR().getBytes("UTF-8"))  );
				} catch (ClassNotFoundException e) {
					System.out.println(e);
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					System.out.println(e);
					e.printStackTrace();
				} catch (InstantiationException e) {
					System.out.println(e);
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					System.out.println(e);
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					System.out.println(e);
					e.printStackTrace();
				}

			} else if (argv[0].equalsIgnoreCase("newquiz")) {

				String username = new String(argv[2]);
				int iteractions = Integer.parseInt(argv[1]);

				Italian2ArabicQuiz itquiz = new Italian2ArabicQuiz();
				itquiz.setSecondaryDatbase(SleepyWordsDatabase
						.getAR_sing_IndexDatabase());
				itquiz.setIterations(iteractions);
				itquiz.load();

				for (int i = 0; i < iteractions; i++) {
					System.out.println("question n°: " + i + "  -- "
							+ itquiz.getQuestion(i));
					BufferedReader br = new BufferedReader(
							new InputStreamReader(System.in));
					System.out.print("Enter user answer: ");
					itquiz.userAnswer(i, br.readLine());
				}

				// Shows Statistics
				Vector<QuizResult> st = itquiz.getStats();
				System.out.println("Value number " + st.size());
				UserProfile up = dit.readUsersDatabase(username);
				up.addQuizStat(st);
				log.debug("updating profile of user " + up.toString());
				dit.writeUserDatabase(up);
				for (int i = 0; i < st.size(); i++) {

					System.out.println("------ Detailed Statistics  ------");
					QuizResult qtObj = (QuizResult) st.get(i);
					dit.setSearchKey(SleepyWordsDatabase.getID_IndexDatabase(),
							new DatabaseEntry(qtObj.getWord_ID().getBytes(
									"UTF-8"))); // ID lookup
					dit.read();
					IWord key = dit.getKey(0);
					Vector<Word> data = dit.getData();

					System.out.println("Quiz Type " + qtObj.getQuizType());
					System.out.println("Quiz to String " + qtObj.toString());

					if (qtObj.getQuizType()
							.equals(Configuration.ITALIAN2ARABIC)) { // ITA2ARAB
						System.out.println("Parola da indovinare "
								+ key.getsingular());
						System.out.println("Risposta data "
								+ qtObj.getUserAnswer());
						IWord w = (IWord) data.get(0);
						System.out
								.println("Risposta esatta " + w.getsingular());
					}
					if (qtObj.getQuizType()
							.equals(Configuration.ARABIC2ITALIAN)) { // ARAB2ITA
						IWord w = (IWord) data.get(0);
						System.out.println("Parola da indovinare "
								+ w.getsingular());
						System.out.println("Risposta data "
								+ qtObj.getUserAnswer());
						System.out.println("Risposta esatta "
								+ key.getsingular());
					}

				}

			}

			else {
				usage();
			}

			log.info("Successfull execution: exiting...");
			System.exit(Configuration.EXIT_SUCCESS);

		} catch (DatabaseException e) {
			System.err.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (LabelNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (BiffException e) {
			System.err.println(e.getMessage());
		} catch (DynamicCursorException e) {
			System.err.println(e.getMessage());
		} catch (DataNotFoundException e) {
			System.err.println(e.getMessage());
		}

	}

}
