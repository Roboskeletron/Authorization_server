package com.roboskeletron.authentication_server.exception;

public class SamePasswordException extends RuntimeException {
    public SamePasswordException(){
        super("Passwords cant match");
    }
}
