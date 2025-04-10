package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserAuthenticationException extends DiscodeitException {

  public UserAuthenticationException(Map<String, Object> details) {
    super(ErrorCode.AUTHENTICATION_FAIL, details);
  }
}
