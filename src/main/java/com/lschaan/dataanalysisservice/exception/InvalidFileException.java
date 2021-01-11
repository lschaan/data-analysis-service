package com.lschaan.dataanalysisservice.exception;

public class InvalidFileException extends RuntimeException {

    private final String message;
    private Throwable cause;

    public InvalidFileException(String message) {
        this.message = message;
    }

    public InvalidFileException(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }

}
