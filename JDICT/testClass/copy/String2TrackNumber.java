package testClass.copy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.util.Enumeration;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;


public class String2TrackNumber {
	

	private static final String log4j_filename = "/Users/ChristianVerdelli/documents/workspace/jdict/conf/log4j.properties";
	private static final String extension = "mp3";
	private static final String separator = "-";
	private static String file_words;
	private static String[] words = new String[1000];
	private static String file_tracks_directory;
	private static String line = null;
	private static Hashtable<Integer, String> map;
	private static Logger log;

	public static void main(String argv[]) {

		
		//System.setProperty("file.encoding", "UTF-8");
		
		System.out.println("Charset.defaultCharset().name() "+ Charset.defaultCharset().name());
		System.out.println("defaultEncodingName "+System.getProperty( "file.encoding" ) );
		
		
		PropertyConfigurator.configure(log4j_filename);
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class String2TrackNumber");

		if (argv.length < 2) {
			System.err.println("invalid arguments\n");
			usage();
			log.error("invalid arguments!!");
			return;
		}

		file_words = argv[0];
		file_tracks_directory = argv[1];

		getFilebyNumber(file_tracks_directory);
		openWordsFile(file_words);
		renameFiles();

	}

	static void getFilebyNumber(String file_tracks_directory) {
		log.debug("listing file inside directory " + file_tracks_directory);
		File[] files;
		map = new Hashtable<Integer, String>();

		try {
			File dir = new File(file_tracks_directory);
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return !name.startsWith(".");
				}
			};

			files = dir.listFiles(filter);
			if (files == null) { // dir does not exist or is not a directory
				log.fatal("empty or inexisting directory");
			} else {
				for (int i = 0; i < files.length; i++) {
					int dotposition = files[i].getName().lastIndexOf(".");
					String noext = files[i].getName().substring(0, dotposition);
					log.debug("filename without extension " + noext);
					String[] numberonly = noext.split(separator);
					log
							.debug("number extracted from filename "
									+ numberonly[1]);
					map.put(new Integer(numberonly[1]), noext);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void openWordsFile(String file_words) {
		log.debug("printing content of file " + file_words);
		BufferedReader br;

		int count = 0;
		try {
			br = new BufferedReader(new FileReader(file_words));
			while ((line = br.readLine()) != null) {
				words[++count] = line;
				log.debug("line " + count + "-  word " + words[count]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void renameFiles() {
		Enumeration<Integer> names;
		Integer str;
		names = map.keys();
		try	{
			while (names.hasMoreElements()) {
				str = names.nextElement();
				log.debug("key: " + str + " <---> value: " + map.get(str));
				

				File newfilename = new File(new String(file_tracks_directory 
											+ words[str]
											+ "." +extension));
				
				log.debug("file "+map.get(str) +"." +extension+
						" will be renamed with filename " 
						+ newfilename.getCanonicalPath());
				
				
				File oldfilename = new File( file_tracks_directory 
											+"/"+ map.get(str) 
											+ "." +extension );
				oldfilename.renameTo(newfilename);
			}
		}catch (IOException e)	{
			e.printStackTrace();
			log.error("IOException "+e.getMessage());
		}
		
		
	}

	public static void usage() {
		System.out.println("usage: java " + "String2TrackNumber "
				+ "words_file " + "exported_mp3_directory ");
		System.exit(-1);
	}

}
