package net.verza.jdict.gui;

import java.util.HashMap;
import java.util.Iterator;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguagesConfiguration;
import org.apache.log4j.Logger;

/**
 * @author Christian Verdelli
 */

public class LanguageSelection {

	private static Logger log;
	private static HashMap<String, String[]> translations;

	public LanguageSelection() {
		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("called class " + this.getClass().getName());
	}

	/*
	 * Costruisce la mappa con i linguaggi enabled a per ogni linguaggio
	 * la lista dei linguaggi per i quali è disponibile la traduzioneJ.
	 * 
	 */
	public static HashMap<String, String[]> buildLanguageMenu() {

		translations = new HashMap<String, String[]>();

		HashMap<String, LanguageConfigurationClassDescriptor> ldesc = LanguagesConfiguration
				.getLanguageConfigurationBlock();
		LanguageConfigurationClassDescriptor sub = null;

		for (Iterator<String> it = ldesc.keySet().iterator(); it.hasNext();) {
			sub = (LanguageConfigurationClassDescriptor) ldesc.get(it.next());

			String nickname = sub.getLanguageNickname();
			String type = sub.getType();

			// check if the language is enabled , if not skip to the next
			// language
			if (!sub.getIsEnabled())
				continue;

			String[] translation = new String[sub.getTranslations().length];

			int externalCounter = 0;
			//if audio is enabled add audio combo
			if (sub.isAudioEnabled) {
				translation = new String[sub.getTranslations().length + 1];
				translation[externalCounter++] = "audio";
			}

			// check if each  translation are enabled; if so add them to the String array
			for (int internalCounter = 0; internalCounter < sub
					.getTranslations().length; externalCounter++, internalCounter++) {
				LanguageConfigurationClassDescriptor transl = LanguagesConfiguration
						.getLanguageMainConfigNode(sub.getTranslations()[internalCounter]
								+ type);
				if (transl.getIsEnabled())
					translation[externalCounter] = transl.getLanguageNickname()
							+ transl.getType();
			}

			translations.put(nickname + type, translation);
		}

		return translations;
	}

}
