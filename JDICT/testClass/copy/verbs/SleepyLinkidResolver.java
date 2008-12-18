package testClass.copy.verbs;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Vector;
import java.util.HashMap;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.log4j.Logger;
import com.sleepycat.je.DatabaseException;

public class SleepyLinkidResolver {

	private HashMap<String, SleepyVerbsDatabaseReader> readerHashMap;
	private HashMap<SearchableObject, HashMap<String, Vector<SearchableObject>>> resolvedIDMap;
	private SleepyVerbsFactory factory;
	private static Logger log;
	private Vector<SearchableObject> wkey = new Vector<SearchableObject>();

	
	public SleepyLinkidResolver() {

		log = Logger.getLogger("testClass.copy.verbs");
		log.trace("called class " + this.getClass().getName());
		factory = new SleepyVerbsFactory();
		this.readerHashMap = new HashMap<String, SleepyVerbsDatabaseReader>();
		this.resolvedIDMap = new HashMap<SearchableObject, HashMap<String, Vector<SearchableObject>>>();
		this.wkey = new Vector<SearchableObject>();

	}

	public SleepyLinkidResolver(Vector<SearchableObject> _verb) {
		System.out
				.println("chiamato SleepyLinkidResolver constructor with arguments "
						+ "verb vector of size" + _verb.size());
		this.wkey = _verb;
		// use the first element to set the languages to look up as link id
		this.setLanguage(_verb.firstElement());
	}

	public SleepyLinkidResolver(SearchableObject _verb) {

		System.out
				.println("chiamato SleepyLinkidResolver constructor with arguments "
						+ "verb " + _verb.toString());

		wkey.add(_verb);
		this.setLanguage(_verb);

	}

	public void setVerb(SearchableObject _verb) {
		System.out.println("setting the object to " + _verb.toString());
		this.wkey.add(_verb);

	}

	public void setVerb(Vector<SearchableObject> _verb) {
		log.info("setting the object array with size " + _verb.size());
		this.wkey = _verb;
	}

	/*
	 * read the linkId attribute of the argument and iterate on it to set the
	 * languages to lookup for as Link ID
	 */
	private void setLanguage(SearchableObject _verb) {

			Iterator<String> iterator = _verb.getlinkid().keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				System.out.println("language " + key);
				this.setLanguage(key);
			}
	}

	/*
	 * Set the language of the Link ID to lookup for. By default the linkID
	 * resolver queris all the language defined inside the linkid map of the
	 * words. To modify this behaviour call this function and set a specific
	 * language.
	 */
	public void setLanguage(String language_to_lookup) {

		// check if the language is enabled
		LanguageConfigurationClassDescriptor sub =  LanguagesConfiguration.getLanguageMainConfigNode(language_to_lookup);
		
		
		if (sub.getIsEnabled()) {
			try {
				readerHashMap.put(language_to_lookup,
						new SleepyVerbsDatabaseReader(factory
								.getDatabase(language_to_lookup)));

			} catch (DatabaseException e) {
				e.printStackTrace();
			}
	
		}else log.info("language " + language_to_lookup
				+ " not enabled, translation ignored");
	
	}

	/*
	 * Reset the languages HashMap used to lookup.. call this function if this
	 * object has been used before to lookup with different languages
	 */
	public void clearLanguage() {
		this.readerHashMap = new HashMap<String, SleepyVerbsDatabaseReader>();
	}

	/* 
	 * returns all the link ID verbs of the verb given as parameter
	 * 
	 */
	public HashMap<String, Vector<SearchableObject>> getLinkedObjects(SearchableObject _verb_key)	{
		
		System.out.println("called method getVerb with parameter "+_verb_key.toString());
		return this.resolvedIDMap.get(_verb_key);
		
	}

	/* 
	 * returns  the link ID verbs of the verb given as parameter
	 * associated with the language given as parameter
	 * 
	 */
	public Vector<SearchableObject> getLinkedObjects(SearchableObject _verb_key, String _lang)	{
		
		System.out.println("called method getVerb with parameter verb"+_verb_key.toString()
										+" and language "+_lang);
		return this.resolvedIDMap.get(_verb_key).get(_lang);
		
	}
	
	/*
	 * returns all the linked verbs for all the langauges
	 */
	public HashMap<SearchableObject, HashMap<String, Vector<SearchableObject>>>	getLinkedObjects()	{
		return this.resolvedIDMap;
	}

	/*
	 * returns all the linked verbs of the given language
	 */
	@SuppressWarnings("unchecked")
	public HashMap<SearchableObject, Vector<SearchableObject>>	getLinkedObjects(String language)	{
		
		HashMap<SearchableObject, Vector<SearchableObject>>  tmp = new HashMap<SearchableObject, Vector<SearchableObject> >();
		// iterator of the Key Verb 
		Iterator<SearchableObject> verbKeyIterator = this.resolvedIDMap
				.keySet().iterator();
		while (verbKeyIterator.hasNext()) {
				SearchableObject _v = verbKeyIterator.next();
				tmp.put(_v, this.resolvedIDMap.get(_v).get(language));
		}
		
		return (HashMap<SearchableObject, Vector<SearchableObject>>) tmp.clone();
	}

	
	@SuppressWarnings("unchecked")
	public void iterateLinkid() {

		log.trace("chiamato iterateLinkid ");
		HashMap<String, Vector<SearchableObject>> tmp = null;
		Vector<SearchableObject> wdata = null;

		try {

			// Iterator of the Object given
			Iterator<SearchableObject> keyVectorIterator = wkey.iterator();
			while (keyVectorIterator.hasNext()) {
				SearchableObject verb = keyVectorIterator.next();
				tmp = new HashMap<String, Vector<SearchableObject>>();

				if (this.readerHashMap.isEmpty())
					this.setLanguage(verb);
				
				// iterator the languages of the link ID
				Iterator<String> readerMapIterator = this.readerHashMap
						.keySet().iterator();
				while (readerMapIterator.hasNext()) {
					String lang = (String) readerMapIterator.next();
					log.trace("next language to be resolved  " + lang);
					
					Integer[] ids = verb.getlinkid(lang);
					
					//If no linked ID are defined on this object for the language skip to the next language
					if(ids == null) continue;
					wdata = new Vector<SearchableObject>();
					// Loop through the linked ids of the key object
					for (int i = 0; i < ids.length; i++) {
						
						readerHashMap.get(lang).setKey(ids[i].toString());
						readerHashMap.get(lang).setDataBinding(
								SleepyBinding.getDataBinding());
						readerHashMap.get(lang).read();
						System.out.println("found "
								+ readerHashMap.get(lang).getData().size()
								+ " entries using key ID " + ids[i]);

						// we searched using the primary key so just one entry has been returned
						wdata.add((SearchableObject)readerHashMap.get(lang).getData(0));

					tmp.put(lang, wdata);

					}
					
				this.resolvedIDMap.put(verb, (HashMap<String, Vector<SearchableObject>>) tmp.clone());
			}
		}
				

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (DynamicCursorException e) {
			e.printStackTrace();
		} catch (KeyNotFoundException e) {
			e.printStackTrace();
		} catch (DataNotFoundException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

	}

	
	public String toString() {
		Iterator<SearchableObject> keyVerbIterator = this.resolvedIDMap.keySet().iterator();
		while (keyVerbIterator.hasNext()) {
			
			SearchableObject _v = keyVerbIterator.next();
			System.out.println("############################################################################");
			System.out.println("############################################################################");
			System.out.println("KEY VERB "+_v.toString());
			
			HashMap<String, Vector<SearchableObject>> tmp = this.resolvedIDMap
					.get(_v);
			
			Iterator<String> languageKeyIterator = tmp.keySet().iterator();
			while(languageKeyIterator.hasNext()){
				String lang = languageKeyIterator.next();
				System.out.println("CONNECTED VERB FOR LANGUAGE: "+lang);
				
				Iterator<SearchableObject> linkedIDSIterator = tmp.get(lang).iterator();
				while(linkedIDSIterator.hasNext()){
					SearchableObject _v2 = linkedIDSIterator.next();
					System.out.println("Linked Verb "+_v2.toString());
				}
				
					
			}

				

		}

		return "String";
	}
	
	
	
}
