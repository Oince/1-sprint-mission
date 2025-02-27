package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

public class FileIOException extends ExpectedException {

  public FileIOException(String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }
}
