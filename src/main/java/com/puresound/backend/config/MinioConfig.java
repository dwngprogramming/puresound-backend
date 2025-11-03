package com.puresound.backend.config;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        log.info("Initializing MinIO Client...");
        log.info("Endpoint: {}", endpoint);

        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(
                            accessKey,
                            secretKey)
                    .build();

            log.info("MinIO Client initialized successfully!");
            return minioClient;
        } catch (Exception e) {
            log.error("Failed to initialize MinIO Client: {}", e.getMessage());
            throw new IllegalStateException("MinIO initialization failed", e);
        }
    }
}
