package net.verza.jdict.gui;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;

import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.exceptions.KeyNotFoundException;
import net.verza.jdict.properties.Configuration;
import net.verza.jdict.properties.LanguagesConfiguration;
import net.verza.jdict.properties.PropertiesLoader;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.sleepycat.je.DatabaseException;

public class Gui {

	private static Logger log;
	private static final String log4j_filename = Configuration.LOG4JCONF;

	public static void main(String[] args) {

		PropertyConfigurator.configure(log4j_filename);
		log = Logger.getLogger("jdict");
		log.trace("starting gui");

		new PropertiesLoader();
		new LanguagesConfiguration();
		new GUIPreferences();

		System.setProperty("file.encoding", "UTF-8");

		Toolkit.getDefaultToolkit().addAWTEventListener(
				new net.verza.jdict.gui.DebugEventListener(),
				AWTEvent.MOUSE_EVENT_MASK);

		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					// If not UserProfle is present in the
					// database open the
					// window to create a new UserProfile

					try {

						if (Factory.getDictionary().getUserList().length <= 0) {
							log.trace("loading user create gui");
							UserProfileCreateGui.getInstance();
						} else {
							log.trace("loading user loader gui");
							UserProfileLoaderGui.getInstance();
						}
					} catch (DatabaseException e) {
						JOptionPane.showMessageDialog(null,
								"DatabaseException " + e.getMessage());
						log.error("DatabaseException: " + e.getMessage() + "- "
								+ e);
						e.printStackTrace();
						System.out.println("exception " + e.getMessage());
					} catch (UnsupportedEncodingException e) {
						JOptionPane.showMessageDialog(
								null,
								"UnsupportedEncodingException "
										+ e.getMessage());
						log.error("UnsupportedEncodingException: "
								+ e.getMessage() + "- " + e);
						e.printStackTrace();
						System.out.println("exception " + e.getMessage());
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(null,
								"FileNotFoundException " + e.getMessage());
						log.error("FileNotFoundException: " + e.getMessage()
								+ "- " + e);
						e.printStackTrace();
						System.out.println("exception " + e.getMessage());
					} catch (DataNotFoundException e) {
						JOptionPane.showMessageDialog(null,
								"DataNotFoundException " + e.getMessage());
						log.error("DataNotFoundException: " + e.getMessage()
								+ "- " + e);
						e.printStackTrace();
						System.out.println("exception " + e.getMessage());
					} catch (DynamicCursorException e) {
						JOptionPane.showMessageDialog(null,
								"DynamicCursorException " + e.getMessage());
						log.error("DynamicCursorException: " + e.getMessage()
								+ "- " + e);
						e.printStackTrace();
						System.out.println("exception " + e.getMessage());
					} catch (KeyNotFoundException e) {
						JOptionPane.showMessageDialog(null,
								"KeyNotFoundException " + e.getMessage());
						log.error("KeyNotFoundException: " + e.getMessage()
								+ "- " + e);
						e.printStackTrace();
						System.out.println("exception " + e.getMessage());
					} catch (IllegalArgumentException e) {
						JOptionPane.showMessageDialog(null,
								"IllegalArgumentException " + e.getMessage());
						log.error("IllegalArgumentException: " + e.getMessage()
								+ "- " + e);
						e.printStackTrace();
						System.out.println("exception " + e.getMessage());
					} catch (ClassNotFoundException e) {
						JOptionPane.showMessageDialog(null,
								"ClassNotFoundException " + e.getMessage());
						log.error("ClassNotFoundException: " + e.getMessage()
								+ "- " + e);
						e.printStackTrace();
						System.out.println("exception " + e.getMessage());
					} catch (NoSuchMethodException e) {
						JOptionPane.showMessageDialog(null,
								"NoSuchMethodException " + e.getMessage());
						log.error("NoSuchMethodException: " + e.getMessage()
								+ "- " + e);
						e.printStackTrace();
						System.out.println("exception " + e.getMessage());
					} catch (InstantiationException e) {
						JOptionPane.showMessageDialog(null,
								"InstantiationException " + e.getMessage());
						log.error("InstantiationException: " + e.getMessage()
								+ "- " + e);
						e.printStackTrace();
						System.out.println("exception " + e.getMessage());
					} catch (IllegalAccessException e) {
						JOptionPane.showMessageDialog(null,
								"IllegalAccessException " + e.getMessage());
						log.error("IllegalAccessException: " + e.getMessage()
								+ "- " + e);
						e.printStackTrace();
						System.out.println("exception " + e.getMessage());
					} catch (InvocationTargetException e) {
						JOptionPane.showMessageDialog(null,
								"InvocationTargetException " + e.getMessage());
						log.error("InvocationTargetException: "
								+ e.getMessage() + "- " + e);
						e.printStackTrace();
						System.out.println("exception " + e.getMessage());
					} catch (Exception e) {
						log.error("exception " + e.getMessage());
						JOptionPane.showMessageDialog(null, "Exception " + e.getLocalizedMessage());
						System.out.println("exception " + e.getMessage());
					}

				}
			});

		} catch (Exception e) {
			log.error("exception " + e.getMessage());
			JOptionPane.showMessageDialog(null, "Exception " + e.getLocalizedMessage());
			System.out.println("exception " + e.getMessage());
		}

	}

}
