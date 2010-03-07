package net.verza.jdict.dictionary.sleepycat.indexes;

import java.util.Set;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryMultiKeyCreator;

public abstract class MultiValueIndexKeyCreator implements
	SecondaryMultiKeyCreator {

    @SuppressWarnings(value = "unchecked")
    public abstract void createSecondaryKeys(SecondaryDatabase secondary,
	    DatabaseEntry key, DatabaseEntry data, Set result);

    public abstract void setBinding(EntryBinding binding);

}