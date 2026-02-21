package com.smarthire.custom_exceptions;

@SuppressWarnings("serial")
public class CompanyNotFoundException extends RuntimeException {
	public CompanyNotFoundException(String message) {
		super(message);
	}
}
 