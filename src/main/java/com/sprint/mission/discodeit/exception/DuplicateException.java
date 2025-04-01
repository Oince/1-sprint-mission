package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

public class DuplicateException extends DiscodeitException {

  public DuplicateException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
