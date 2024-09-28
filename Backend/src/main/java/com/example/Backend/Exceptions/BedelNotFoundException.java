package com.example.Backend.Exceptions;

public class BedelNotFoundException extends RuntimeException {
    public BedelNotFoundException(Long id) {
        super("No se encontró el Bedel con ID: " + id);
    }
}
