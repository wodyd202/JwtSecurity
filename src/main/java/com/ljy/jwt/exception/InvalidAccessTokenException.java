package com.ljy.jwt.exception;

public class InvalidAccessTokenException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;

	public InvalidAccessTokenException(String msg) {
		super(msg);
	}
}
