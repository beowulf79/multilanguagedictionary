package net.verza.jdict.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import net.verza.jdict.model.DictionaryObject;
import net.verza.jdict.model.SearchableObject;

import org.apache.log4j.Logger;

public class JTableWordButtonEditor extends DefaultCellEditor {

    private static final long serialVersionUID = 1L;
    protected JButton button;
    private DictionaryObject dictionaryObject;
    private boolean isPushed;
    private static Logger log;

    public JTableWordButtonEditor(JCheckBox checkBox) {
	super(checkBox);
	log = Logger.getLogger("jdict");
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
	log.trace("inside getTableCellEditorComponent " + value);

	dictionaryObject = (DictionaryObject) value;

	if (isSelected) {
	    button.setForeground(table.getSelectionForeground());
	    button.setBackground(table.getSelectionBackground());
	} else {
	    button.setForeground(table.getForeground());
	    button.setBackground(table.getBackground());
	}

	if (value != null) {
	    SearchableObject sObj = (SearchableObject) value;
	    sObj.toGraphich();
	}

	isPushed = true;
	return button;
    }

    public Object getCellEditorValue() {
	if (isPushed) {

	}
	isPushed = false;
	return dictionaryObject;
    }

    public boolean stopCellEditing() {
	isPushed = false;
	return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
	super.fireEditingStopped();
    }
}
