package com.sprint.mission.discodeit.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> expectedException(ExpectedException e, HttpServletRequest request) {
        ErrorResult errorResult = new ErrorResult(e.getStatus(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(e.getStatus()).body(errorResult);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> exception(RuntimeException e, HttpServletRequest request) {
        e.printStackTrace();
        ErrorResult errorResult = new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }
}
