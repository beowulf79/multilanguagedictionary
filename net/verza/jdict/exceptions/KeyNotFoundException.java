/**
 * 
 */
package net.verza.jdict.exceptions;


/**
 * @author christianverdelli
 *
 */
public class KeyNotFoundException extends Exception {
	
		private static final long serialVersionUID = 1L;
		
        public KeyNotFoundException(String message) {
              // Constructor.  Create a ParseError object containing
              // the given message as its error message.
           super(message);
        }
}

