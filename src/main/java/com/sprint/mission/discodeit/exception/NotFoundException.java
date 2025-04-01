package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends DiscodeitException {

  public NotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}
