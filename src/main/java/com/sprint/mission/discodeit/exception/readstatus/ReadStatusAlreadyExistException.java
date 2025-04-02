package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class ReadStatusAlreadyExistException extends ReadStatusException {

  public ReadStatusAlreadyExistException(Map<String, Object> details) {
    super(ErrorCode.READ_STATUS_ALREADY_EXIST, details);
  }
}
