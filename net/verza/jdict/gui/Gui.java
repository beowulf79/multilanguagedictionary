package net.verza.jdict.gui;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import net.verza.jdict.dictionary.Factory;
import net.verza.jdict.exceptions.DataNotFoundException;
import net.verza.jdict.exceptions.DynamicCursorException;
import net.verza.jdict.properties.LanguagesConfiguration;
import net.verza.jdict.properties.PropertiesLoader;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.sleepycat.je.DatabaseException;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import net.verza.jdict.Configuration;

public class Gui {

	private static Logger log;
	private static final String log4j_filename = Configuration.LOG4JCONF;
	
	
	public static void main(String[] args) {

		PropertyConfigurator.configure(log4j_filename);
		log = Logger.getLogger("default");
		log.trace("Starting...");

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
					// If not UserProfle is present in the database open the
					// window to create a new UserProfile
					try {
						if (Factory.getDictionary().getUserList().length == 0)
							UserProfileCreateGui.getInstance();

					} catch (DatabaseException e) {
						System.err.println(e.getMessage());
					} catch (UnsupportedEncodingException e) {
						System.err.println(e.getMessage());
					} catch (FileNotFoundException e) {
						System.err.println(e.getMessage());
					} catch (DataNotFoundException e) {
						System.err.println(e.getMessage());
					} catch (DynamicCursorException e) {
						System.err.println(e.getMessage());
					}

				}
			});

		} catch (Exception e) {

		}

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				log.trace("Launching user Interface");
				UserProfileLoaderGui.getInstance();
				
			}
		});

	}
}
