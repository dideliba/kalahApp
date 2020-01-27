package com.backbase.games.kalah.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import com.backbase.games.kalah.common.enums.PlayerEnum;
import com.backbase.games.kalah.exception.InvalidPitException;
import com.backbase.games.kalah.exception.InvalidPlayerException;

import static com.backbase.games.kalah.common.constants.GameConstants.FIRST_PLAYER_KALAH_ID;
import static com.backbase.games.kalah.common.constants.GameConstants.SECOND_PLAYER_KALAH_ID;
import static com.backbase.games.kalah.common.constants.GameConstants.STONES_PER_PIT;
import static com.backbase.games.kalah.common.constants.GameConstants.TOTAL_NUMBER_PITS;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BoardTest {

	private  Board initialBoard ;
	private  Board emptyPlayer1Board ;
	private  Map<Integer, Integer> emptyPlayer1BoardMap;
	
	@BeforeAll
	public void initBeforeAllTests() {
		initialBoard=new Board(TOTAL_NUMBER_PITS,STONES_PER_PIT);
		List<Integer> pitList = Arrays.asList(0,0,0,0,0,0,36,6,6,6,6,6,6,0);
		emptyPlayer1Board=new Board(pitList);
		emptyPlayer1BoardMap = Stream.of(new Object[][] { 
		     {1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,36}, 
		     {8,6},{9,6},{10,6},{11,6},{12,6},{13,6},{14,0},
		     }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Integer) data[1]));	
	}
	
	@Test
	public void countStonesOnPlayerSideTest() {
		assertFalse(initialBoard.isPlayerSideEmpty(PlayerEnum.FIRST_PLAYER));
		assertTrue(emptyPlayer1Board.isPlayerSideEmpty(PlayerEnum.FIRST_PLAYER));
		assertFalse(initialBoard.isPlayerSideEmpty(PlayerEnum.SECOND_PLAYER));
	}
	
	@Test
	public void countStonesInKalahTest() {
		assertThat(initialBoard.countStonesInKalah(PlayerEnum.FIRST_PLAYER)).isEqualTo(0);
		assertThat(emptyPlayer1Board.countStonesInKalah(PlayerEnum.FIRST_PLAYER)).isEqualTo(36);
		assertThat(initialBoard.countStonesInKalah(PlayerEnum.SECOND_PLAYER)).isEqualTo(0);
		assertThat(emptyPlayer1Board.countStonesInKalah(PlayerEnum.SECOND_PLAYER)).isEqualTo(0);
	}
	
	@Test
	public void convertBoardToMapTest() {
		Map<Integer,Integer> map= emptyPlayer1Board.convertBoardToMap();
		 //check if all elements between the two maps are equal
	     boolean allMatch=map.keySet().stream().allMatch(key -> map.get(key).equals(emptyPlayer1BoardMap.get(key)));
	     assertTrue(allMatch);
	}
	
	@Test
	public void reserveOpponentStonesTest() {
		List<Integer> pitList = Arrays.asList(0,0,0,1,0,0,35,6,6,6,6,6,6,0);
		Board board=new Board(pitList);
		board.reserveOpponentStones(4);
		assertThat(board.getPits().get(3).getStones()).isEqualTo(0);
		assertThat(board.getPits().get(9).getStones()).isEqualTo(0);
		assertThat(board.getPits().get(FIRST_PLAYER_KALAH_ID-1).getStones()).isEqualTo(42);
	}
	
	@Test
	public void moveTest() {
		assertThrows(InvalidPitException.class , () -> {initialBoard.move(0,PlayerEnum.FIRST_PLAYER);});
		assertThrows(InvalidPitException.class , () -> {initialBoard.move(FIRST_PLAYER_KALAH_ID,PlayerEnum.FIRST_PLAYER);});
		assertThrows(InvalidPlayerException.class , () -> {initialBoard.move(SECOND_PLAYER_KALAH_ID-1,PlayerEnum.FIRST_PLAYER);});
		assertThrows(InvalidPitException.class , () -> {emptyPlayer1Board.move(FIRST_PLAYER_KALAH_ID-1,PlayerEnum.FIRST_PLAYER);});
	}
}
