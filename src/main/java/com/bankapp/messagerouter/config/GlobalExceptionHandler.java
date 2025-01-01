package com.bankapp.messagerouter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.EOFException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EOFException.class)
    public ResponseEntity<String> handleEOFException(EOFException e) {
        log.warn("Client disconnected prematurely: {}", e.getMessage());
        return new ResponseEntity<>("Client disconnected prematurely", HttpStatus.BAD_REQUEST);
    }
}
