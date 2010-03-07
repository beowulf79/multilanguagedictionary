package net.verza.jdict.exceptions;

/**
 * @author ChristianVerdelli
 * 
 * Construct an OperatorException with the specified message.
 * 
 * @param message
 *                The message
 */

public class DataNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public DataNotFoundException(String message) {
	super(message);
    }
}
