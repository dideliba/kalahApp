package com.backbase.games.kalah.model;



import com.backbase.games.kalah.common.enums.PitTypeEnum;
import com.backbase.games.kalah.common.enums.PlayerEnum;


/**
 * Class that represents  a pit
 * @author didel
 *
 */
public class Pit {
	private int id;
	private int stones;
	private PitTypeEnum pitType;
	private PlayerEnum pitOwner;
	
	 
	public Pit(int id, int stones, PitTypeEnum pitType, PlayerEnum pitOwner) {
		this.id=id;
		this.stones=stones;
		this.pitType=pitType;
		this.pitOwner=pitOwner;
	}
	
	/**
	 * @return pit's identifier
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id pit's identifier
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return current number of stones contained in pit
	 */
	public int getStones() {
		return stones;
	}
	/**
	 * @param stones current number of stones in pit
	 */
	public void setStones(int stones) {
		this.stones = stones;
	}
	/**
	 * @return the {@link PitTypeEnum PitTypeEnum.class} of pit
	 */
	public PitTypeEnum getPitType() {
		return pitType;
	}
	/**
	 * @param pitType {@link PitTypeEnum PitTypeEnum.class} of pit
	 */
	public void setPitType(PitTypeEnum pitType) {
		this.pitType = pitType;
	}
	/**
	 * @return the {@link PlayerEnum PlayerEnum.class} who owns the pit
	 */
	public PlayerEnum getPitOwner() {
		return pitOwner;
	}
	/**
	 * @param pitOwner the {@link PlayerEnum PlayerEnum.class} who owns the pit
	 */
	public void setPitOwner(PlayerEnum pitOwner) {
		this.pitOwner = pitOwner;
	}
}
