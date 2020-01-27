/**
 * 
 */
package com.backbase.games.kalah.exception;

/**
 * Exception thrown when an invalid pit is requested
 * @author didel
 *
 */
public class InvalidPitException extends GameException {


	private static final long serialVersionUID = 1L;

	public InvalidPitException() {
	}
	
	public InvalidPitException(String message) {
		super(message);
	}

}
