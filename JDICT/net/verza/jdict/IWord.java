package net.verza.jdict;

import java.util.Set;

import net.verza.jdict.exceptions.AudioNotFoundException;

public interface IWord {

	
	public abstract String getlanguage();

	
	
	public abstract String getsingular();

	
	
	public abstract String getplural();

	
	
	public abstract String getid();

	
	
	public abstract String[] getlinkid();

	
	
	public abstract Object getaudio();

	
	
	public abstract int setsingular(String newValue);

	
	
	public abstract int setplural(String newValue);
	
	
	
	public abstract void setaudio(byte[] newAudioStream);

	
	
	public abstract int setid(String newKey);

	
	
	public abstract int setlinkid(String key);

	
	
	public abstract String getcomment();

	
	
	public abstract int setcomment(String text);

	
	
	public abstract Set<String> getsection();

	
	
	public abstract int setsection(String s);

	
	
	public abstract Set<String> getcategory();

	
	
	public abstract int setcategory(String s);

	
	
	public abstract void playaudio() throws AudioNotFoundException;

	
}