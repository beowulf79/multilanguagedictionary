package net.verza.jdict.gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;

public class ColorRenderer extends JLabel implements TableCellRenderer {
	
	private static final long serialVersionUID = 1L;
	private String columnName;

	public ColorRenderer(String column) {
		this.columnName = column;
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Object columnValue = table.getValueAt(row, table.getColumnModel()
				.getColumnIndex(columnName));

		
		if (value != null)
			setText(value.toString());
		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
			if ( (columnValue.equals("correct")) ||  (columnValue.equals("1")) )
				setBackground(java.awt.Color.green);
			else if ( (columnValue.equals("wrong")) ||  (columnValue.equals("-1")) )
				setBackground(java.awt.Color.pink);


		}
		return this;
	}
}