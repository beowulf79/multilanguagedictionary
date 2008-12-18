package testClass.copy.verbs;

import java.util.Map;

public interface SearchableObject {

	public abstract Integer getid();

	public abstract void setid(String newvalue) throws NullPointerException;

	public abstract Integer[] getlinkid(String language);

	public abstract Map<String, Integer[]> getlinkid();

	public abstract void setlinkid(String language, String newvalue)
			throws NullPointerException;

}