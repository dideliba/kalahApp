package com.backbase.games.kalah.model;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.backbase.games.kalah.model.Board;

import static com.backbase.games.kalah.common.constants.GameConstants.TOTAL_NUMBER_PITS;
import static com.backbase.games.kalah.common.constants.GameConstants.STONES_PER_PIT;


import com.backbase.games.kalah.model.state.GameState;
import com.backbase.games.kalah.model.state.impl.GameOverState;
import com.backbase.games.kalah.model.state.impl.FirstPlayerTurnState;
import com.backbase.games.kalah.model.state.impl.SecondPlayerTurnState;

import com.backbase.games.kalah.common.enums.PlayerEnum;
/**
 * Class that represents a Kalah game
 * 
 * @author didel
 *
 */
public class Game {
	
	private static final Logger logger = LoggerFactory.getLogger(Game.class);

	private String winner;
	private Board board;
	/* instance variables of all possible game states */
	private GameState gameOverState;
	private GameState firstPlayerTurnState;
	private GameState secondPlayerTurnState;
	
	/* the current state of the game can be on of the 3 above actually */
	private GameState currentState;
	
	private int gameId; 
	
	public Game(int id) {

		this.gameId=id;
		this.board=new Board(TOTAL_NUMBER_PITS,STONES_PER_PIT);
		this.winner="";
		
		this.gameOverState=new GameOverState(this);
		this.firstPlayerTurnState=new FirstPlayerTurnState(this);
		this.secondPlayerTurnState=new SecondPlayerTurnState(this);
		
		//the first player will begin
		this.currentState=this.firstPlayerTurnState;
	}
	
	public Game(int id, Board board) {
		this.gameId=id;
		this.board= board;
		this.winner="";
		
		this.gameOverState=new GameOverState(this);
		this.firstPlayerTurnState=new FirstPlayerTurnState(this);
		this.secondPlayerTurnState=new SecondPlayerTurnState(this);
	}
	
	 
	/**
	 * @return the {@link String String.class} representation of a game
	 */
	@Override
	public String toString() {
		return "Game (id=" + gameId + ", winner= " + winner
					+ ", currentState=" + currentState.getStateEnum() + ")";
	}
	 
	
	/**
	 * @return the {@link GameOverState GameOverState.class} of the game
	 */
	public GameState getGameOverState() {
		return gameOverState;
	}

	/**
	 * @return the {@link FirstPlayerTurnState FirstPlayerTurnState.class}
	 */
	public GameState getFirstPlayerTurnState() {
		return firstPlayerTurnState;
	}

	/**
	 * @param firstPlayerTurn the {@link FirstPlayerTurnState FirstPlayerTurnState.class} to set to the game
	 */
	public void setFirstPlayerTurnState(GameState firstPlayerTurn) {
		this.firstPlayerTurnState = firstPlayerTurn;
	}

	/**
	 * @return the {@link SecondPlayerTurnState SecondPlayerTurnState.class}
	 */
	public GameState getSecondPlayerTurnState() {
		return secondPlayerTurnState;
	}

	/**
	 * @param secondPlayerTurn the {@link SecondPlayerTurnState SecondPlayerTurnState.class} to set to the game
	 */
	public void setSecondPlayerTurnState(GameState secondPlayerTurn) {
		this.secondPlayerTurnState = secondPlayerTurn;
	}

	/**
	 * @return the current {@link  GameState  GameState.class} of the game
	 */
	public GameState getCurrentState() {
		return currentState;
	}

	/**
	 * @param currentState the current {@link  GameState  GameState.class} of the game to set
	 */
	public void setCurrentState(GameState currentState) {
		this.currentState = currentState;
	}
	
	/**
	 * @return the {@link String String.class} representation of the winner of the game
	 */
	public String getWinner() {
		return this.winner;
	}
	
	/**
	 * @param winner the {@link String String.class} representation to set
	 */
	public void setWinner(String winner) {
		this.winner=winner;
	}
	
	/**
	 * @param pitId id of initial pit of the move
	 * @return a {@link Map Map.class} representation of board state after the move execution
	 */
	public Map<Integer, Integer> move(int pitId) {
		return this.currentState.move(pitId);
	}

	/**
	 * @return id of the game
	 */
	public int getGameId() {
		return gameId;
	}

	/**
	 * @param gameId id of the game to set
	 */
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	/**
	 * @return {@link Board Board.class} of the game
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @param board {@link Board Board.class} to set in game
	 */
	public void setBoard(Board board) {
		this.board = board;
	}
	
	/**
	 * @param key the int of the state {1:firstPlayersTurn,2:secondPlayersTurn,3:gameOver}
	 * @return {@link GameState GameState.class} corresponding to key or null if key is invalid
	 */
	public GameState gameStateFactory(int key) {
    	int validInputsArray [] = { 1, 2, 3}; //validate input and throw exception if invalid
    	if(!IntStream.of(validInputsArray).anyMatch(x -> x == key)) {
    		logger.error("Invalid input provided provided in method gameStateFactory");
    		throw new IllegalArgumentException();
    	}
    	switch (key) {
    		case 1:	{ return new FirstPlayerTurnState(this); }
    		case 2:	{ return new SecondPlayerTurnState(this); }
    		case 3:	{ return new GameOverState(this); }
    		default: {return null;}
    	}
     }
	
	/**
	 * @return true if this game is over or false otherwise
	 */
	public boolean isGameOver() {
		return Arrays.stream(PlayerEnum.values())
		.map(p -> this.board.isPlayerSideEmpty(p))
		.anyMatch(x-> x==true);
	}
	
	/**
	 * performs the required actions after this game is over (i.e move remaining stones to kalah)
	 */
	public void handleGameOver() {
		//add all remaining stones to user kalah
		Arrays.stream(PlayerEnum.values()).forEach(p -> this.board.addAllStonesToKalah(p));
		//find winner
		this.winner=Arrays.stream(PlayerEnum.values())
				.max((x, y) -> this.board.countStonesInKalah(x) - this.board.countStonesInKalah(y))
				.get().toString();
	}
}
