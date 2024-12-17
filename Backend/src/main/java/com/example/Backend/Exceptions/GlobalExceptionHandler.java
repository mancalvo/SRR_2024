package com.example.Backend.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BedelException.class)
    public ResponseEntity<ErrorResponse> handleBedelException(BedelException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler(ReservaException.class)
    public ResponseEntity<ErrorResponse> handleReservaException(ReservaException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(),HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AulaException.class)
    public ResponseEntity<ErrorResponse> handleAulaException(AulaException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }


/*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse error = new ErrorResponse("Ocurrió un error inesperado.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        System.out.println(ex.getStackTrace());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

 */


}
