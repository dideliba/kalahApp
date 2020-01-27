package com.backbase.games.kalah.model.state.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.backbase.games.kalah.model.state.GameState;
import com.backbase.games.kalah.common.enums.GameStateEnum;
import com.backbase.games.kalah.model.Game;
/**
 * Represents the game state when the game has ended
 * @author didel
 *
 */
public class GameOverState implements GameState {

	private static final Logger logger = LoggerFactory.getLogger(GameOverState.class);

	private Game game;
	private GameStateEnum stateEnum;

	public GameOverState(Game newGame) {
		this.game=newGame;
		this.stateEnum=GameStateEnum.GAME_OVER;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Integer, Integer> move(int pitId) {
		logger.info("Move requested for game {} despite this game is over (winner is {})",game.getGameId(),game.getWinner());
		return game.getBoard().convertBoardToMap();
	}

	public GameStateEnum getStateEnum(){
		return this.stateEnum;
	}
}
