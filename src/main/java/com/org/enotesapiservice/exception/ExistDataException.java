package com.org.enotesapiservice.exception;

public class ExistDataException extends RuntimeException{

    public ExistDataException(String message) {
        super(message);
    }
}
