package com.backbase.games.kalah.dao;

import com.backbase.games.kalah.model.Game;
import java.util.List;
/**
 * Interface for performing CRUD operations on various game repositories 
 * @author didel
 *
 */
public interface GameDAO {
	
	/**
	 * @param id the gameId of the game to retrieve
	 * @return the {@link Game Game.class} having this id or null if it does not exists in repository
	 */
	public Game findGameById(int id);
	
	/**
	 * @param game the {@link Game Game.class} object to persist on repository
	 * @return the id of the newly created game
	 */
	public int saveGame(Game game);
	
	/**
	 * deletes all existing games
	 */
	public void deleteAllGames();

	/**
	 * @return a {@link List List.class} containing all existing games in string representation 
	 */
	public List<String> findAllGames();
}
