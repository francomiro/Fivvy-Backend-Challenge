package com.fivvy.backend.challenge.exception;

public class AcceptanceAlreadyExistsException extends RuntimeException{

    public AcceptanceAlreadyExistsException(String message){
        super(message);
    }
}
