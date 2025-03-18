package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

  @Column(nullable = false)
  private String fileName;

  @Column(nullable = false)
  private Long size;

  @Column(nullable = false)
  private String contentType;

  @Builder(access = AccessLevel.PRIVATE)
  private BinaryContent(Long size, String fileName, String contentType) {
    this.size = size;
    this.fileName = fileName;
    this.contentType = contentType;
  }

  public static BinaryContent create(Long size, String fileName, String contentType) {
    return BinaryContent.builder()
        .size(size)
        .fileName(fileName)
        .contentType(contentType)
        .build();
  }
}
