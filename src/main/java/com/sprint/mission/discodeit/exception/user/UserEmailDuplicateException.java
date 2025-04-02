package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserEmailDuplicateException extends UserException {

  public UserEmailDuplicateException(Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_USER_EMAIL, details);
  }
}
