
package com.backbase.games.kalah.web.rest.controller;

import com.backbase.games.kalah.exception.GameNotFoundException;
import com.backbase.games.kalah.exception.GameException;
import com.backbase.games.kalah.exception.InvalidPitException;
import com.backbase.games.kalah.exception.InvalidPlayerException;
import com.backbase.games.kalah.web.rest.entity.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;


/**
 * Global error handler of the game
 * @author didel
 *
 */
@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private static final String detailsFormat = "[Error],[Message: %s],[Ip: %s],[Path: %s]";
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * @param exception the {@link Exception Exception.class} that thrown
	 * @param request {@link HttpServletRequest HttpServletRequest.class} that triggered the error 
	 * @return {@link ErrorResponse ErrorResponse.class} with error information
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllExceptions(Exception exception, HttpServletRequest request) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		if (exception instanceof GameException) {
			logger.warn(String.format(detailsFormat, exception.getMessage(), request.getRemoteHost(), request.getRequestURI()));
			if(exception instanceof GameNotFoundException) {
				httpStatus = HttpStatus.NOT_FOUND;
			}
			else if((exception instanceof InvalidPlayerException) || 
					(exception instanceof InvalidPitException) || (exception instanceof IllegalArgumentException)) {
				httpStatus= HttpStatus.BAD_REQUEST;
			}
		}
		else logger.error(exception.getMessage(), exception);
		
		return new ResponseEntity<>(new ErrorResponse(
				httpStatus.value(),
				httpStatus.getReasonPhrase(),
				exception.getClass().getName(),
				exception.getMessage(),
				request.getRequestURI()), httpStatus);
    } 
}
