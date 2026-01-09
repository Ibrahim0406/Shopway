package com.shopway.shopway.errorhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
   /*
    * Globalni handler za hvatanje izuzetaka vezanih za nečitljive HTTP poruke.
    */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleBadRequest(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}

