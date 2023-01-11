package com.system.base.exception;

public class NoLoginRuntimeException extends RuntimeException {
    private String message;

    public NoLoginRuntimeException(String message) {
        super(message);
        this.message = message;
    }
}
