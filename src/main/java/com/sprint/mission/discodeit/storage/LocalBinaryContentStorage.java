package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.exception.FileIOException;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") Path root) {
    this.root = root;
  }

  @PostConstruct
  public void init() {
    if (!Files.exists(root)) {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        throw new FileIOException("저장 디렉토리 생성 실패");
      }
    }
  }

  @Override
  public UUID put(UUID id, byte[] data) {
    Path path = resolvePath(id);
    try {
      Files.write(path, data);
    } catch (IOException e) {
      throw new FileIOException("파일 생성 실패: " + path);
    }
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    Path path = resolvePath(id);
    try {
      return new ByteArrayInputStream(Files.readAllBytes(path));
    } catch (IOException e) {
      throw new FileIOException("파일 읽기 실패: " + path);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentResponse response) {
    Path path = resolvePath(response.id());
    try {
      UrlResource resource = new UrlResource("file:" + path);
      String contentDisposition = "attachment; filename=\"" + resource.getFilename() + "\"";
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
          .body(resource);
    } catch (MalformedURLException e) {
      throw new FileIOException("잘못된 URL. file:" + path);
    }
  }

  @Override
  public void delete(UUID id) {
    Path path = resolvePath(id);
    if (Files.exists(path)) {
      try {
        Files.delete(path);
      } catch (IOException e) {
        throw new FileIOException("파일 삭제 실패: " + path);
      }
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}