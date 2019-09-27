package com.sapient.league.exception;

public class DataNotFoundException extends RuntimeException {

    private String message;

    private String errorCode;

    public DataNotFoundException(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
