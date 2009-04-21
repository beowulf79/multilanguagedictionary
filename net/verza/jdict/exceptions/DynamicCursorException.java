package net.verza.jdict.exceptions;

/**
 * @author ChristianVerdelli
 * 
 * Construct an OperatorException with the specified message.
 * 
 * @param message
 *            The message
 */

public class DynamicCursorException extends Exception {
	private static final long serialVersionUID = 1L;

	public DynamicCursorException(String message) {
		super(message);
	}
}
