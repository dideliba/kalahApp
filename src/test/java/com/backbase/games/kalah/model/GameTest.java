package com.backbase.games.kalah.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import static com.backbase.games.kalah.common.constants.GameConstants.FIRST_PLAYER_KALAH_ID;
import static com.backbase.games.kalah.common.constants.GameConstants.SECOND_PLAYER_KALAH_ID;

import com.backbase.games.kalah.common.util.GameUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameTest {

	private Game gameOver ;
	private Game gameNotOver;
	
	private Map<Integer,Integer> gameOverBoardMap;
	private Map<Integer,Integer> gameNotOverBoardMap;
	
	@BeforeAll
	public void initBeforeAllTests() {
		gameOverBoardMap = Stream.of(new Object[][] { 
		     {1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,9}, 
		     {8,6},{9,6},{10,6},{11,5},{12,5},{13,5},{14,0},
		     }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));
		gameNotOverBoardMap = Stream.of(new Object[][] { 
		     {1,0},{2,1},{3,0},{4,1},{5,0},{6,1},{7,35}, 
		     {8,6},{9,6},{10,6},{11,5},{12,5},{13,6},{14,0},
		     }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));
		
		gameOver=new Game(123);
		gameNotOver=new Game(321);
		gameOver.setBoard(GameUtils.convertMapToBoard(gameOverBoardMap));
		gameNotOver.setBoard(GameUtils.convertMapToBoard(gameNotOverBoardMap));
	}
	
	@Test
	public void isGameOverTest() {		
		assertTrue(gameOver.isGameOver());
		assertFalse(gameNotOver.isGameOver());
	}
	
	@Test
	public void handleGameOverTest() {
		assertEquals(gameOver.getWinner(),"");
		gameOver.handleGameOver();
		assertThat(gameOver.getBoard().getPits().get(FIRST_PLAYER_KALAH_ID-1).getStones()).isEqualTo(9);
		assertThat(gameOver.getBoard().getPits().get(SECOND_PLAYER_KALAH_ID-1).getStones()).isEqualTo(33);
		assertEquals(gameOver.getWinner(),"SECOND_PLAYER");
	}	
}
