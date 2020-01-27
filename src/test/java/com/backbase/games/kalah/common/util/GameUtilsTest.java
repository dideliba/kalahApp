package com.backbase.games.kalah.common.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static com.backbase.games.kalah.common.util.GameUtils.createInitialPit;
import static com.backbase.games.kalah.common.util.GameUtils.createPit;
import static com.backbase.games.kalah.common.util.GameUtils.convertStateEnumToInt;
import static com.backbase.games.kalah.common.util.GameUtils.convertIntToStateEnum;

import com.backbase.games.kalah.model.Pit;
import com.backbase.games.kalah.common.enums.GameStateEnum;
import com.backbase.games.kalah.common.enums.PitTypeEnum;
import com.backbase.games.kalah.common.enums.PlayerEnum;

import static com.backbase.games.kalah.common.constants.GameConstants.FIRST_PLAYER_KALAH_ID;
import static com.backbase.games.kalah.common.constants.GameConstants.SECOND_PLAYER_KALAH_ID;
import static com.backbase.games.kalah.common.constants.GameConstants.STONES_PER_PIT;

class GameUtilsTest {
	
	@Test
	public void createInitialPitTest() {
		
		Pit createdPit1=createInitialPit(FIRST_PLAYER_KALAH_ID-1, STONES_PER_PIT);
		Pit createdPit2=createInitialPit(FIRST_PLAYER_KALAH_ID, STONES_PER_PIT);
		Pit createdPit3=createInitialPit(SECOND_PLAYER_KALAH_ID-1, STONES_PER_PIT);
		Pit createdPit4=createInitialPit(SECOND_PLAYER_KALAH_ID, STONES_PER_PIT);
		
		assertThat(createdPit1.getPitOwner()).isEqualTo(PlayerEnum.FIRST_PLAYER);
		assertThat(createdPit1.getPitType()).isEqualTo(PitTypeEnum.REGULAR_PIT);
		assertThat(createdPit1.getId()).isEqualTo(FIRST_PLAYER_KALAH_ID-1);
		assertThat(createdPit1.getStones()).isEqualTo(STONES_PER_PIT);

		assertThat(createdPit2.getPitOwner()).isEqualTo(PlayerEnum.FIRST_PLAYER);
		assertThat(createdPit2.getPitType()).isEqualTo(PitTypeEnum.KALAH_PIT);
		assertThat(createdPit2.getId()).isEqualTo(FIRST_PLAYER_KALAH_ID);
		assertThat(createdPit2.getStones()).isEqualTo(0);
		
		assertThat(createdPit3.getPitOwner()).isEqualTo(PlayerEnum.SECOND_PLAYER);
		assertThat(createdPit3.getPitType()).isEqualTo(PitTypeEnum.REGULAR_PIT);
		assertThat(createdPit3.getId()).isEqualTo(SECOND_PLAYER_KALAH_ID-1);
		assertThat(createdPit3.getStones()).isEqualTo(STONES_PER_PIT);
		
		assertThat(createdPit4.getPitOwner()).isEqualTo(PlayerEnum.SECOND_PLAYER);
		assertThat(createdPit4.getPitType()).isEqualTo(PitTypeEnum.KALAH_PIT);
		assertThat(createdPit4.getId()).isEqualTo(SECOND_PLAYER_KALAH_ID);
		assertThat(createdPit4.getStones()).isEqualTo(0);
	 }
	
	@Test
	public void createPitTest() {
		
		Pit createdPit1=createPit(FIRST_PLAYER_KALAH_ID-1, STONES_PER_PIT);
		Pit createdPit2=createPit(FIRST_PLAYER_KALAH_ID, STONES_PER_PIT);
		Pit createdPit3=createPit(SECOND_PLAYER_KALAH_ID-1, STONES_PER_PIT);
		Pit createdPit4=createPit(SECOND_PLAYER_KALAH_ID, STONES_PER_PIT);
		
		assertThat(createdPit1.getPitOwner()).isEqualTo(PlayerEnum.FIRST_PLAYER);
		assertThat(createdPit1.getPitType()).isEqualTo(PitTypeEnum.REGULAR_PIT);
		assertThat(createdPit1.getId()).isEqualTo(FIRST_PLAYER_KALAH_ID-1);
		assertThat(createdPit1.getStones()).isEqualTo(STONES_PER_PIT);

		assertThat(createdPit2.getPitOwner()).isEqualTo(PlayerEnum.FIRST_PLAYER);
		assertThat(createdPit2.getPitType()).isEqualTo(PitTypeEnum.KALAH_PIT);
		assertThat(createdPit2.getId()).isEqualTo(FIRST_PLAYER_KALAH_ID);
		assertThat(createdPit2.getStones()).isEqualTo(STONES_PER_PIT);
		
		assertThat(createdPit3.getPitOwner()).isEqualTo(PlayerEnum.SECOND_PLAYER);
		assertThat(createdPit3.getPitType()).isEqualTo(PitTypeEnum.REGULAR_PIT);
		assertThat(createdPit3.getId()).isEqualTo(SECOND_PLAYER_KALAH_ID-1);
		assertThat(createdPit3.getStones()).isEqualTo(STONES_PER_PIT);
		
		assertThat(createdPit4.getPitOwner()).isEqualTo(PlayerEnum.SECOND_PLAYER);
		assertThat(createdPit4.getPitType()).isEqualTo(PitTypeEnum.KALAH_PIT);
		assertThat(createdPit4.getId()).isEqualTo(SECOND_PLAYER_KALAH_ID);
		assertThat(createdPit4.getStones()).isEqualTo(STONES_PER_PIT);

	 }
	
	@Test
	public void convertStateEnumToIntTest() {    
        assertThat(convertStateEnumToInt(GameStateEnum.FIRST_PLAYER_TURN)).isEqualTo(1);
        assertThat(convertStateEnumToInt(GameStateEnum.SECOND_PLAYER_TURN)).isEqualTo(2);
	    assertThrows(IllegalArgumentException.class , () -> {convertStateEnumToInt(null);});  

    }
	
	@Test
	public void convertIntToStateEnumTest() {
	    assertThat(convertIntToStateEnum(1)).isEqualTo(GameStateEnum.FIRST_PLAYER_TURN);
	    assertThat(convertIntToStateEnum(2)).isEqualTo(GameStateEnum.SECOND_PLAYER_TURN);
	    assertThat(convertIntToStateEnum(3)).isEqualTo(GameStateEnum.GAME_OVER);
	    assertThrows(IllegalArgumentException.class , () -> {convertIntToStateEnum(-1);});  
	}
}
