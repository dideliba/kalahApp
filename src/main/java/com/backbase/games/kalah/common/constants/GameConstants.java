package com.backbase.games.kalah.common.constants;
/**
 * This class contains all game related constants for all layers
 * 
 * @author didel
 *
 */
public final class GameConstants {

    private GameConstants() {
     /*restrict instantiation*/
    }
    
    /**
    * The value of TOTAL_NUMBER_PITS is {@value}
    */
    public static final int TOTAL_NUMBER_PITS = 14 ;
    /**
     * The value of STONES_PER_PIT is {@value}
     */
    public static final int STONES_PER_PIT = 6;
	/**
     * The value of FIRST_PLAYER_KALAH_ID is {@value}
     */
	public static final int FIRST_PLAYER_KALAH_ID = 7;
	/**
     * The value of SECOND_PLAYER_KALAH_ID is {@value}
     */
	public static final int SECOND_PLAYER_KALAH_ID = 14;
	
	/**
     * The value of GAME_ID_INITIAL_VALUE is {@value}
     */
	public static final int GAME_ID_INITIAL_VALUE = 100;
}