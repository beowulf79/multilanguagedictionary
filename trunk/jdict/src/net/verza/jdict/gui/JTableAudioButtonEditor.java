package net.verza.jdict.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import net.verza.jdict.exceptions.AudioNotFoundException;
import net.verza.jdict.utils.AudioPlayer;

import org.apache.log4j.Logger;

public class JTableAudioButtonEditor extends DefaultCellEditor {
    private static final long serialVersionUID = 1L;

    protected JButton button;
    private boolean isPushed;
    private static Logger log;
    private byte[] audio;

    public JTableAudioButtonEditor(JCheckBox checkBox) {
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

	audio = (byte[]) value;
	try {
	    AudioPlayer player = new AudioPlayer((byte[]) value);
	    player.play();
	} catch (AudioNotFoundException e) {
	    log.error("AudioNotFoundException " + e.getMessage());
	    JOptionPane.showMessageDialog(new JFrame(), "audio player error: "
		    + e.getMessage());
	}

	isPushed = true;
	return button;
    }

    public Object getCellEditorValue() {
	if (isPushed) {
	}
	isPushed = false;
	return audio;
    }

    public boolean stopCellEditing() {
	isPushed = false;
	return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
	super.fireEditingStopped();
    }
}
