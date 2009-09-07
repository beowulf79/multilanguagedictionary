package net.verza.jdict.dataloaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public interface IAudioFileLoader {

		// /////////////////////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see net.verza.jdict.IAudioFileLoader#get(java.lang.String)
	 */
	public abstract byte[] get(String filename) throws MalformedURLException,FileNotFoundException, IOException ;

}