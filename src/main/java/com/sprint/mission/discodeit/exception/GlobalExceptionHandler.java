package com.sprint.mission.discodeit.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> expectedException(DiscodeitException e) {
    log.error("error: ", e);
    ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), e.getDetails(),
        e.getClass().getSimpleName());
    return ResponseEntity.status(errorResponse.status()).body(errorResponse);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> methodArgumentNotValidException(
      MethodArgumentNotValidException e
  ) {
    Map<String, Object> details = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(fieldError -> {
      details.put(fieldError.getField(), fieldError.getDefaultMessage());
    });
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.VALIDATION_FAIL, details,
        e.getClass().getSimpleName());
    return ResponseEntity.status(errorResponse.status()).body(errorResponse);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> exception(RuntimeException e) {
    log.error("error: ", e);
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.UNKNOWN_EXCEPTION, Map.of(),
        e.getClass().getSimpleName());
    return ResponseEntity.status(errorResponse.status()).body(errorResponse);
  }
}
