package net.verza.jdict.gui;

import java.awt.Font;
import java.util.Enumeration;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class GUIPreferences {
	
	public GUIPreferences() {
		
		UIDefaults defaults = UIManager.getDefaults();
		Enumeration keys = defaults.keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = defaults.get(key);
			if(value != null && value instanceof Font) {
			     UIManager.put(key, null);
			     Font font = UIManager.getFont(key);
			     if(font != null) {
			        UIManager.put(key, new Font("Arial",Font.ROMAN_BASELINE,16));
			     } 
			}
		}
		
	}
	
	
}
