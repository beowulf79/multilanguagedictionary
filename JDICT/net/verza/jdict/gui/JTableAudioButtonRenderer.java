package net.verza.jdict.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * 
 * 
 * /**
 * 
 * @author ChristianVerdelli
 * 
 */

public class JTableAudioButtonRenderer extends JButton implements
		TableCellRenderer {

	private static final long serialVersionUID = 1L;

	public JTableAudioButtonRenderer(String b) {
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
		setText("Play Audio");
		return this;
	}
}