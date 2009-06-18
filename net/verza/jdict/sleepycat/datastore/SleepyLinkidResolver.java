package net.verza.jdict.sleepycat.datastore;

import java.io.UnsupportedEncodingException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LinkIDException;
import java.util.Iterator;
import java.util.Vector;
import java.util.HashMap;
import java.util.HashSet;
import net.verza.jdict.SearchableObject;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.properties.LanguageConfigurationClassDescriptor;
import net.verza.jdict.properties.LanguagesConfiguration;
import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;

public class SleepyLinkidResolver {

	private HashMap<SearchableObject, HashMap<String, Vector<SearchableObject>>> resolvedIDMap;
	private SleepyFactory factory;
	private static Logger log;
	private Vector<SearchableObject> wkey = new Vector<SearchableObject>();
	private SleepyDatabaseReader reader;

	private HashSet<String> languages;

	public SleepyLinkidResolver(SleepyDatabaseReader _r) {

		log = Logger.getLogger("net.verza.jdict.sleepycat.datastore");
		log.trace("called class " + this.getClass().getName());
		factory = SleepyFactory.getInstance();
		this.reader = _r;
		this.resolvedIDMap = new HashMap<SearchableObject, HashMap<String, Vector<SearchableObject>>>();
		this.wkey = new Vector<SearchableObject>();
		this.languages = new HashSet<String>();
	}

	public SleepyLinkidResolver(SleepyDatabaseReader _r,
			Vector<SearchableObject> _searchableObject) {
		this(_r);
		this.wkey = _searchableObject;
		// use the first element to set the languages to look up as link id
		this.setLanguage(_searchableObject.firstElement());
	}

	public SleepyLinkidResolver(SleepyDatabaseReader _r,
			SearchableObject _searchableObject) {
		this(_r);
		wkey.add(_searchableObject);
		this.setLanguage(_searchableObject);

	}

	public void setSearchableObject(SearchableObject _searchableObject) {
		this.wkey.add(_searchableObject);

	}

	public void setSearchableObject(Vector<SearchableObject> _searchableObject) {
		log.info("setting the object array with size "
				+ _searchableObject.size());
		this.wkey = _searchableObject;
	}

	/*
	 * read the linkId attribute of the argument and iterate on it to set the
	 * languages to lookup for as Link ID
	 */
	private void setLanguage(SearchableObject _searchableObject) {
		log
				.trace("called function setLanguage with argument _searchableObject");
		Iterator<String> iterator = _searchableObject.getlinkid().keySet()
				.iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			log.debug("setting language to resolve" + key);
			this.addLanguage(key);
		}
	}

	/*
	 * Set the language of the Link ID to lookup for. By default the linkID
	 * resolver queris all the language defined inside the linkid map of the
	 * words. To modify this behaviour call this function and set a specific
	 * language.
	 */
	public void addLanguage(String _l) {

		// check if the language is enabled
		LanguageConfigurationClassDescriptor sub = LanguagesConfiguration
				.getLanguageMainConfigNode(_l);

		if (sub.getIsEnabled()) {
			this.languages.add(_l);
		} else
			log.info("language " + _l + " not enabled, translation ignored");

	}

	/*
	 * Reset the languages HashMap used to lookup.. call this function if this
	 * object has been used before to lookup with different languages
	 */
	public void clearLanguage() {
		this.languages.clear();
	}

	/*
	 * returns all the link ID objects of the verb given as parameter
	 * 
	 */
	public HashMap<String, Vector<SearchableObject>> getLinkedObjects(
			SearchableObject _key) {

		return this.resolvedIDMap.get(_key);

	}

	/*
	 * returns the link ID objects of the verb given as parameter associated
	 * with the language given as parameter
	 * 
	 */
	public Vector<SearchableObject> getLinkedObjects(SearchableObject _key,
			String _lang) {

		return this.resolvedIDMap.get(_key).get(_lang);

	}

	/*
	 * returns all the linked objects for all the langauges
	 */
	public HashMap<SearchableObject, HashMap<String, Vector<SearchableObject>>> getLinkedObjects() {
		return this.resolvedIDMap;
	}

	/*
	 * returns all the linked objects of the given language
	 */
	@SuppressWarnings("unchecked")
	public HashMap<SearchableObject, Vector<SearchableObject>> getLinkedObjects(
			String language) {

		HashMap<SearchableObject, Vector<SearchableObject>> tmp = new HashMap<SearchableObject, Vector<SearchableObject>>();
		// iterator of the Key Verb
		Iterator<SearchableObject> KeyIterator = this.resolvedIDMap.keySet()
				.iterator();
		while (KeyIterator.hasNext()) {
			SearchableObject key = KeyIterator.next();
			tmp.put(key, this.resolvedIDMap.get(key).get(language));
		}

		return (HashMap<SearchableObject, Vector<SearchableObject>>) tmp
				.clone();
	}

	@SuppressWarnings("unchecked")
	public void iterateLinkid() throws LinkIDException {

		log.trace("chiamato iterateLinkid ");
		HashMap<String, Vector<SearchableObject>> tmp = null;
		Vector<SearchableObject> wdata = null;

		try {

			// Iterator of the Object given
			Iterator<SearchableObject> keyVectorIterator = wkey.iterator();
			while (keyVectorIterator.hasNext()) {
				SearchableObject searchableObject = keyVectorIterator.next();
				tmp = new HashMap<String, Vector<SearchableObject>>();

				// if no languages have been, use all the languages available in
				// the key object
				if (this.languages.size() == 0)
					this.setLanguage(searchableObject);

				// iterator the languages that must be resolved
				Iterator<String> it = this.languages.iterator();
				while (it.hasNext()) {
					String lang = (String) it.next();
					log.trace("next language to be resolved  " + lang);

					Integer[] ids = searchableObject.getlinkid(lang);

					// If no linked ID are defined on this object for the
					// language skip to the next language
					if (ids == null) {
						log
								.error("empty linkId field found, throwing exception");
						throw new LinkIDException("empty link id");
					}
					wdata = new Vector<SearchableObject>();
					// Loop through the linked ids of the key object
					for (int i = 0; i < ids.length; i++) {

						this.reader.setDatabase(factory.getDatabase(lang));
						this.reader.setKey(ids[i].toString());
						this.reader.setDataBinding(SleepyBinding
								.getDataBinding());
						this.reader.read();

						int size = this.reader.getData().size();

						// if no entries are found there is an error with the
						// link id of the word
						// let's report an error in the logs
						if (size > 0) {
							log.info("found " + size + " entries using key ID "
									+ ids[i]);
							// we searched using the primary key so just one
							// entry has been returned
							wdata.add((SearchableObject) reader.getData(0));
							tmp.put(lang, wdata);
						}

						else {
							log.error("wrong linkID for the word "
									+ ids[i].toString());
							throw new LinkIDException(
									"no record found using linkID");
						}

						//reader.closeCursors();
					}

					this.resolvedIDMap.put(searchableObject,
							(HashMap<String, Vector<SearchableObject>>) tmp
									.clone());

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
		} catch (KeyNotFoundException e) {
			e.printStackTrace();
		}

	}

	public String toString() {
		Iterator<SearchableObject> keyVerbIterator = this.resolvedIDMap
				.keySet().iterator();
		while (keyVerbIterator.hasNext()) {

			SearchableObject _v = keyVerbIterator.next();
			HashMap<String, Vector<SearchableObject>> tmp = this.resolvedIDMap
					.get(_v);

			Iterator<String> languageKeyIterator = tmp.keySet().iterator();
			while (languageKeyIterator.hasNext()) {
				String lang = languageKeyIterator.next();
				 System.out.println("connetcted objects for the language: "
				 + lang);

				Iterator<SearchableObject> linkedIDSIterator = tmp.get(lang)
						.iterator();
				while (linkedIDSIterator.hasNext()) {
					SearchableObject _v2 = linkedIDSIterator.next();
					 System.out.println("Linked Objects " + _v2.toString());
				}

			}

		}

		return "String";
	}

}
