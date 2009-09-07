package testClass.copy.reflection;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import net.verza.jdict.gui.*;

public class JPanelTest extends JPanel {

	private GridBagConstraints c;
	private static final long serialVersionUID = 1L;

	public JPanelTest() {
		super();
		initComponents();
		createAndShowGUI();
	}

	private void initComponents() {

		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(new Insets(5, 5, 5, 5))); // 5 pixels gap to
		// the borders

		c = new GridBagConstraints(); // add some space between components to
		c.insets = new Insets(2, 2, 2, 2); // avoid clutter
		c.anchor = GridBagConstraints.EAST;// anchor all components WEST
		c.weightx = 0.1;
		c.weighty = 0.1;

		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel("JLabel"), c);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 3;
		add(this.getInternalPanel(), c);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 4;
		add(new JLabel("JLabel"), c);

	}

	private void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("JPanelTest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(this);
		frame.setSize(1000, 550);
	    frame.setVisible(true);
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				new JPanelTest();
			}
		});

	}

	public void setPreferredColumnWidths(JTable _table, double[] percentages) {

	}

	public JPanel getInternalPanel() {

		Object dataValues[][];
		dataValues = new Object[1][3];
		String headers[] = { "Obj", "Sing", "Audio" };
		dataValues[0][0] = "Obj_";
		dataValues[0][1] = "Sing_";
		dataValues[0][2] = "Audio_";
		DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(dataValues, headers);
		JTable table = new JTable(dm);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setRowSelectionAllowed(false);
		double[] percentages = { 0.1, 0.6, 0.1 };
		Dimension tableDim = this.getPreferredSize();;
		
		System.out.println("JPanel size "+this.getSize());
		System.out.println("JPanel size "+tableDim.getSize());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		double total = 0;int size=20;
		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++)
			total += percentages[i];

		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			javax.swing.table.TableColumn column = table.getColumnModel()
					.getColumn(i);
			System.out.println("column " + column.getHeaderValue());
			System.out.println("resizing to "
					+ (tableDim.width * (percentages[i] / total)));
			//column
			//		.setPreferredWidth((int) (tableDim.width * (percentages[i] / total)));
			column
			.setPreferredWidth((size *= 2));
		}
		table.doLayout();
		

		
		JPanel jpnl = new JPanel();
		jpnl.setBackground(new Color(0.0f, 1.0f, 0.0f, 0.35f));
		jpnl.setLayout(new GridBagLayout());
		GridBagConstraints d = new GridBagConstraints();
		d.insets = new Insets(2, 2, 2, 2); // avoid clutter
		d.anchor = GridBagConstraints.NORTH;// anchor all components WEST
		d.fill = GridBagConstraints.HORIZONTAL;
		d.weightx = 1.0;
		d.weighty = 1.0;
		d.gridx = 0;
		d.gridy = 0;

		jpnl.add(table, d);
		return jpnl;

	}

}
