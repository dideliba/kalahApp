package com.backbase.games.kalah.web.rest.controller;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backbase.games.kalah.service.GameService;
import com.backbase.games.kalah.web.rest.entity.CreateResponse;
import com.backbase.games.kalah.web.rest.entity.MoveResponse;

/**
 * The REST controller implements the REST interface of the kalah game
 * @author didel
 *
 */
@RestController
@RequestMapping("/games")
public class GameRestController {

		@Autowired
		GameService gameService;
	    
		/**
		 * @param request the display games {@link HttpServletRequest HttpServletRequest.class} request
		 * @return a {@link List List.class} containing the games
		 */
		@ResponseStatus(HttpStatus.OK)
	    @RequestMapping(method = RequestMethod.GET)
	    public List<String> displayGames(HttpServletRequest request) {
	    	String url=request.getRequestURL().toString();
	    	//remove one or more forward slashes from that might have been inputed by user
	    	url = url.replaceAll("\\/*$", "");

	    	return gameService.fetchAllGames();
	    }
		
	    /**
	     * @param request the create game {@link HttpServletRequest HttpServletRequest.class} request
	     * @return {@link CreateResponse CreateResponse.class} as response
	     */
	    @ResponseStatus(HttpStatus.CREATED)
	    @RequestMapping(method = RequestMethod.POST)
	    public CreateResponse create(HttpServletRequest request) {
	    	int id=gameService.createGame();

	    	String url=request.getRequestURL().toString();
	    	//remove one or more forward slashes from that might have been inputed by user
	    	url = url.replaceAll("\\/*$", "");

	    	return new CreateResponse(id,url+"/"+id);
	    }
	    
	    /**
	     * @param request the move {@link HttpServletRequest HttpServletRequest.class} request
	     * @param gameId id of the game to perform the move
	     * @param pitId id of pit to start the move
	     * @return {@link MoveResponse MoveResponse.class} as response
	     */
	    @RequestMapping(method = RequestMethod.PUT,value="/{gameId}/pits/{pitId}")
	    public MoveResponse move(HttpServletRequest request, @PathVariable int gameId, @PathVariable int pitId) {
	    	Map<String, String> boardMap=gameService.move(gameId, pitId);
	    	String url=request.getRequestURL().toString();
	    	//remove one or more forward slashes from that might have been inputed by user
	    	url = url.replaceAll("\\/*$", "");
	    	
	    	return new MoveResponse(gameId, url, boardMap);
	    }
}
