package com.ruco.exception;

public class JwtException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public JwtException(String errMsg) {
		super(errMsg);
	}


}
