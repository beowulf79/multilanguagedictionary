package net.verza.jdict.exceptions;

/**
 * @author ChristianVerdelli
 * 
 * Construct an OperatorException with the specified message.
 * 
 * @param message
 *                The message
 */

public class AudioNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AudioNotFoundException(String message) {
	super(message);
    }
}
