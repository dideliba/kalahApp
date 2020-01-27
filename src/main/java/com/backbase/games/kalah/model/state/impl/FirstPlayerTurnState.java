package com.backbase.games.kalah.model.state.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.backbase.games.kalah.model.Game;
import com.backbase.games.kalah.model.state.GameState;
import com.backbase.games.kalah.common.enums.GameStateEnum;
import com.backbase.games.kalah.common.enums.PitTypeEnum;
import com.backbase.games.kalah.common.enums.PlayerEnum;

import com.backbase.games.kalah.model.Pit;
/**
 * Represents the game state when it's the first player's turn to play
 * @author didel
 *
 */
public class FirstPlayerTurnState implements GameState {

	private static final Logger logger = LoggerFactory.getLogger(FirstPlayerTurnState.class);

	private Game game;
	private GameStateEnum stateEnum;
	
	public FirstPlayerTurnState(Game newGame) {
		this.game=newGame;
		this.stateEnum=GameStateEnum.FIRST_PLAYER_TURN;
	}
	
	public GameStateEnum getStateEnum(){
		return this.stateEnum;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Integer, Integer> move(int pitId) {
		logger.info("First player performs his move");
		Pit lastPit=game.getBoard().move(pitId, PlayerEnum.FIRST_PLAYER);
		
		if(lastPit.getPitOwner()==PlayerEnum.FIRST_PLAYER && 
		   lastPit.getStones()==1 && lastPit.getPitType()==PitTypeEnum.REGULAR_PIT) {
			logger.info("First player reserving opponents stones");
			game.getBoard().reserveOpponentStones(lastPit.getId());
		}
		
		if(game.isGameOver()) {
			game.handleGameOver();
			game.setCurrentState(game.getGameOverState());
			return game.getBoard().convertBoardToMap();
		}
		
		if(lastPit.getPitOwner()==PlayerEnum.FIRST_PLAYER) {
			//first player can play again
			return game.getBoard().convertBoardToMap();
		}
		//second player's turn		
		game.setCurrentState(game.getSecondPlayerTurnState());
		return game.getBoard().convertBoardToMap();
	}

}
