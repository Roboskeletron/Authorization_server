package com.roboskeletron.authentication_server.exception;

public class ParameterRequiredException extends RuntimeException {
    public ParameterRequiredException(String message){
        super(message);
    }
}
