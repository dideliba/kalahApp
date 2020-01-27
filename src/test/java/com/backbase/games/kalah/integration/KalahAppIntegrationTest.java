package com.backbase.games.kalah.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.backbase.games.kalah.KalahApplication;
import com.backbase.games.kalah.db.nosql.entity.GameDTO;
import com.backbase.games.kalah.service.GameService;
import com.backbase.games.kalah.db.nosql.repository.GameRepository;
import com.backbase.games.kalah.web.rest.entity.CreateResponse;
import com.backbase.games.kalah.web.rest.entity.MoveResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backbase.games.kalah.web.rest.entity.ErrorResponse;
import static com.backbase.games.kalah.common.constants.GameConstants.GAME_ID_INITIAL_VALUE;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
class KalahAppIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(KalahAppIntegrationTest.class);

	public static final String baseUrl= "http://localhost";
	
	//resulting status of board after first move
	Map<String, String> firstMoveMap = Stream.of(new Object[][] { 
	     {"1","0"},{"2","7"},{ "3","7"},{ "4","7"},{"5","7" },{"6","7"},{"7","1"}, 
	     {"8","6"},{"9","6"},{"10","6"},{ "11","6"},{"12","6"},{"13","6"},{"14","0"},
	     }).collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));
		
	@Autowired
	private GameService gameService;

	@Autowired
    private TestRestTemplate restTemplate;
	
	@LocalServerPort
    int randomServerPort;

	@Autowired
	GameRepository gameRepository;
	
	@BeforeEach
    public void initBeforeEachTest() {
		gameService.removeAllGames();
    }

	private ResponseEntity<CreateResponse> addGame() throws Exception {
		 final String url =  baseUrl+ ":" +randomServerPort+ "/games";
	     URI uri = new URI(url);
		 //create game
		 ResponseEntity<CreateResponse> response = restTemplate.exchange(uri, HttpMethod.POST, null, CreateResponse.class);

	     return response;
	}
	
	private ResponseEntity<ErrorResponse> makeWrongMove(int gameId, int pitId) throws Exception {
		 final String url =  baseUrl+ ":" +randomServerPort+ "/games/"+ gameId + "/pits/"+pitId;
		 URI uri = new URI(url);
		 //make the move
		 ResponseEntity<ErrorResponse> response = restTemplate.exchange(uri, HttpMethod.PUT, null, ErrorResponse.class);
		 
	     return response;
	}

	private ResponseEntity<MoveResponse> makeMove(int gameId, int pitId) throws Exception {
		 final String url =  baseUrl+ ":" +randomServerPort+ "/games/"+ gameId + "/pits/"+pitId;
		 
		 URI uri = new URI(url);
		 //make the move
		 ResponseEntity<MoveResponse> response = restTemplate.exchange(uri, HttpMethod.PUT, null, MoveResponse.class);

		 return response;
	}
	
	@Test 
	public void testGame_addFirstGame() throws Exception {
		 final String url =  baseUrl+ ":" +randomServerPort+ "/games";
		 ResponseEntity<CreateResponse> result=addGame();
	     //Verify request succeed and returned the appropriate response
	     assertNotNull(result);
	     assertEquals(HttpStatus.CREATED, result.getStatusCode());
	     assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());

	     //verify that first game added has the initial id
	     assertEquals(result.getBody().getId(), String.valueOf(GAME_ID_INITIAL_VALUE));
	     assertEquals(result.getBody().getUri(), url+"/"+GAME_ID_INITIAL_VALUE);
	}
	
	@Test 
	public void testGame_addTwoConsecutiveGames() throws Exception {
		 final String url =  baseUrl+ ":" +randomServerPort+ "/games";
	     //create first game
		 addGame();
	     //create second game
		 ResponseEntity<CreateResponse> result=addGame(); 
	     
	     //Verify request succeed and returned the appropriate response
	     assertNotNull(result);
	     assertEquals(HttpStatus.CREATED, result.getStatusCode());
	     assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());

	     //verify that first game added has the initial id+1
	     assertEquals(result.getBody().getId(), String.valueOf(GAME_ID_INITIAL_VALUE+1));
	     assertEquals(result.getBody().getUri(), url+"/"+(GAME_ID_INITIAL_VALUE+1));
	     assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());

	}
	
	@Test 
	public void testGame_verifyInvalidMove() throws Exception {
		 addGame();
		 //try to move in a existing game but the pit is second player's
		 ResponseEntity<ErrorResponse> result=makeWrongMove(GAME_ID_INITIAL_VALUE,11);
	     //Verify request succeed and returned the appropriate response
	     assertNotNull(result);
	     assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
	     assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());
	}
	
	@Test 
	public void testGame_verifyNotFoundGameMove() throws Exception {
		 addGame();
		 //try to move in a non existing game
		 ResponseEntity<ErrorResponse> result=makeWrongMove(GAME_ID_INITIAL_VALUE+1,1);
	     //Verify request succeed and returned the appropriate response
	     assertNotNull(result);
	     assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	     assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());
	}
	 
	@Test 
	public void testGame_verifyCorrectMove() throws Exception {
		 addGame();
		 //perform a valid move
		 ResponseEntity<MoveResponse> result=makeMove(GAME_ID_INITIAL_VALUE,1);
	     //Verify request succeed and returned the appropriate response
	     Map<String,String> map= result.getBody().getStatus();

	     //check if all elements between the two maps are equal
	     boolean allMatch=map.keySet().stream().allMatch(key -> map.get(key).equals(firstMoveMap.get(key)));
	     assertTrue(allMatch);
	     assertEquals(HttpStatus.OK, result.getStatusCode());
	     assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());
	}
	
	@Test 
	public void testGame_verifyError() throws Exception {
		//save an invalid game entity (i.e. no board element present)
		GameDTO gameDTO= new GameDTO(GAME_ID_INITIAL_VALUE);
        gameRepository.save(gameDTO);

		//perform a valid move via makeWrong move function in order to deserialize the error response
		ResponseEntity<ErrorResponse> result=makeWrongMove(GAME_ID_INITIAL_VALUE,1);
	    //Verify request failed and returned the appropriate response (500 error) 
	    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
	    assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());
	}
}