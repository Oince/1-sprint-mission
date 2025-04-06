package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.dto.response.UserStatusResponse;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.user.UserNameDuplicateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  UserService userService;
  @MockitoBean
  UserStatusService userStatusService;

  @Test
  @DisplayName("createUserWithProfile")
  void createUserWithProfile() throws Exception {
    UserCreateRequest request = new UserCreateRequest("email@email.com", "username",
        "password");
    MockMultipartFile userCreateRequest = new MockMultipartFile("userCreateRequest", null,
        "application/json", objectMapper.writeValueAsBytes(request));
    MockMultipartFile profile = new MockMultipartFile("profile", "profile.jpg",
        "imege/jpeg", "file".getBytes());

    BinaryContentResponse binaryContentResponse = new BinaryContentResponse(UUID.randomUUID(),
        profile.getSize(), profile.getName(), profile.getContentType());

    UserResponse userResponse = new UserResponse(UUID.randomUUID(), request.username(),
        request.email(), binaryContentResponse, false);

    given(userService.createUser(any(), any())).willReturn(userResponse);

    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequest)
            .file(profile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userResponse.id().toString()))
        .andExpect(jsonPath("$.username").value(userResponse.username()));
  }

  @Test
  @DisplayName("createUser")
  void createUser() throws Exception {
    UserCreateRequest request = new UserCreateRequest("email@email.com", "username",
        "password");
    MockMultipartFile userCreateRequest = new MockMultipartFile("userCreateRequest", null,
        "application/json", objectMapper.writeValueAsBytes(request));

    UserResponse userResponse = new UserResponse(UUID.randomUUID(), request.username(),
        request.email(), null, false);

    given(userService.createUser(any(), any())).willReturn(userResponse);

    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequest)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userResponse.id().toString()))
        .andExpect(jsonPath("$.username").value(userResponse.username()));
  }

  @Test
  @DisplayName("createUser 실패 - 이름이 중복된 경우")
  void failCreateUser() throws Exception {
    UserCreateRequest request = new UserCreateRequest("email@email.com", "username",
        "password");
    MockMultipartFile userCreateRequest = new MockMultipartFile("userCreateRequest", null,
        "application/json", objectMapper.writeValueAsBytes(request));

    UserResponse userResponse = new UserResponse(UUID.randomUUID(), request.username(),
        request.email(), null, false);

    given(userService.createUser(any(), any())).willThrow(new UserNameDuplicateException(Map.of()));

    mockMvc.perform(multipart("/api/users")
            .file(userCreateRequest)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_USER_USERNAME.getMessage()));
  }

  @Test
  @DisplayName("getUsers")
  void getUsers() throws Exception {

    UserResponse userResponse1 = new UserResponse(UUID.randomUUID(), "username1",
        "email1@email.com", null, false);
    UserResponse userResponse2 = new UserResponse(UUID.randomUUID(), "username2",
        "email2@email.com", null, false);

    given(userService.readAll()).willReturn(List.of(userResponse1, userResponse2));

    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2)) // JSON 배열 크기
        .andExpect(jsonPath("$[0].username").value("username1"))
        .andExpect(jsonPath("$[1].email").value("email2@email.com"));
  }

  @Test
  @DisplayName("updateUserWithProfile")
  void updateUserWithProfile() throws Exception {
    UserUpdateRequest request = new UserUpdateRequest("email@email.com", "username",
        "password");
    MockMultipartFile userUpdateRequest = new MockMultipartFile("userUpdateRequest", null,
        "application/json", objectMapper.writeValueAsBytes(request));
    MockMultipartFile profile = new MockMultipartFile("profile", "profile.jpg",
        "imege/jpeg", "file".getBytes());

    BinaryContentResponse binaryContentResponse = new BinaryContentResponse(UUID.randomUUID(),
        profile.getSize(), profile.getName(), profile.getContentType());

    UserResponse userResponse = new UserResponse(UUID.randomUUID(), request.newUsername(),
        request.newEmail(), binaryContentResponse, false);

    given(userService.updateUser(any(), any(), any())).willReturn(userResponse);

    mockMvc.perform(multipart("/api/users/" + userResponse.id())
            .file(userUpdateRequest)
            .file(profile)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userResponse.id().toString()))
        .andExpect(jsonPath("$.username").value(userResponse.username()));
  }

  @Test
  @DisplayName("updateUser 실패 - id가 없는 경우")
  void failUpdateUser() throws Exception {
    UserUpdateRequest request = new UserUpdateRequest("email@email.com", "username",
        "password");
    MockMultipartFile userUpdateRequest = new MockMultipartFile("userUpdateRequest", null,
        "application/json", objectMapper.writeValueAsBytes(request));

    UserResponse userResponse = new UserResponse(UUID.randomUUID(), request.newUsername(),
        request.newEmail(), null, false);

    given(userService.updateUser(any(), any(), any())).willThrow(
        new UserNotFoundException(Map.of()));

    mockMvc.perform(multipart("/api/users/" + userResponse.id())
            .file(userUpdateRequest)
            .with(req -> {
              req.setMethod("PATCH");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("updateUserOnline")
  void updateUserOnline() throws Exception {
    UUID userId = UUID.randomUUID();
    UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(
        Instant.now().minusSeconds(10));

    UserStatusResponse userStatusResponse = new UserStatusResponse(
        UUID.randomUUID(),
        userId,
        userStatusUpdateRequest.newLastActiveAt()
    );

    given(userStatusService.update(any(), any())).willReturn(userStatusResponse);

    mockMvc.perform(patch("/api/users/" + userId + "/userStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userStatusUpdateRequest))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userStatusResponse.id().toString()))
        .andExpect(jsonPath("$.lastActiveAt").value(userStatusResponse.lastActiveAt().toString()));
  }

  @Test
  @DisplayName("updateUserOnline 실패 - id가 없는 경우")
  void failUpdateUserOnline() throws Exception {
    UUID userId = UUID.randomUUID();
    UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(
        Instant.now().minusSeconds(10));

    UserStatusResponse userStatusResponse = new UserStatusResponse(
        UUID.randomUUID(),
        userId,
        userStatusUpdateRequest.newLastActiveAt()
    );

    given(userStatusService.update(any(), any())).willThrow(new UserNotFoundException(Map.of()));

    mockMvc.perform(patch("/api/users/" + userId + "/userStatus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userStatusUpdateRequest))
        )
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("deleteUser")
  void deleteUser() throws Exception {
    UUID userId = UUID.randomUUID();

    mockMvc.perform(delete("/api/users/" + userId))
        .andExpect(status().isNoContent());
  }
}