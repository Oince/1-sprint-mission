package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends ExpectedException {

  public AuthenticationException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
