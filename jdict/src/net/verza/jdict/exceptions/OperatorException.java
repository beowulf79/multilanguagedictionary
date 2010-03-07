package net.verza.jdict.exceptions;

public class OperatorException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Construct an OperatorException with the specified message
     * 
     * @param message
     *                The message
     */
    public OperatorException(String message) {

	super(message);
    }
}
