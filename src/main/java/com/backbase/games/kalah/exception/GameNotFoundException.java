/**
 * 
 */
package com.backbase.games.kalah.exception;

/**
 * Exception thrown when requested game does not exist
 * @author didel
 *
 */
public class GameNotFoundException extends GameException {


	private static final long serialVersionUID = 1L;

	public GameNotFoundException () {
	}
	
	public GameNotFoundException (String message) {
		super(message);
	}

}
