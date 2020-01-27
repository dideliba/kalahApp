package com.backbase.games.kalah.model.state.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.backbase.games.kalah.model.Game;
import com.backbase.games.kalah.model.Pit;
import com.backbase.games.kalah.model.state.GameState;
import com.backbase.games.kalah.common.enums.GameStateEnum;
import com.backbase.games.kalah.common.enums.PitTypeEnum;
import com.backbase.games.kalah.common.enums.PlayerEnum;

/**
 * Represents the game state when it's the second player's turn to play
 * @author didel
 *
 */
public class SecondPlayerTurnState implements GameState {

	private static final Logger logger = LoggerFactory.getLogger(SecondPlayerTurnState.class);

	private Game game;
	private GameStateEnum stateEnum;
	
	public SecondPlayerTurnState(Game newGame) {
		this.game=newGame;
		this.stateEnum=GameStateEnum.SECOND_PLAYER_TURN;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Integer, Integer> move(int pitId) {
		logger.info("Second player performs his move");

		Pit lastPit=game.getBoard().move(pitId, PlayerEnum.SECOND_PLAYER);
		
		if(lastPit.getPitOwner()==PlayerEnum.SECOND_PLAYER && 
		   lastPit.getStones()==1 && lastPit.getPitType()==PitTypeEnum.REGULAR_PIT) {
			logger.info("Second player reserving opponents stones");
			game.getBoard().reserveOpponentStones(lastPit.getId());
		}
		
		if(game.isGameOver()) {
			game.handleGameOver();
			game.setCurrentState(game.getGameOverState());
			return game.getBoard().convertBoardToMap();
		}
		
		if(lastPit.getPitOwner()==PlayerEnum.SECOND_PLAYER) {
			//second player can play again
			return game.getBoard().convertBoardToMap();
		}
		//first player's turn		
		game.setCurrentState(game.getFirstPlayerTurnState());
		return game.getBoard().convertBoardToMap();
	}
	
	public GameStateEnum getStateEnum(){
		return this.stateEnum;
	}

}
