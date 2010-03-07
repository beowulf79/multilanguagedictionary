package net.verza.jdict.exceptions;

/**
 * @author ChristianVerdelli
 * 
 * Construct an OperatorException with the specified message.
 * 
 * @param message
 *                The message
 */

public class LabelNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public LabelNotFoundException(String message) {
	super(message);
    }
}
