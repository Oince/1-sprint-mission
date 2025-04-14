package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.exception.binarycontent.file.FileReadException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final S3Client s3Client;

  @Value("${discodeit.storage.s3.bucket}")
  private String bucketName;

  @Override
  public UUID put(UUID id, byte[] data) {
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(id.toString())
        .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(data));

    return id;
  }

  @Override
  public InputStream get(UUID id) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(id.toString())
        .build();

    return s3Client.getObject(getObjectRequest);
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentResponse response) {
    InputStream inputStream = get(response.id());
    try {
      Resource resource = new ByteArrayResource(inputStream.readAllBytes());

      return ResponseEntity
          .status(HttpStatus.OK)
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + response.id() + "\"")
          .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()))
          .body(resource);
    } catch (IOException e) {
      throw new FileReadException(Map.of("id", response.id()));
    }
  }
}
