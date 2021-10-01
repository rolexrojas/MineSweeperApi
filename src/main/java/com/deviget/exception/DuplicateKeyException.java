package com.deviget.exception;

public class DuplicateKeyException extends Exception{

    public DuplicateKeyException(String message, Exception e) {
        super(message, e);
    }

    public DuplicateKeyException(String message) {
        super(message);
    }
}
