package net.verza.jdict;

/**
 * 
 */

/**
 * @author ChristianVerdelli
 *
 */

import javax.sound.sampled.*;
import java.io.*;
import org.apache.log4j.Logger;
import net.verza.jdict.exceptions.*;

public class AudioPlayer {

	byte stream[];
	private Logger log;

	/**
	 * * Constructor.
	 */
	public AudioPlayer(byte[] inputbyte) {
		log = Logger.getLogger("XMLCOMPARER_Logger");
		log.trace("called class " + this.getClass().getName());
		stream = inputbyte;
	}

	public void play() throws AudioNotFoundException {
		try {
			InputStream ba = new ByteArrayInputStream(stream);
			log.debug("Av bytes:" + ba.available());
			log.trace("Supporta: " + ba.markSupported());

			AudioInputStream in = AudioSystem.getAudioInputStream(ba);
			AudioInputStream din = null;
			AudioFormat baseFormat = in.getFormat();
			log.info("format " + baseFormat.toString());
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			// Play now.
			rawplay(decodedFormat, din);
			in.close();
		} catch (IOException e) {
			throw new AudioNotFoundException("suberror: IOException");
		} catch (UnsupportedAudioFileException e) {
			throw new AudioNotFoundException(
					"suberror: UnsupportedAudioFileException");
		} catch (NullPointerException e) {
			throw new AudioNotFoundException("suberror: NullPointerException");
		}
	}

	private void rawplay(AudioFormat targetFormat, AudioInputStream din)
			throws AudioNotFoundException {

		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		try {
			if (line != null) {
				// Start
				line.start();
				int nBytesRead = 0;
				while (nBytesRead != -1) {
					nBytesRead = din.read(data, 0, data.length);
					if (nBytesRead != -1)
						line.write(data, 0, nBytesRead);
				}
				// Stop
				line.drain();
				line.stop();
				line.close();
				din.close();
			}
		} catch (IOException e) {
			throw new AudioNotFoundException("suberror: IOException");
		}

	}

	private SourceDataLine getLine(AudioFormat audioFormat)
			throws AudioNotFoundException {
		SourceDataLine res = null;
		try {
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,
					audioFormat);
			res = (SourceDataLine) AudioSystem.getLine(info);
			res.open(audioFormat);

		} catch (LineUnavailableException e) {
			throw new AudioNotFoundException(
					"suberror: LineUnavailableException");
		}
		return res;
	}

}