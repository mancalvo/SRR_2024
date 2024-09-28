package com.example.Backend.Exceptions;

public class BedelAlreadyExistsException extends RuntimeException{
    public BedelAlreadyExistsException(String nombre) {
        super("Ya existe un Bedel con el nombre: " + nombre);
    }
}
