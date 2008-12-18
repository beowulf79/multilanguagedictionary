/**
 * 
 */
package testClass.copy;

/**
 * @author ChristianVerdelli
 * 
 */

import java.awt.*;

class ShowString extends Frame {

	FontMetrics fontM;
	String outString;

	ShowString(String target, String title) {

		setTitle(title);
		outString = target;

		Font font = new Font("Monospaced", Font.PLAIN, 36);
		fontM = getFontMetrics(font);
		setFont(font);

		int size = 0;
		for (int i = 0; i < outString.length(); i++) {
			size += fontM.charWidth(outString.charAt(i));
		}
		size += 24;

		setSize(size, fontM.getHeight() + 60);
		setLocation(getSize().width / 2, getSize().height / 2);
		setVisible(true);
	}

	public void paint(Graphics g) {
		Insets insets = getInsets();
		int x = insets.left;
		int y = insets.top;
		g.drawString(outString, x + 6, y + fontM.getAscent() + 14);
	}
}