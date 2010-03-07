package net.verza.jdict.dictionary.sleepycat.indexes;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

public abstract class IndexKeyCreator implements SecondaryKeyCreator {

    public abstract boolean createSecondaryKey(SecondaryDatabase secondary,
	    DatabaseEntry key, DatabaseEntry data, DatabaseEntry result);

    public abstract void setBinding(EntryBinding binding);

}