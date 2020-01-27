/**
 * 
 */
package com.backbase.games.kalah.service.impl;

import static com.backbase.games.kalah.common.constants.GameConstants.GAME_ID_INITIAL_VALUE;
import com.backbase.games.kalah.model.Game;
import com.backbase.games.kalah.service.GameService;
import com.backbase.games.kalah.dao.GameDAO;

import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link GameService GameService.class}
 * @author didel
 *
 */
@Service("gameService")
public class GameServiceImpl implements GameService {
	
	private AtomicInteger counter; //provides the gameId sequence


	@Autowired
	private GameDAO gameDao;
	
	public void setCounter(AtomicInteger counter) {
		this.counter = counter;
	}
	
	public GameServiceImpl() {
		counter=new AtomicInteger(GAME_ID_INITIAL_VALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int createGame() {
		Game newGame=new Game(counter.getAndIncrement());
		return gameDao.saveGame(newGame);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllGames() {
		gameDao.deleteAllGames();
		this.counter=new AtomicInteger(GAME_ID_INITIAL_VALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> move(int gameId, int pid) {
		Game game= gameDao.findGameById(gameId);
		Map <Integer, Integer> boardMap= game.move(pid);
		gameDao.saveGame(game);
		//convert to sorted <String,Stirng> map		
		LinkedHashMap<String, String> stringBoardMap = new LinkedHashMap<>();
		boardMap.entrySet()
		    	.stream()
		    	.sorted(Map.Entry.comparingByKey())
		    	.forEachOrdered(x -> stringBoardMap.put(x.getKey().toString(), x.getValue().toString()));
		
		return stringBoardMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> fetchAllGames() {
		return gameDao.findAllGames();
	}

}
