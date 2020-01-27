package com.backbase.games.kalah.common.util;

import static com.backbase.games.kalah.common.constants.GameConstants.FIRST_PLAYER_KALAH_ID;
import static com.backbase.games.kalah.common.constants.GameConstants.SECOND_PLAYER_KALAH_ID;
import static com.backbase.games.kalah.common.constants.GameConstants.TOTAL_NUMBER_PITS;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.backbase.games.kalah.common.enums.GameStateEnum;
import com.backbase.games.kalah.common.enums.PitTypeEnum;
import com.backbase.games.kalah.common.enums.PlayerEnum;
import com.backbase.games.kalah.model.Board;
import com.backbase.games.kalah.model.Pit;
 
/**
 * Utility class containing static methods used by game modules
 * to perform simple game related tasks
 * 
 * @author didel
 *
 */
public final class GameUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(GameUtils.class); 
	
	private static Set<Integer> firstPlayerPitSet; //will contain player1 pit IDs
	private static Set<Integer> secondPlayerPitSet; //will contain player2 pit IDs
	
	/* this will be initialized once, during class loading */
	static {
		firstPlayerPitSet=new HashSet<>();
		secondPlayerPitSet=new HashSet<>();
		for(int i=1;i<=TOTAL_NUMBER_PITS;i++) {
			if(i<=FIRST_PLAYER_KALAH_ID) { 
				firstPlayerPitSet.add(i);
			}
			else {secondPlayerPitSet.add(i);}
		}
	}
	
    /**
     * @param e the {@link GameStateEnum GameStateEnum.class} to convert
     * @return the integer representation of the enum 
     * @throws IllegalArgumentException when provided input is null
     */
   public static int convertStateEnumToInt(GameStateEnum e) {
    	if(e==null) {
    		logger.error("Invalid input provided provided in method convertStateEnumToInt");
    		throw new IllegalArgumentException();
    	}
        return e.getKey();
    }

    /**
     * @param key the int representing the state
     * @return {@link GameStateEnum GameStateEnum.class} represented by given input
     * @throws IllegalArgumentException when provided key is less or equal to zero
     */
    public static GameStateEnum convertIntToStateEnum(int key) {
    	if(key<=0) {
    		logger.error("Invalid input provided in method convertIntToStateEnum");
    		throw new IllegalArgumentException("Invalid state id input provided");
    	}
    	return GameStateEnum.values()[key-1];
    }
    
    /**
     * @param map the input {@link Map Map.class} with (pitId,#stones) as entries
     * @return the {@link Board Board.class} representation of the input
     * @throws IllegalArgumentException when provided input is null
     */
    public static Board convertMapToBoard(Map<Integer,Integer> map){
    	if(map==null) {
    		logger.error("Invalid input provided provided in method convertMapToBoard");
    		throw new IllegalArgumentException("Map provided is null");
    	}
    	List <Integer> pitList=new ArrayList<>();
    	pitList = map.entrySet()
        		.stream()
                .sorted(Map.Entry.comparingByKey()) 
                .map(x -> x.getValue())
        	    .collect(Collectors.toCollection(ArrayList::new));
  		
  		return new Board(pitList);
    }
    
     /**
     * @param pitId the pitId
     * @return the {@link PlayerEnum PlayerEnum.class} that owns this pit or null if invalid input
     */
    public static PlayerEnum getPitOwnerByPitId(int pitId) {
    	 if(firstPlayerPitSet.contains(pitId)) {
    		 return PlayerEnum.FIRST_PLAYER;
    	 }
    	 else if (secondPlayerPitSet.contains(pitId)) {
    		 return PlayerEnum.SECOND_PLAYER;
    	 }
    	 else {
    		 return null;
    	 }
     }
     
     /**
     * @param player the player 
     * @return a Set containing the pit IDs of specific player or null if invalid input 
     */
    public static Set<Integer> getPlayersPitIds(PlayerEnum player){
    	 if(player==PlayerEnum.FIRST_PLAYER) return firstPlayerPitSet;
    	 else if(player==PlayerEnum.SECOND_PLAYER) return secondPlayerPitSet;
    	 else return null;
     }
     
     /**
     * @param pitId we want to identify
     * @return the {@link PlayerEnum PlayerEnum.class} of specific pit or null if invalid input
     */
    public static PitTypeEnum getPitTypeByPitId(int pitId) {
    	 if(pitId==FIRST_PLAYER_KALAH_ID || pitId==SECOND_PLAYER_KALAH_ID) {
    		 return PitTypeEnum.KALAH_PIT;
    	 }
    	 else if(pitId>=1 && pitId<TOTAL_NUMBER_PITS) {
    		 return PitTypeEnum.REGULAR_PIT;
    	 }
    	 else {
    		 return null;
    	 }
     }
      
     /**
     * @param pitId the id to set in pit
     * @param stonesPerPit stones to add to specific pit
     * @return the newly {@link Pit Pit.class} created or null
     */
    public static Pit createInitialPit(int pitId, int stonesPerPit) {
    	if(getPitTypeByPitId(pitId)==PitTypeEnum.KALAH_PIT) {
 			return new Pit(pitId, 0, PitTypeEnum.KALAH_PIT, getPitOwnerByPitId(pitId));
    	}
    	else if(getPitTypeByPitId(pitId)==PitTypeEnum.REGULAR_PIT) {
    		return new Pit(pitId, stonesPerPit, PitTypeEnum.REGULAR_PIT, getPitOwnerByPitId(pitId));
    	}
    	else {
    		logger.error("Invalid Pit id provided");
 			return null;
    	}
     }
     
    /**
     * @param pitId the id to set in pit
     * @param stonesPerPit stones to add to specific pit
     * @return the newly {@link Pit Pit.class} created or null
     */
    public static Pit createPit(int pitId, int stonesPerPit) {
     	if(getPitTypeByPitId(pitId)==PitTypeEnum.KALAH_PIT) {
  			return new Pit(pitId, stonesPerPit, PitTypeEnum.KALAH_PIT, getPitOwnerByPitId(pitId));
     	}
     	else if(getPitTypeByPitId(pitId)==PitTypeEnum.REGULAR_PIT) {
     		return new Pit(pitId, stonesPerPit, PitTypeEnum.REGULAR_PIT, getPitOwnerByPitId(pitId));
     	}
     	else {
     		logger.error("Invalid Pit id provided");
  			return null;
     	}
      }
    
}