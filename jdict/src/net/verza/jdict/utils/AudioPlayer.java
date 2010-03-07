package net.verza.jdict.utils;

/**
 * 
 */

/**
 * @author ChristianVerdelli
 *
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.verza.jdict.exceptions.AudioNotFoundException;

import org.apache.log4j.Logger;

public class AudioPlayer {

    byte stream[];
    private Logger log;

    /**
     * * Constructor.
     */
    public AudioPlayer(byte[] inputbyte) {
	log = Logger.getLogger("jdict");
	log.trace("called class " + this.getClass().getName());
	log.debug("received audio stream byte of size " + inputbyte.length);
	stream = inputbyte;
    }

    public void play() throws AudioNotFoundException {
	try {
	    InputStream ba = new ByteArrayInputStream(stream);

	    AudioInputStream in = AudioSystem.getAudioInputStream(ba);
	    AudioInputStream din = null;
	    AudioFormat baseFormat = in.getFormat();
	    log.debug("format " + baseFormat.toString());
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
	    log.error("AudioNotFoundException ");
	    throw new AudioNotFoundException("suberror: IOException");
	} catch (UnsupportedAudioFileException e) {
	    log.error("UnsupportedAudioFileException");
	    throw new AudioNotFoundException(
		    "suberror: UnsupportedAudioFileException");
	} catch (NullPointerException e) {
	    log.error("NullPointerException");
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
	    log.error("AudioNotFoundException");
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
	    log.error("AudioNotFoundException");
	    throw new AudioNotFoundException(
		    "suberror: LineUnavailableException");
	}
	return res;
    }

}