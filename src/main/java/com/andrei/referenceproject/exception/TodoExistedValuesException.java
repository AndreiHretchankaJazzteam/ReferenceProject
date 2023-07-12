package com.andrei.referenceproject.exception;

public class TodoExistedValuesException extends RuntimeException {
    public TodoExistedValuesException(String message) {
        super(message);
    }
}
