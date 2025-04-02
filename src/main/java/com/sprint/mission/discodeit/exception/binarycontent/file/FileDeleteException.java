package com.sprint.mission.discodeit.exception.binarycontent.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentException;
import java.util.Map;

public class FileDeleteException extends BinaryContentException {

  public FileDeleteException(Map<String, Object> details) {
    super(ErrorCode.FILE_DELETE_FAIL, details);
  }
}
