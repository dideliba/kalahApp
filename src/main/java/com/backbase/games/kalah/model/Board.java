/**
 * 
 */
package com.backbase.games.kalah.model;

import com.backbase.games.kalah.common.enums.PitTypeEnum;
import com.backbase.games.kalah.common.enums.PlayerEnum;
import com.backbase.games.kalah.common.util.GameUtils;

import static com.backbase.games.kalah.common.constants.GameConstants.FIRST_PLAYER_KALAH_ID;
import static com.backbase.games.kalah.common.constants.GameConstants.SECOND_PLAYER_KALAH_ID;
import com.backbase.games.kalah.exception.InvalidPitException;
import com.backbase.games.kalah.exception.InvalidPlayerException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
/**
 * Represents the Kalah game board
 * @author didel
 *
 */
public class Board {

	private static final Logger logger = LoggerFactory.getLogger(Board.class);

	private int totalPits;
	private List<Pit> pitList;
	/* used to initialize a Board in the beginning of the game */
	public Board(int totalPits, int stonesPerPit) {
		this.totalPits=totalPits;
		pitList=new ArrayList<>();
		for(int i=0;i<totalPits;i++) {
			pitList.add(GameUtils.createInitialPit(i+1, stonesPerPit)); //pit ids start from value 1
		}
	}
	
	/* used to initialize a Board from a current state of stones in the Pits */
	public Board(List<Integer> pitsStoneList) {
		pitList=new ArrayList<>();
		for(int i=0;i<pitsStoneList.size();i++) {
			pitList.add(GameUtils.createPit(i+1, pitsStoneList.get(i)));
		}
		this.totalPits=pitsStoneList.size();
	}

	/**
	 * @return the list of {@link Pit Pit.class} in the board
	 */
	public List<Pit> getPits() {
		return pitList;
	}
	
	/**
	 * @param pits the list of {@link Pit Pit.class} to set in the board
	 */
	public void setPits(List<Pit> pits) {
		pitList = pits;
	}
	
	/**
	 * @param id the starting Pit's identifier where this move started
	 * @param currentPlayer the player that performs the move
	 * @return the last pit of the move (i.e. where current player's last stone placed)
	 * @throws InvalidPitException when selected pit is not valid (i.e in [1-14]) or selected pit has no stones or when selected pit is of the opponent's or a kalah pit
	 */
	public Pit move(int id, PlayerEnum currentPlayer) {
		Pit selectedPit=null; //the current Pit
		int stonesToSow=0; //stones for this move that need to be sowed
		int stonesInPit=0; //holds the stones contained the current pit
		/* check that selected pit is valid (1-14)
		and selected pit has stones and selected pit is not an opponent's or a kalah */
		
		//arrayList elements are numbered starting from 0, while pitIds from 1
		int pitIdx=id-1;
		if(id<1 || id>totalPits ) {
			throw new InvalidPitException("Pit ID out of bounds");
		}
		selectedPit=pitList.get(pitIdx);
		stonesToSow=selectedPit.getStones();

		if(selectedPit.getPitType()==PitTypeEnum.KALAH_PIT || stonesToSow==0) {
			throw new InvalidPitException("Invalid Pit ID");
		}
		if(selectedPit.getPitOwner()!=currentPlayer) 
		{
			throw new InvalidPlayerException("Invalid Player, it is "+ currentPlayer + "'s turn");
		}
		selectedPit.setStones(0); //take the stones
		pitList.set(pitIdx, selectedPit);
		//sow the stones
		while(stonesToSow>0) {	
			pitIdx=(pitIdx+1) % totalPits; // update index to the next pit
			selectedPit=pitList.get(pitIdx);
			//we cannot place stones in opponent's kalah
			if(selectedPit.getPitOwner()!=currentPlayer && selectedPit.getPitType()==PitTypeEnum.KALAH_PIT) {
				continue;
			}
			stonesInPit=pitList.get(pitIdx).getStones();
			selectedPit.setStones(stonesInPit+1); //add stone to pit
			pitList.set(pitIdx, selectedPit); 

			stonesToSow--;
		}
		return pitList.get(pitIdx); //this is the pit where last stone landed
	}
	
	/**
	 * @param player the player to find its kalah id
	 * @return the kalah id
	 */
	private int getPlayerKalahId(PlayerEnum player) {
		if(player==PlayerEnum.FIRST_PLAYER) {
			return FIRST_PLAYER_KALAH_ID;
		}
		else if(player==PlayerEnum.SECOND_PLAYER) {
			return  SECOND_PLAYER_KALAH_ID;
		}
		else {return -1;}
	}
	
	/**
	 * @param player the owner of the Kalah 
	 * @return number of stones in Kalah of the player
	 */
	public int countStonesInKalah(PlayerEnum player) {
		int totalStones=0;
		totalStones=pitList.get(getPlayerKalahId(player)-1).getStones();
		return totalStones;
	}
	
	/**
	 * @param player whose side we going to count stones
	 * @return the number of stones on player's side (no kalah pit stones included)
	 */
	private int countStonesOnPlayerSide(PlayerEnum player) {
		int totalStonesOnSide=pitList.stream()
		.filter(pit -> (pit.getPitOwner()==player && pit.getPitType()!=PitTypeEnum.KALAH_PIT))
		.mapToInt(pit -> pit.getStones()).sum();
		
		return totalStonesOnSide;
	}
	
	/**
	 * @param player to whose side we going to check
	 * @return return true if player's side has no stones
	 */
	public boolean isPlayerSideEmpty(PlayerEnum player) {
		return countStonesOnPlayerSide(player)==0 ;
	}
	
	/**
	 * @param player the player whose stones will be moved to his kalah pit
	 */
	public void addAllStonesToKalah(PlayerEnum player) {
		Set<Integer> playersPitIds= GameUtils.getPlayersPitIds(player);
		int totalStones=0; //stones in player's pits
		int totalStonesInKalah=0;
		int kalahId=getPlayerKalahId(player);
		Pit currentPit=null;
		for(int id : playersPitIds) {
			if(id!=kalahId) {
				currentPit=pitList.get(id-1);
				totalStones+=currentPit.getStones();
				currentPit.setStones(0); //remove those stones
				pitList.set(id-1, currentPit);
			}
		}
		currentPit=pitList.get(kalahId-1); //get the kalah pit and place stones from other pits there
		totalStonesInKalah=currentPit.getStones();
		logger.debug("Kalah contained "+ totalStonesInKalah + ", adding "+ totalStones + " more");
		currentPit.setStones(totalStonesInKalah+totalStones);
		pitList.set(kalahId-1,currentPit);
	}
	
	/**
	 * @return {@link Map Map.class} representation (id,numberOfStones) of a board 
	 */
	public Map<Integer,Integer> convertBoardToMap(){
		Map<Integer,Integer> map= this.getPits().stream()
  		.collect(Collectors.toMap(Pit::getId,Pit::getStones));
  		
  		return map;
    }
	
	
	/**
	 * @param pitId the pit that was empty and last stone of a move landed
	 */
	public void reserveOpponentStones(int pitId) {
		Pit currentPit=pitList.get(pitId-1);
		Pit opponentPit=pitList.get(getOppositePitId(pitId)-1);
		
		//if opposite pit is empty -> do nothing
		if(opponentPit.getStones()==0) {
			logger.debug("Opponent has no stones to acquire");
			return;
		}
		addPitStonesToKalah(currentPit.getId(),currentPit.getPitOwner());
		addPitStonesToKalah(opponentPit.getId(),currentPit.getPitOwner());
	}
	
	/**
	 * @param pitId to find its opposite
	 * @return the pitId of the opposite pit
	 */
	private int getOppositePitId(int pitId) {
		return (pitId < totalPits/2)  ? (totalPits-2*pitId)+pitId : pitId-2*(pitId-totalPits/2);  
 	}
	
	/**
	 * @param pitId the pit to get stones from and move to kalah pit
	 * @param player the {@link PlayerEnum PlayerEnum.class} to whose kalah pit will be added
	 */
	private void addPitStonesToKalah(int pitId, PlayerEnum player) {
		int kalahId=getPlayerKalahId(player);
		int containedStones=0;
		Pit currentPit=pitList.get(pitId-1);
		Pit kalahPit=pitList.get(kalahId-1);
		logger.debug("Pit {} contained: "+currentPit.getStones()+ " while kalah pit contained "+ kalahPit.getStones(),currentPit.getId());
		containedStones=kalahPit.getStones();
		containedStones+=currentPit.getStones();
		currentPit.setStones(0);
		kalahPit.setStones(containedStones);
		pitList.set(kalahId-1,kalahPit);
		pitList.set(pitId-1,currentPit);
		logger.debug("Now kalah pit {} contains: "+kalahPit.getStones(),kalahPit.getId());
	}
}
