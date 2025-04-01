package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends DiscodeitException {

  public AuthenticationException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
