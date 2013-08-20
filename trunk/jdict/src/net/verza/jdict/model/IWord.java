package net.verza.jdict.model;

import java.util.Set;

public interface IWord {

    public abstract String getsingular();

    public abstract void setsingular(String newvalue)
	    throws NullPointerException;

    public abstract String getnotes();

    public abstract void setnotes(String newvalue) throws NullPointerException;

    public abstract Object getaudiosingular();

    public abstract void setaudiosingular(byte[] newvalue) throws NullPointerException;

    public abstract Set<String> getsection();

    public abstract void setsection(String newvalue)
	    throws NullPointerException;

    public abstract String toString();

}