package com.fivvy.backend.challenge.exception;

public class MissingFieldsException extends RuntimeException{

    public MissingFieldsException(String message){
        super(message);
    }
}
