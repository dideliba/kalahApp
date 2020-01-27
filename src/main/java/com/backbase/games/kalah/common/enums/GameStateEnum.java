package com.backbase.games.kalah.common.enums;

/**
 * Enumerated type representing game state
 * 
 * @author didel
 *
 */
public enum GameStateEnum {
	FIRST_PLAYER_TURN(1,"FIRST_PLAYER_TURN"),
	SECOND_PLAYER_TURN(2,"SECOND_PLAYER_TURN"),
	GAME_OVER(3,"GAME_OVER");

	
    private final int key;
    private final String value;
	
	private GameStateEnum(int key, String value) {
        this.key= key ;
		this.value = value;
    }

	public String getValue() {
		return value;
	}
	
	public int getKey() {
		return key;
	}


}
