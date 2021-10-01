package com.deviget.exception;

public class NoGameFoundException extends Exception {

    public NoGameFoundException(String message, Exception e) {
        super(message, e);
    }

    public NoGameFoundException(String message) {
        super(message);
    }

}