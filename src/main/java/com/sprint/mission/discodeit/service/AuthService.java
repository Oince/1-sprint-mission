package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAuthenticationException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserResponse login(UserLoginRequest dto) {
    User user = userRepository.findByUsername(dto.username())
        .orElseThrow(() -> new UserNotFoundException(Map.of("username", dto.username())));

    String password = dto.password();
    if (!(user.getUsername().equals(dto.username()) && user.getPassword().equals(password))) {
      throw new UserAuthenticationException(
          Map.of("username", dto.username(), "password", password));
    }

    return userMapper.toDto(user);
  }
}
