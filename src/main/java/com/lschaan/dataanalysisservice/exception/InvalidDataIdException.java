package com.lschaan.dataanalysisservice.exception;

public class InvalidDataIdException extends RuntimeException {

    private final String message;

    public InvalidDataIdException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
