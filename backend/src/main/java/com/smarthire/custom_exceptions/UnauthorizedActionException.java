package com.smarthire.custom_exceptions;

@SuppressWarnings("serial")
public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
