package net.verza.jdict.sleepycat.datastore;

import java.io.UnsupportedEncodingException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import java.util.Iterator;
import java.util.Vector;
import java.util.HashMap;
import net.verza.jdict.SearchableObject;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguagesConfiguration;
import org.apache.log4j.Logger;


import com.sleepycat.je.DatabaseException;

public class SleepyLinkidResolver {

	private HashMap<String, SleepyDatabaseReader> readerHashMap;
	private HashMap<SearchableObject, HashMap<String, Vector<SearchableObject>>> resolvedIDMap;
	private SleepyFactory factory;
	private static Logger log;
	private Vector<SearchableObject> wkey = new Vector<SearchableObject>();

	
	public SleepyLinkidResolver() {

		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName());
		factory = SleepyFactory.getInstance();
		this.readerHashMap = new HashMap<String, SleepyDatabaseReader>();
		this.resolvedIDMap = new HashMap<SearchableObject, HashMap<String, Vector<SearchableObject>>>();
		this.wkey = new Vector<SearchableObject>();

	}

	public SleepyLinkidResolver(Vector<SearchableObject> _searchableObject) {
		System.out
				.println("chiamato SleepyLinkidResolver constructor with arguments "
						+ "SearchableObject vector of size" + _searchableObject.size());
		this.wkey = _searchableObject;
		// use the first element to set the languages to look up as link id
		this.setLanguage(_searchableObject.firstElement());
	}

	public SleepyLinkidResolver(SearchableObject _searchableObject) {

		System.out
				.println("chiamato SleepyLinkidResolver constructor with arguments "
						+ "verb " + _searchableObject.toString());

		wkey.add(_searchableObject);
		this.setLanguage(_searchableObject);

	}

	public void setSearchableObject(SearchableObject _searchableObject) {
		System.out.println("setting the object to " + _searchableObject.toString());
		this.wkey.add(_searchableObject);

	}

	public void setSearchableObject(Vector<SearchableObject> _searchableObject) {
		log.info("setting the object array with size " + _searchableObject.size());
		this.wkey = _searchableObject;
	}

	/*
	 * read the linkId attribute of the argument and iterate on it to set the
	 * languages to lookup for as Link ID
	 */
	private void setLanguage(SearchableObject _searchableObject) {
			log.trace("called function setLanguage with argument _searchableObject");
			Iterator<String> iterator = _searchableObject.getlinkid().keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				log.debug("setting language to resolve" + key);
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
						new SleepyDatabaseReader(factory
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
		this.readerHashMap = new HashMap<String, SleepyDatabaseReader>();
	}

	/* 
	 * returns all the link ID verbs of the verb given as parameter
	 * 
	 */
	public HashMap<String, Vector<SearchableObject>> getLinkedObjects(SearchableObject _key)	{
		
		System.out.println("called method getVerb with parameter "+_key.toString());
		return this.resolvedIDMap.get(_key);
		
	}

	/* 
	 * returns  the link ID verbs of the verb given as parameter
	 * associated with the language given as parameter
	 * 
	 */
	public Vector<SearchableObject> getLinkedObjects(SearchableObject _key, String _lang)	{
		
		System.out.println("called method getVerb with parameter verb"+_key.toString()
										+" and language "+_lang);
		return this.resolvedIDMap.get(_key).get(_lang);
		
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
		Iterator<SearchableObject> KeyIterator = this.resolvedIDMap
				.keySet().iterator();
		while (KeyIterator.hasNext()) {
				SearchableObject key = KeyIterator.next();
				tmp.put(key, this.resolvedIDMap.get(key).get(language));
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
				SearchableObject searchableObject = keyVectorIterator.next();
				tmp = new HashMap<String, Vector<SearchableObject>>();

				if (this.readerHashMap.isEmpty())
					this.setLanguage(searchableObject);
				
				// iterator the languages of the link ID
				Iterator<String> readerMapIterator = this.readerHashMap
						.keySet().iterator();
				while (readerMapIterator.hasNext()) {
					String lang = (String) readerMapIterator.next();
					log.trace("next language to be resolved  " + lang);
					
					Integer[] ids = searchableObject.getlinkid(lang);
					
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
					
				this.resolvedIDMap.put(searchableObject, (HashMap<String, Vector<SearchableObject>>) tmp.clone());
			}
		}
				

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (DynamicCursorException e) {
			e.printStackTrace();
		} catch (DataNotFoundException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}catch (KeyNotFoundException e) {
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
