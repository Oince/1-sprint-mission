package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserNameDuplicateException extends UserException {

  public UserNameDuplicateException(Map<String, Object> details) {
    super(ErrorCode.DUPLICATE_USER_USERNAME, details);
  }
}
