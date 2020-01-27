package com.backbase.games.kalah.model.state;

import java.util.Map;

import com.backbase.games.kalah.common.enums.GameStateEnum;

/**
 * Represents the various states that a particular game can be in
 * It's methods can change the state of the game
 * 
 * @author didel
 *
 */
public interface GameState {

	/**
     * Method used by a player in order to perform transfer of stones  
     * @param pitId the identifier of the pit by which the player will start (the associated pit must belong to him) This is the only method that can change the state 
     * @return map {@link Map Map.class} with (pitId,#stones) as entries
     */
	public Map<Integer, Integer> move(final int pitId);
	
	/**
	 * @return the {@link GameStateEnum GameStateEnum.class} that corresponds to {@link GameState GameState.class} concretions
	 */
	public GameStateEnum getStateEnum();
}
