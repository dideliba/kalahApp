package com.backbase.games.kalah.exception;

/**
 * Exception thrown when a player tries to play out of turn
 * @author didel
 *
 */
public class InvalidPlayerException extends GameException {


	private static final long serialVersionUID = 1L;

	public InvalidPlayerException() {
	}
	
	public InvalidPlayerException(String message) {
		super(message);
	}

}
