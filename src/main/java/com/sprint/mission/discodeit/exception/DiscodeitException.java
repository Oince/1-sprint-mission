package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DiscodeitException extends RuntimeException {

  private final HttpStatus status;

  public DiscodeitException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}
