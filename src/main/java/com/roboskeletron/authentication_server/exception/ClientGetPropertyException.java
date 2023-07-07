package com.roboskeletron.authentication_server.exception;

public class ClientGetPropertyException extends RuntimeException {
    private final String detailsMessage;

    public ClientGetPropertyException(String message, String detailsMessage){
        super(message);
        this.detailsMessage = detailsMessage;
    }

    public String getDetailsMessage() {
        return detailsMessage;
    }
}
