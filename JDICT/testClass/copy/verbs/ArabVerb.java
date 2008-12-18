
package testClass.copy.verbs;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.verbs.ArabVerbPast;
import net.verza.jdict.verbs.ArabVerbPresent;


public class ArabVerb extends Verb  implements Serializable {

	private static final long serialVersionUID = -5752057185882277357L;
	private String past;
	private String paradigm;
	//private ArabVerbPast pastObject;
	//private ArabVerbPresent presentObject;
	
	
	public ArabVerb() {
		super();
	}

	
	public String getpast()		{
		return this.past;
	}
	
	public String getparadigm()		{
		return this.paradigm;
	}
	
	
	public void setpast(String _past) {
		try {
			System.out.println("setting past to " + new String(_past.getBytes("UTF-8")) );
			this.past = _past;
		
		} catch (UnsupportedEncodingException e) {
			e.	printStackTrace();
		}
	}
	
	
	public void setparadigm(String _paradigm) {
		this.paradigm = _paradigm;
	}
	
	
	public void pastToString()	 {
		try {
			
			new ArabVerbPast(this.past.toCharArray(), this.paradigm).toString();

		} catch(DataNotFoundException e)		{
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}
	
	
	public void presentToString()	{
		try {
			
			new ArabVerbPresent(this.infinitive.toCharArray(), this.paradigm).toString();

		} catch(DataNotFoundException e)		{
			System.err.println(e.toString());
			e.printStackTrace();
		}
		
	}
	
	
	
	public String toString() {
		

		System.out.println("---------------------------------------------------------------------");
		System.out.println("------------   PAST VERB CONJUGATION   -------------");
		//if (this.past != null) this.pastToString();
		
		
		System.out.println("---------------------------------------------------------------------");
		System.out.println("------------   PRESENT VERB  CONJUGATION  -------------");
		//if (this.infinitive !=  null ) this.presentToString(); 

		
		String toReturn = null;
		//try {

			toReturn =  
			" - ID: " + id  
			+" - infinitive: " +infinitive //new String(this.infinitive.getBytes("UTF-8")) 
			+ " - past "+ past//new String(this.past.getBytes("UTF-8"))
			+ " - link ID: " + linkId.toString() 
			+ " - note: " + this.notes 
			+ " - section: " + this.section.toString()
			+ " - paradigm  " + this.paradigm
			+ " - audio size  " + audio.length
			 ;

	//}catch(UnsupportedEncodingException e)	{
		//	System.err.println(e.getMessage());
	//		e.printStackTrace();
//	}

		return toReturn;
	}

}