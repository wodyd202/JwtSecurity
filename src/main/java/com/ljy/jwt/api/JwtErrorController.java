package com.ljy.jwt.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ljy.jwt.api.model.JwtErrorResponse;
import com.ljy.jwt.exception.InvalidAccessTokenException;
import com.ljy.jwt.exception.InvalidRefreshTokenException;

@RestController
@RestControllerAdvice
public class JwtErrorController {
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<JwtErrorResponse> error(UsernameNotFoundException e) {
		return new ResponseEntity<>(JwtErrorResponse.create(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidAccessTokenException.class)
	public ResponseEntity<JwtErrorResponse> error(InvalidAccessTokenException e) {
		return new ResponseEntity<>(JwtErrorResponse.create(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidRefreshTokenException.class)
	public ResponseEntity<JwtErrorResponse> error(InvalidRefreshTokenException e) {
		return new ResponseEntity<>(JwtErrorResponse.create(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
}
