/**
 * 
 */
package net.verza.jdict.exceptions;

/**
 * @author christianverdelli
 * 
 */
public class LinkIDException extends Exception {

    private static final long serialVersionUID = 1L;

    public LinkIDException(String message) {
	// Constructor. Create a ParseError object containing
	// the given message as its error message.
	super(message);
    }
}
