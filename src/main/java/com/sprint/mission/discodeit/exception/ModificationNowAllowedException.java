package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

public class ModificationNowAllowedException extends ExpectedException {

  public ModificationNowAllowedException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
