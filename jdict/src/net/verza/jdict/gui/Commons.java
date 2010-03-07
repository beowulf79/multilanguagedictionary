package net.verza.jdict.gui;

import javax.swing.ImageIcon;

public class Commons {

    /** Returns an ImageIcon, or null if the path was invalid. */
    public static ImageIcon createImageIcon(String path) {
	java.net.URL imgURL = Commons.class.getResource(path);
	if (imgURL != null) {
	    return new ImageIcon(imgURL);
	} else {
	    return null;
	}
    }

}