package com.system.base.advice;

import com.system.base.exception.NoLoginRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.system")
public class GlobalExceptionResolver {
    /**
     * 处理未登录异常
     */
    @ExceptionHandler(NoLoginRuntimeException.class)
    public ResponseEntity handleNoLoginRuntimeException(NoLoginRuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(e.getMessage());
    }
}
