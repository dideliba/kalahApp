package com.backbase.games.kalah.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.backbase.games.kalah.dao.GameDAO;
import com.backbase.games.kalah.model.Game;
import com.backbase.games.kalah.service.impl.GameServiceImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameServiceTest {
	
	@InjectMocks
	GameService gameService=new GameServiceImpl();
	
	@Mock
	private GameDAO gameDao;
	
	@Mock
	private Game game;
	
	
	private Map<Integer,Integer> map;
	
	@BeforeAll
	public void initBeforeAllTests() {
		map = Stream.of(new Object[][] { 
		     {1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,36}, 
		     {8,6},{9,6},{10,6},{11,6},{12,6},{13,6},{14,0},
		     }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));	
	
		game=new Game(123);
	}

	@Test
	public void createGameTest() {		
		when(gameDao.saveGame(any(Game.class))).thenReturn(123);
		assertThat(gameService.createGame()).isEqualTo(123);
	}
	
	@Test
	public void moveTest() {
		
		Map <String,String> resultMap;
		when(gameDao.findGameById(anyInt())).thenReturn(game);
		when(game.move(anyInt())).thenReturn(map);
		
		resultMap=gameService.move(123,1);
		//check if the map<Int,Int> has been correctly converted to map<String,String>
		boolean allMatch=map.keySet().stream()
				.allMatch(key -> map.get(key).equals(Integer.valueOf(resultMap.get(key.toString()))));
	    
		assertTrue(allMatch);
	}
}