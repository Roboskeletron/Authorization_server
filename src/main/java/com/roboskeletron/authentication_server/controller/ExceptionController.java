package com.roboskeletron.authentication_server.controller;

import com.roboskeletron.authentication_server.exception.ErrorResponse;
import com.roboskeletron.authentication_server.exception.InvalidPasswordException;
import com.roboskeletron.authentication_server.exception.SamePasswordException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFound(EntityNotFoundException exception){
        ErrorResponse errorResponse = createErrorResponse(exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> invalidPassword(InvalidPasswordException exception){
        ErrorResponse errorResponse = createErrorResponse(exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unhandledException(Exception exception){
        ErrorResponse errorResponse = createErrorResponse(exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<ErrorResponse> samePasswordException(SamePasswordException exception){
        ErrorResponse errorResponse = createErrorResponse(exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse createErrorResponse(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        log.error(exception.getMessage(), exception);
        return errorResponse;
    }
}
