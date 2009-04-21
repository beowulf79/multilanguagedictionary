package net.verza.jdict;

import java.util.Map;

import javax.swing.JTable;

public interface SearchableObject {

	public abstract Integer getid();

	public abstract void setid(String newvalue) throws NullPointerException;

	public abstract Integer[] getlinkid(String language);

	public abstract Map<String, Integer[]> getlinkid();

	public abstract void setlinkid(String language, String newvalue)
			throws NullPointerException;
	
	public JTable getTable();
	
	public void toGraphich();
	
	public Boolean equals(SearchableObject target, String language);
	
}