package com.decoder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateApplicationException.class)
    public ResponseEntity<?> handleDuplicateApplication(DuplicateApplicationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            java.util.Collections.singletonMap("error", ex.getMessage())
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            java.util.Collections.singletonMap("error", ex.getMessage())
        );
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}

