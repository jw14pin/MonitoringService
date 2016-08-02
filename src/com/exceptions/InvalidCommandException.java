package com.exceptions;

/*
 * Used when there are no more tests for a machine to notify the client
 */
public class InvalidCommandException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7063166064153975484L;

	public InvalidCommandException() {
		super();
	}
	
	public InvalidCommandException(String message) {
		super(message);
	}
	
	public InvalidCommandException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
