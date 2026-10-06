package com.ruco.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ruco.response.ErrorResponse;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(SignatureException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ErrorResponse buildErrorResponse(SignatureException exception) {

		String message = exception.getMessage();
		ErrorResponse response = new ErrorResponse(message);
		return response;

	}
	
	@ExceptionHandler(JwtException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ErrorResponse buildErrorResponse(JwtException exception) {

		String message = exception.getMessage();
		ErrorResponse response = new ErrorResponse(message);
		return response;

	}

}
