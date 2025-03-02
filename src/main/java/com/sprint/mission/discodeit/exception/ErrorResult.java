package com.sprint.mission.discodeit.exception;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Value
public class ErrorResult {

  int status;
  String message;
  String url;
  Instant timestamp;

  public ErrorResult(HttpStatus status, String message, String url) {
    this.status = status.value();
    this.message = message;
    this.url = url;
    this.timestamp = Instant.now();
  }
}
