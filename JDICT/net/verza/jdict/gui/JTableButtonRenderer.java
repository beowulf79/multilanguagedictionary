package net.verza.jdict.gui;

import java.awt.*;
import java.io.UnsupportedEncodingException;

import javax.swing.*;
import javax.swing.table.*;

import net.verza.jdict.IWord;

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

	public JTableButtonRenderer(String b) {
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
			setText("Play Audio");
		}

		else if ((value instanceof String) && (!value.equals("label"))) {

			IWord w = (IWord) value;
			try {
				setText(new String(w.getsingular().getBytes(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				System.out.println("Exception : " + e.getMessage());
			}
		}

		else {
			if (value != null) {
				IWord w = (IWord) value;
				try {
					setText(new String(w.getsingular().getBytes(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					System.out.println("Exception : " + e.getMessage());
				}
			}
		}

		return this;

	}
}