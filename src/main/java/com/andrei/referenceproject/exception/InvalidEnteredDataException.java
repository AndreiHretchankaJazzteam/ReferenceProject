package com.andrei.referenceproject.exception;

public class InvalidEnteredDataException  extends RuntimeException {
    public InvalidEnteredDataException(String message) {
        super(message);
    }
}