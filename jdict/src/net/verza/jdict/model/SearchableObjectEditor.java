package net.verza.jdict.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import javax.swing.JPanel;

import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.exceptions.LanguagesConfigurationException;
import net.verza.jdict.exceptions.LinkIDException;

import com.sleepycat.je.DatabaseException;

public abstract class SearchableObjectEditor extends JPanel {

    abstract void initComponents() throws LanguagesConfigurationException,
	    UnsupportedEncodingException, SecurityException,
	    IllegalArgumentException, FileNotFoundException, DatabaseException,
	    DynamicCursorException, DataNotFoundException,
	    KeyNotFoundException, LinkIDException, ClassNotFoundException,
	    NoSuchMethodException, InstantiationException,
	    IllegalAccessException, InvocationTargetException;

    abstract void write() throws KeyNotFoundException, DynamicCursorException,
	    DataNotFoundException, DatabaseException, IllegalArgumentException,
	    IllegalAccessException, InvocationTargetException,
	    MalformedURLException, FileNotFoundException, IOException,
	    SecurityException, ClassNotFoundException, NoSuchMethodException,
	    InstantiationException;

    abstract SearchableObject getSearchableObject();

}
