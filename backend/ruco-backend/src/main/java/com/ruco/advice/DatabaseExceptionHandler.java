package com.ruco.advice;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ruco.response.ErrorResponse;

@RestControllerAdvice
public class DatabaseExceptionHandler {

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ErrorResponse buildErrorResponse(SQLIntegrityConstraintViolationException exception) {

		String message = exception.getMessage();
		ErrorResponse response = new ErrorResponse(message);
		return response;

	}

}
