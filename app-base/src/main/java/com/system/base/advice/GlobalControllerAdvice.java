package com.system.base.advice;

import com.system.base.exception.NoLoginRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(NoLoginRuntimeException.class)
    public ResponseEntity handle(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(e.getMessage());
    }
}
