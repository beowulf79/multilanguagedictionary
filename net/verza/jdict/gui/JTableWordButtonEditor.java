package net.verza.jdict.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.verza.jdict.SearchableObject;
import org.apache.log4j.Logger;

public class JTableWordButtonEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;
	protected JButton button;
	private Object obj;
	private boolean isPushed;
	private static Logger log;

	public JTableWordButtonEditor(JCheckBox checkBox) {
		super(checkBox);
		log = Logger.getLogger("net.verza.jdict.gui");
		log.trace("initializing gui class");
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		log.debug("inside getTableCellEditorComponent " + value);
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}

		if (value != null) {
			SearchableObject sObj = (SearchableObject)value;
			sObj.toGraphich();
		}

		isPushed = true;
		return button;
	}

	public Object getCellEditorValue() {
		if (isPushed) {

		}
		isPushed = false;
		return obj;
	}

	public boolean stopCellEditing() {
		isPushed = false;
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}
