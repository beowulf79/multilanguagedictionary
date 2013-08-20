package net.verza.jdict.dictionary.sleepycat;

import java.io.UnsupportedEncodingException;

import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.model.ArabVerb;
import net.verza.jdict.model.Verb;
import net.verza.jdict.model.Word;
import net.verza.jdict.properties.Configuration;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguagesConfiguration;
import net.verza.jdict.properties.PropertiesLoader;

import org.apache.log4j.PropertyConfigurator;

import com.sleepycat.je.DatabaseException;

public class DatabaseWriterTestClass {

    private static SleepyDatabaseWriter writer;
    private static SleepyFactory factory;

    public static void main(String[] args) {
	try {

	    PropertyConfigurator.configure(Configuration.LOG4JCONF);

	    new PropertiesLoader();
	    new LanguagesConfiguration();
	    new SleepyBinding();

	    factory = SleepyFactory.getInstance();

	    write();

	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private static void write() {
	try {

	    writer = new SleepyDatabaseWriter();
	    // writer.setDataBinding(SleepyBinding.getDataBinding());
	    LanguageConfigurationClassDescriptor langconf;

	    // ############### ITALIAN WORD WRITE TEST
	    // #################
	    // get the configuration for the italianword language;
	    // if enabled write an italianword on the db
	    langconf = LanguagesConfiguration
		    .getLanguageMainConfigNode("italianword");
	    if (langconf.isEnabled()) {
		Word ita_word = new Word();
		ita_word.setid(0);
		ita_word.setlinkid("english", "0#1");
		ita_word.setlinkid("arabic", "0");
		ita_word.setsingular("felice");
		ita_word.setnotes("stato d'animo");
		ita_word.setsection("9");
		ita_word.setaudiosingular("audiotest".getBytes("UTF8"));
		ita_word.setcategory("1#2");

		Word ita_word2 = new Word();
		ita_word2.setid(1);
		ita_word2.setlinkid("english", "1");
		ita_word2.setlinkid("arabic", "1");
		ita_word2.setsingular("fuoco");
		ita_word2.setnotes("usato per scaldare");
		ita_word2.setsection("5#6");
		ita_word2.setaudiosingular("audiotest".getBytes("UTF8"));
		ita_word2.setcategory("2#3");

		System.out.println("getting italian word database");
		writer.setDatabase(factory.getDatabase("italianword"));
		System.out.println("write key " + ita_word.getid()
			+ " with data " + ita_word.toString());
		String id = new String(ita_word.getid().toString());
		writer.write(id, ita_word);

		System.out.println("write key " + ita_word.getid()
			+ " with data " + ita_word.toString());
		id = new String(ita_word2.getid().toString());
		writer.write(id, ita_word2);

	    }

	    // ############### ITALIAN VERB WRITE TEST
	    // #################
	    // get the configuration for the italianverb language;
	    // if enabled write an italianverb on the db
	    langconf = LanguagesConfiguration
		    .getLanguageMainConfigNode("italianverb");
	    if (langconf.isEnabled()) {
		Verb ita_verb = new Verb();
		ita_verb.setid(0);
		ita_verb.setlinkid("english", "0");
		ita_verb.setlinkid("arabic", "0");
		ita_verb.setinfinitive("volare");
		ita_verb.setnotes("verbo italiano");
		ita_verb.setsection("1#2");
		ita_verb.setaudioinfinitive("audiotest".getBytes("UTF8"));

		Verb ita_verb2 = new Verb();

		ita_verb2.setid(1);
		ita_verb2.setlinkid("english", "1#2");
		ita_verb2.setlinkid("arabic", "0");
		ita_verb2.setinfinitive("parlare");
		ita_verb2.setnotes("verbo italiano");
		ita_verb2.setsection("2#3");
		ita_verb2.setaudioinfinitive("audiotest".getBytes("UTF8"));

		System.out.println("getting italian verb database");
		writer.setDatabase(factory.getDatabase("italianverb"));

		// scrive Verb 1
		System.out.println("write key " + ita_verb.getid()
			+ " with data " + ita_verb.toString());
		String id = new String(ita_verb.getid().toString());
		writer.write(id, ita_verb);

		// scrive Verb 2
		System.out.println("write key " + ita_verb2.getid()
			+ " with data " + ita_verb2.toString());
		id = new String(ita_verb2.getid().toString());
		writer.write(id, ita_verb2);

	    }

	    // ############### ENGLISH WORD WRITE TEST
	    // #################
	    // get the configuration for the englishword language;
	    // if enabled write an englishword on the db
	    langconf = LanguagesConfiguration
		    .getLanguageMainConfigNode("italianverb");
	    if (langconf.isEnabled()) {

		Word eng_word = new Word();
		eng_word.setid(0);
		eng_word.setlinkid("italian", "0");
		eng_word.setlinkid("arabic", "0");
		eng_word.setsingular("happy");
		eng_word.setnotes("english word");
		eng_word.setsection("1#2");
		eng_word.setaudiosingular("audiotest".getBytes("UTF8"));
		eng_word.setcategory("2#3");

		Word eng_word2 = new Word();
		eng_word2.setid(1);
		eng_word2.setlinkid("italian", "1");
		eng_word2.setlinkid("arabic", "1");
		eng_word2.setsingular("fire");
		eng_word2.setnotes("english word");
		eng_word.setsection("1#2");
		eng_word2.setaudiosingular("audiotest".getBytes("UTF8"));
		eng_word2.setcategory("2#3");

		System.out.println("getting italian word database");
		writer.setDatabase(factory.getDatabase("italianword"));

		System.out.println("write key " + eng_word.getid() + " "
			+ "with data " + eng_word.toString());
		String id = new String(eng_word.getid().toString());
		writer.write(id, eng_word);

		System.out.println("write key " + eng_word.getid() + " "
			+ "with data " + eng_word.toString());
		id = new String(eng_word2.getid().toString());
		writer.write(id, eng_word2);

	    }

	    // ############### ENGLISH VERB WRITE TEST
	    // #################
	    // get the configuration for the englishverb language;
	    // if enabled write an englishverb on the db
	    langconf = LanguagesConfiguration
		    .getLanguageMainConfigNode("italianverb");
	    if (langconf.isEnabled()) {

		Verb eng_verb = new Verb();
		eng_verb.setid(0);
		eng_verb.setlinkid("italian", "0");
		eng_verb.setlinkid("arabic", "0");
		eng_verb.setinfinitive("fly");
		eng_verb.setnotes("english verb");
		eng_verb.setsection("1#2");
		eng_verb.setaudioinfinitive("audiotest".getBytes("UTF8"));

		Verb eng_verb2 = new Verb();
		eng_verb2.setid(1);
		eng_verb2.setlinkid("italian", "1");
		eng_verb2.setlinkid("arabic", "1");
		eng_verb2.setinfinitive("talk");
		eng_verb2.setnotes("english verb");
		eng_verb2.setsection("3#4");
		eng_verb2.setaudioinfinitive("audiotest".getBytes("UTF8"));

		System.out.println("opening english verb database");
		writer.setDatabase(factory.getDatabase("englishverb"));

		String id = new String(eng_verb.getid().toString());
		System.out.println("write key " + eng_verb.getid()
			+ " with data " + eng_verb.toString());
		writer.write(id, eng_verb);

		id = new String(eng_verb2.getid().toString());
		System.out.println("write key " + eng_verb2.getid()
			+ " with data " + eng_verb2.toString());
		writer.write(id, eng_verb2);

	    }

	    // ############### ARABIC WORD WRITE TEST
	    // #################
	    // get the configuration for the arabicword language;
	    // if enabled write an arabicword on the db
	    langconf = LanguagesConfiguration
		    .getLanguageMainConfigNode("arabicword");
	    if (langconf.isEnabled()) {

		Word ara_word = new Word();
		ara_word.setid(0);
		ara_word.setlinkid("italian", "0");
		ara_word.setlinkid("english", "0");
		ara_word.setsingular("");
		ara_word.setnotes("arabic word");
		ara_word.setsection("1#2");
		ara_word.setaudiosingular("audiotest".getBytes("UTF8"));
		ara_word.setcategory("2#3");

		Word ara_word2 = new Word();
		ara_word2.setid(1);
		ara_word2.setlinkid("italian", "1");
		ara_word2.setlinkid("english", "1");
		ara_word2.setsingular("fire");
		ara_word2.setnotes("arabic word");
		ara_word2.setsection("1#2");
		ara_word2.setaudiosingular("audiotest".getBytes("UTF8"));
		ara_word2.setcategory("2#3");

		System.out.println("getting italian word database");
		writer.setDatabase(factory.getDatabase("italianword"));

		// Scrive ara_word
		System.out.println("write key " + ara_word.getid() + " "
			+ "with data " + ara_word.toString());
		String id = new String(ara_word.getid().toString());
		writer.write(id, ara_word);

		// Scrive ara_word2
		System.out.println("write key " + ara_word2.getid() + " "
			+ "with data " + ara_word2.toString());
		id = new String(ara_word2.getid().toString());
		writer.write(id, ara_word2);

	    }

	    // ############### ARABIC VERB WRITE TEST
	    // #################
	    // get the configuration for the arabiverb language;
	    // if enabled write an arabicverb on the db
	    langconf = LanguagesConfiguration
		    .getLanguageMainConfigNode("arabicverb");
	    if (langconf.isEnabled()) {

		ArabVerb ara_verb = new ArabVerb();
		ara_verb.setid(0);
		ara_verb.setinfinitive("يطير");
		ara_verb.setlinkid("english", "0");
		ara_verb.setlinkid("italian", "0");
		ara_verb.setnotes("verbo arabo");
		ara_verb.setsection("1#2");
		ara_verb.setaudioinfinitive("audiotest".getBytes("UTF8"));
		// This method is just for the ArabVerb
		ara_verb.setpast("past");

		ArabVerb ara_verb2 = new ArabVerb();
		ara_verb.setid(0);
		ara_verb.setinfinitive("يتكلّم");
		ara_verb2.setlinkid("english", "1");
		ara_verb2.setlinkid("italian", "1");
		ara_verb2.setnotes("verbo arabo");
		ara_verb2.setsection("1#2");
		ara_verb2.setaudioinfinitive("audiotest".getBytes("UTF8"));
		// This method is just for the ArabVerb
		ara_verb2.setpast("past");

		System.out.println("opening arabic database");
		writer.setDatabase(factory.getDatabase("arabicverb"));

		// Scrive verbo 1
		System.out.println("write key " + ara_verb.getid()
			+ " with data " + ara_verb.toString());
		String id = new String(ara_verb.getid().toString());
		writer.write(id, ara_verb);

		// Scrive verbo 2
		System.out.println("write key " + ara_verb.getid()
			+ " with data " + ara_verb.toString());
		id = new String(ara_verb.getid().toString());
		writer.write(id, ara_verb);

	    }

	} catch (DatabaseException e) {
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (LanguagesConfigurationException e) {
	    e.printStackTrace();
	}

    }

}
