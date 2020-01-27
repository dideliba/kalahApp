/**
 * 
 */
package com.backbase.games.kalah.exception;

/**
 * Generic Kalah game exception
 * @author didel
 *
 */
public class GameException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	public GameException() {
	}
	
	public GameException(String message) {
		super(message);
	}

}
