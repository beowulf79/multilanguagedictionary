package net.verza.jdict.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * 
 * @author ChristianVerdelli
 * 
 */

public class JTableAudioButtonRenderer extends JButton implements
	TableCellRenderer {

    private static final long serialVersionUID = 1L;
    private String word;
    
    public JTableAudioButtonRenderer(String _word) {
	setOpaque(true);
	word = _word;
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
	// if audio null set Play audio button disable
	byte[] audio = (byte[]) value;
	if (audio.length == 0) {
	    setText(word);
	    setEnabled(false);
	} else
	    setText(word);

	return this;
    }

}