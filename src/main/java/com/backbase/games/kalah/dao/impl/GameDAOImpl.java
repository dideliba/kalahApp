package com.backbase.games.kalah.dao.impl;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.backbase.games.kalah.common.util.GameUtils;
import com.backbase.games.kalah.dao.GameDAO;
import com.backbase.games.kalah.model.Game;
import com.backbase.games.kalah.model.state.GameState;
import com.backbase.games.kalah.db.nosql.entity.GameDTO;
import com.backbase.games.kalah.db.nosql.repository.GameRepository;
import com.backbase.games.kalah.exception.GameNotFoundException;

/**
 * Implementation of the DAO layer
 * The underlying store is a nosql database (mongoDB)
 * @author didel
 *
 */
@Repository("gameDAO")
public class GameDAOImpl implements GameDAO {

	private static final Logger logger = LoggerFactory.getLogger(GameDAOImpl.class);

    @Autowired
    GameRepository gameRepository;

    /**
     * {@inheritDoc}
     */
    @Override
	public List<String> findAllGames() {
    	List<String> result= new ArrayList<String>();
    	List<GameDTO> games=gameRepository.findAll();
    	Game game=null;
		GameState currentState=null;
    	for(GameDTO g : games) {
    		game= new Game(g.getGameId(),null);
    		currentState=game.gameStateFactory(g.getState());
    		game.setCurrentState(currentState);
    		game.setWinner(g.getWinner());
    		result.add(game.toString());
    	}
    	return result;
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
	public void deleteAllGames () {
		gameRepository.deleteAll();
	}
    
    /**
     * {@inheritDoc}
     */
	@Override
	public Game findGameById(int id) {
	
		GameDTO gameDTO = gameRepository.findById(id).orElse(null);
		if (gameDTO == null) {
			logger.info("No game with id: "+ id + " found");
			throw new GameNotFoundException("No game with id "+id +"found");
		}
		//restore current state of the game
		Game game= new Game(gameDTO.getGameId(), GameUtils.convertMapToBoard(gameDTO.getBoard()));
		
		GameState currentState=game.gameStateFactory(gameDTO.getState());
		game.setCurrentState(currentState);
		game.setWinner(gameDTO.getWinner());
		return game;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int saveGame(Game game) {
		int id=game.getGameId();
		int state=GameUtils.convertStateEnumToInt(game.getCurrentState().getStateEnum());
		GameDTO gameDTO = new GameDTO(id);
		gameDTO.setBoard(game.getBoard().convertBoardToMap());
		gameDTO.setState(state);
		gameDTO.setWinner(game.getWinner());
		gameRepository.save(gameDTO);
		return id;
	}
}
