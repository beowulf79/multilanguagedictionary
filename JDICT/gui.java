

import net.verza.jdict.gui.Window;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;


public class gui {

	private static Logger log;
	private static final String log4j_filename = "/Users/ChristianVerdelli/documents/workspace/jdict/conf/log4j.properties";
	private static final String logger_name = "XMLCOMPARER_Logger";
	
	public static void main(String[] args) {


		    PropertyConfigurator.configure(log4j_filename);
			log = Logger.getLogger(logger_name);
			log.trace("Starting...");
						
					
			
			System.setProperty("file.encoding", "UTF-8");
			// Schedule a job for the event-dispatching thread:
			// creating and showing this application's gui.
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					log.trace("Launching user Interface");
					new Window();
				}
			});
	

	}

}