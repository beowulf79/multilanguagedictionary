package net.verza.jdict.model;

import java.util.Map;
import java.util.Set;

public interface IVerb {

    public abstract Integer getid();

    public abstract void setid(Integer newvalue) throws NullPointerException;

    public abstract String getinfinitive();

    public abstract void setinfinitive(String newvalue)
	    throws NullPointerException;

    public abstract Integer[] getlinkid(String language);

    public abstract Map<String, Integer[]> getlinkid();

    public abstract void setlinkid(String language, String newvalue)
	    throws NullPointerException;

    public abstract String getnotes();

    public abstract void setnotes(String newvalue) throws NullPointerException;

    public abstract Object getaudioinfinitive();

    public abstract void setaudioinfinitive(byte[] newvalue) throws NullPointerException;

    public abstract Set<String> getsection();

    public abstract void setsection(String newvalue)
	    throws NullPointerException;

}