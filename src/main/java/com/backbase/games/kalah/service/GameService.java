package com.backbase.games.kalah.service;

import java.util.Map;
import java.util.List;
/**
 * Service used for all game related operations (e.g. creation, move executions etc.)
 * @author didel
 *
 */
public interface GameService {
	
	/**
	 * @return the identifier of the newly created game
	 */
	public int createGame();
	
	/**
	 * removes all games in database
	 */
	public void removeAllGames();
	
	/**
	 * @param gameId id of the game to perform the move 
	 * @param pid id of the Pit to start our move 
	 * @return a {@link Map Map.class} containing the as elements (key:value) pairs of ("Pit id":"stones in pit")
	 * after the move has been successfully performed 
	 */
	public Map<String, String> move(int gameId, int pid);
	
	
	/**
	 * @return a {@link List List.class} with the games in {@link String String.class} representation
	 */
	public List<String> fetchAllGames();
	
		
}
