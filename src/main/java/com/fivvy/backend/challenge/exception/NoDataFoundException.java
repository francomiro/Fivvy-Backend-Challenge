package com.fivvy.backend.challenge.exception;

public class NoDataFoundException extends RuntimeException{
    public NoDataFoundException(String message){
        super(message);
    }
}
