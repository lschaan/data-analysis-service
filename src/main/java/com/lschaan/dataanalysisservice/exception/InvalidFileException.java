package com.lschaan.dataanalysisservice.exception;

import java.util.Objects;

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

    public String getMessage() {
        return this.message;
    }

    public Throwable getCause() {
        return this.cause;
    }

    @Override
    public String toString() {
        return Objects.toString(this);
    }
}
