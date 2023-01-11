package com.system.base.exception;

public class BusinessRuntimeException extends RuntimeException {
    private String message;

    public BusinessRuntimeException(String message) {
        super(message);
        this.message = message;
    }
}
