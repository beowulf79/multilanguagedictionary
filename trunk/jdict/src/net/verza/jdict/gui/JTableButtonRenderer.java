package net.verza.jdict.gui;

import java.awt.Component;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * /**
 * 
 * @author ChristianVerdelli
 * 
 */

public class JTableButtonRenderer extends JButton implements TableCellRenderer {

    private static final long serialVersionUID = 1L;
    private static Logger log;

    public JTableButtonRenderer(String b) {
	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());
	setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int row, int column) {

	if (isSelected) {
	    setForeground(table.getSelectionForeground());
	    setBackground(table.getSelectionBackground());
	} else {
	    setForeground(table.getForeground());
	    setBackground(UIManager.getColor("Button.background"));
	}

	if (value instanceof byte[]) {
	    log.debug("instance of byte[]");
	    setText("Play Audio");
	}

	else if ((value instanceof String) && (!value.equals("label"))) {
	    log.debug("instance of String");
	    String s = (String) value;
	    try {
		setText(new String(s.getBytes(), "UTF-8"));
	    } catch (UnsupportedEncodingException e) {
		log.error("UnsupportedEncodingException : " + e.getMessage());
	    }
	}

	else {
	    log.debug("don't know how to render received data type");
	    setText("#");
	}

	return this;

    }
}