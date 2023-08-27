package com.GitHub.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class BadHeaderException extends RuntimeException {


    public BadHeaderException() {
        super();
    }
}
