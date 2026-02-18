package com.smarthire.custom_exceptions;

@SuppressWarnings("serial")
public class EmailAlreadyExistsException extends RuntimeException{
	public EmailAlreadyExistsException(String message) {
		super(message);
	}
}
