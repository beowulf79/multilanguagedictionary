package net.verza.jdict.model;

import java.util.Map;

import javax.swing.JTable;

public interface SearchableObject {

    public abstract Integer getid();

    public abstract void setid(Integer newvalue);

    public abstract Integer[] getlinkid(String language);

    public abstract Map<String, Integer[]> getlinkid();

    public abstract void setlinkid(String language, String newvalue);

    public abstract void addlinkid(String langauge, Integer newvalue);

    public abstract void removelinkid(String language, Integer newvalue);

    public abstract void setsection(String newvalue);

    public abstract void addsection(String value);

    public abstract void removesection(String value);

    public JTable getTable();

    public void toGraphich();

    public Boolean equals(SearchableObject target, String language);

}