package com.example.Backend.Exceptions;

public class InvalidBedelDataException extends RuntimeException {
    public InvalidBedelDataException(String message) {
        super(message);
    }
}
