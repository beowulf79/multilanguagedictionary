package net.verza.jdict.gui;

import java.awt.*;
import java.io.UnsupportedEncodingException;

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
			System.out.println("instance of byte[]");
			setText("Play Audio");
		}

		else if ((value instanceof String) && (!value.equals("label"))) {
			System.out.println("instance of String");
			String s = (String)value;
			try {
				setText(new String(s.getBytes(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				System.out.println("Exception : " + e.getMessage());
			}
		}

		else {
			System.out.println("instance of anything else");
			setText("#");
		}

		return this;

	}
}