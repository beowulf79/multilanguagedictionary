package net.verza.jdict.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import net.verza.jdict.AudioPlayer;
import net.verza.jdict.exceptions.AudioNotFoundException;
import org.apache.log4j.Logger;

public class JTableAudioButtonEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;

	protected JButton button;
	private boolean isPushed;
	private static Logger log;
	private Object value;

	public JTableAudioButtonEditor(JCheckBox checkBox) {
		super(checkBox);
		log = Logger.getLogger("XMLCOMPARER_Logger");
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
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}

		this.value = value;
		log.trace("playing audio data from table");
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
		return this.value;
	}

	public boolean stopCellEditing() {
		isPushed = false;
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}
