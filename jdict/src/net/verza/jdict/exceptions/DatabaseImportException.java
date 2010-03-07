package net.verza.jdict.exceptions;

public class DatabaseImportException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Construct an OperatorException with the specified message
     * 
     * @param message
     *                The message
     */
    public DatabaseImportException(String message) {

	super(message);
    }
}
