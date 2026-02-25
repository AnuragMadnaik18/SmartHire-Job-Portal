package com.smarthire.custom_exceptions;

@SuppressWarnings("serial")
public class JobException extends RuntimeException {
	public JobException(String message) {
		super(message);
	}
}
