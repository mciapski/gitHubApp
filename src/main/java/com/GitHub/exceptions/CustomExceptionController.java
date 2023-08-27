package com.GitHub.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class CustomExceptionController {

    @ExceptionHandler(BadHeaderException.class)
    public ResponseEntity<ErrorResponse> handleException(BadHeaderException badHeaderException){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Wrong header received");
        errorResponse.setStatus(HttpStatus.NOT_ACCEPTABLE.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }


    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpClientErrorException badUserException){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Wrong owner name");
        errorResponse.setStatus(HttpStatus.NOT_FOUND.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
