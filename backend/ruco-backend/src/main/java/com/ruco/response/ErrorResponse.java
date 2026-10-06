package com.ruco.response;

public class ErrorResponse extends Exception {

	private static final long serialVersionUID = 1L;

	public ErrorResponse(String message) {
		super(message);
	}

}
