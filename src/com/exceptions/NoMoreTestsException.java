package com.exceptions;

/*
 * Used when there are no more tests for a machine to notify the client
 */
public class NoMoreTestsException extends Exception {

	private static final long serialVersionUID = -7042265010636788967L;
	
	public NoMoreTestsException() {
		super();
	}
	
	public NoMoreTestsException(String message) {
		super(message);
	}
	
	public NoMoreTestsException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
