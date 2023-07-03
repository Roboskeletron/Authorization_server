package com.roboskeletron.authentication_server.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvalidPasswordException extends RuntimeException {
    private static String message;

    public InvalidPasswordException(){
        super(message);
    }

    @Value("${spring.security.password.error-message}")
    private void readConfig(String message){
        InvalidPasswordException.message = message;
    }
}
