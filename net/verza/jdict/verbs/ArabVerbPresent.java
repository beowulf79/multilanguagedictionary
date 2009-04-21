package net.verza.jdict.verbs;

import java.io.UnsupportedEncodingException;
import net.verza.jdict.exceptions.DataNotFoundException;
import org.apache.log4j.Logger;


/**
 * 
 */

/**
 * @author ChristianVerdelli
 * 
 */
public class ArabVerbPresent implements ArabVerbFormInterface {

	char[] root;
	char[] shortVowelsArray;
	ArabVerbShortVowels sw;

	ArabVerbPresentSuffixes ps;
	ArabVerbPresentPrefixes pp;

	private static Logger log;
	private final String language = "arabic";
	private final String type = "arabic_verb";

	private char[] token = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int common_Side_Counter;

	private String I_P_SING;
	private String II_P_SING_M;
	private String II_P_SING_F;
	private String III_P_SING_M;
	private String III_P_SING_F;
	private String I_P_PLUR;
	private String II_P_PLUR_M;
	private String II_P_PLUR_F;
	private String II_P_DUAL;
	private String III_P_PLUR_M;
	private String III_P_PLUR_F;
	private String III_P_DUAL_M;
	private String III_P_DUAL_F;

	
	public ArabVerbPresent() {
		log = Logger.getLogger("net.verza.jdict.verbs");
		log.trace("called class "+this.getClass().getName());
	}

	
	public ArabVerbPresent(char[] root, String paradigm) throws DataNotFoundException {
		log = Logger.getLogger("net.verza.jdict.verbs");
		
		
		this.root = root;
		
		System.out.println("root length " +root.length);
		
		sw = new ArabVerbShortVowels(paradigm);
		shortVowelsArray = sw.getPresentVowels();
		this.set_Common_Part();
		conjugateForm();
	}

	
	public void conjugateForm() {
		this.set_I_P_SING();
		this.set_II_P_SING_F();
		this.set_I_P_PLUR();
	}

	
	public String[][] get_all_Form() {
		return new String[][] { { ArabVerbPronoun.I_P_SING, I_P_SING }, // corresponds ANA
				{ ArabVerbPronoun.II_P_SING_F, II_P_SING_F }, // corresponds to ANTI
				{ ArabVerbPronoun.I_P_PLUR, I_P_PLUR } }; // corresponds to NAHNU
	}
	


	private void set_Common_Part() {
		log.debug("building common part");
		int root_counter;
		// Each round add a vowel from the root and its short vowels;
		// Some vowels however, does not required short vowels
		for (common_Side_Counter = 2, root_counter = 0; root_counter < root.length; common_Side_Counter++, root_counter++) {
			// adding the vowel (taken from the root)
			token[common_Side_Counter] = root[root_counter]; // +2 because
																// the first two
																// postion have
																// already been
																// set
			log.trace("token " + (common_Side_Counter) + "-  "
					+ token[common_Side_Counter]);
			// these vowels do not want short vowels
			if ((shortVowelsArray[root_counter + 1] == 'ا')
					|| (shortVowelsArray[root_counter + 1] == 'آ')
					|| (shortVowelsArray[root_counter + 1] == 'ى')
					|| (shortVowelsArray[root_counter + 1] == 'ي')
					|| (shortVowelsArray[root_counter + 1] == 'و')) {
				log.trace("found vowels that does not require short vowels");
				continue;
			}
			// adding the short vowel
			if (shortVowelsArray[root_counter + 1] > 0) {
				token[common_Side_Counter + 1] = shortVowelsArray[root_counter + 1];
				log.trace("token " + (common_Side_Counter + 1) + "-  "
						+ token[common_Side_Counter + 1]);

				common_Side_Counter++;
			}

		}
		log.trace("Common Counter value " + common_Side_Counter);
	}

	public String get_I_P_SING() {
		if (I_P_SING != null) {

			return (I_P_SING);
		}
		log.error("I_P_SING empty");
		return null;
	}

	public String get_II_P_SING_F() {
		if (II_P_SING_F != null) {

			return (II_P_SING_F);
		}
		log.error("II_P_SING_F empty");
		return null;
	}

	public String get_I_P_PLUR() {
		if (I_P_PLUR != null) {

			return (I_P_PLUR);
		}
		log.error("I_P_PLUR empty");
		return null;
	}

	public void set_I_P_SING() {
		log.debug("building I_P_SING ");

		// setting prefix with his short vowels first
		token[0] = pp.I_P_SING;
		log.trace("token 0 -  " + token[0]);
		token[1] = shortVowelsArray[0];
		log.trace("token 1 -  " + token[1]);

		// adding suffix
		log.debug("Common Side Counter" + common_Side_Counter);
		int a;
		for (a = 0; a < ps.I_P_SING.length; a++) {
			token[(common_Side_Counter + a)] = ps.I_P_SING[a];
			log.trace("token " + (common_Side_Counter + a) + " -  "
					+ token[(common_Side_Counter + a)]);
		}

		token[(common_Side_Counter + a + 1)] = '\0';

		I_P_SING = new String(token);
	}

	public void set_II_P_SING_F() {
		log.debug("building II_P_SING_F ");

		// setting prefix with his short vowels first
		token[0] = pp.II_P_SING_F;
		log.trace("token 0 -  " + token[0]);
		token[1] = shortVowelsArray[0];
		log.trace("token 1 -  " + token[1]);

		// adding suffix
		log.debug("Common Side Counter" + common_Side_Counter);
		int a;
		for (a = 0; a < ps.II_P_SING_F.length; a++) {
			token[(common_Side_Counter + a)] = ps.II_P_SING_F[a];
			log.trace("token " + (common_Side_Counter + a) + " -  "
					+ token[(common_Side_Counter + a)]);
		}

		token[(common_Side_Counter + a + 1)] = '\0';

		II_P_SING_F = new String(token);
	}

	public void set_I_P_PLUR() {
		log.debug("building I_P_PLUR ");

		// setting prefix with his short vowels first
		token[0] = pp.I_P_PLUR;
		log.trace("token 0 -  " + token[0]);
		token[1] = shortVowelsArray[0];
		log.trace("token 1 -  " + token[1]);

		// adding suffix
		log.debug("Common Side Counter" + common_Side_Counter);
		int a;
		for (a = 0; a < ps.I_P_PLUR.length; a++) {
			token[(common_Side_Counter + a)] = ps.I_P_PLUR[a];
			log.trace("token " + (common_Side_Counter + a) + " -  "
					+ token[(common_Side_Counter + a)]);
		}
		token[(common_Side_Counter + a + 1)] = '\0';

		I_P_PLUR = new String(token);
	}

	
	public String toString() {

		try {

			for (int external_counter = 0; external_counter < get_all_Form().length; external_counter++) {

				System.out.println("-------  PERSONA - VERBO -------");

				for (int internal_counter = 0; internal_counter < get_all_Form()[external_counter].length; internal_counter++) {
					System.out.println(new String(
							get_all_Form()[external_counter][internal_counter]
									.getBytes("UTF-8")));
				}

				System.out.println("----------_------------");
			}

		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		}

		return "Verbo con root " + root + "I_P_SING "
				+ get_all_Form().toString();

	}
	
}
