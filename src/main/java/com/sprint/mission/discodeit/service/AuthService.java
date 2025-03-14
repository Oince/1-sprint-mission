package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserLoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.AuthenticationException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  public User login(UserLoginRequest dto) {
    User user = userRepository.findByUsername(dto.username())
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. username=" + dto.username()));

    String password = dto.password();
    if (!(user.getUsername().equals(dto.username()) && user.getPassword().equals(password))) {
      throw new AuthenticationException("로그인 실패");
    }

    return user;
  }
}
