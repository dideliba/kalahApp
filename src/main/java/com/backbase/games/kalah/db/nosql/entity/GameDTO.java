package com.backbase.games.kalah.db.nosql.entity;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Class that represents a Kalah game on mongoDB
 * 
 * @author didel
 *
 */
@Document("game")
public class GameDTO {

	@Id
	private int gameId; 
	private Map<Integer,Integer> board;
	private int state;
	private String winner;
	
	public GameDTO() {
		this.gameId=0;
	}
	
	public GameDTO(int id) {
		this.gameId=id;
	}
	
	/**
	 * @return the winner {@link String String.class} representation 
	 */
	public String getWinner() {
		return this.winner;
	}
	
	/**
	 * @param winner the winner {@link String String.class} to set
	 */
	public void setWinner(String winner) {
		this.winner = winner;
	}
	
	/**
	 * @return the id of the game
	 */
	public int getGameId() {
		return gameId;
	}
    
	/**
	 * @param gameId set id to set
	 */
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	/**
	 * @return {@link Map Map.class} representation of the board 
	 */
	public Map<Integer, Integer> getBoard() {
		return board;
	}

	/**
	 * @param board {@link Map Map.class} representation of the board to set
	 */
	public void setBoard(Map<Integer, Integer> board) {
		this.board = board;
	}

	/**
	 * @return int representing the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state the state int to set
	 */
	public void setState(int state) {
		this.state = state;
	}
}
