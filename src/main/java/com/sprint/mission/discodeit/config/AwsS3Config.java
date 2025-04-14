package com.sprint.mission.discodeit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {
  @Value("${discodeit.storage.s3.region}")
  private String region;
  @Value("${discodeit.storage.s3.accessKey}")
  private String accessKey;
  @Value("${discodeit.storage.s3.secretKey}")
  private String secretKey;

  @Bean
  public S3Client s3Client() {
    AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(() -> credentials)
        .build();
  }
}
