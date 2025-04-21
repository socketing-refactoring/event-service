package com.jeein.event;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@Slf4j
public class EventApplication {

    @Value("${upload.path}")
    private String uploadPathForTemp;

    public static void main(String[] args) {
        SpringApplication.run(EventApplication.class, args);
    }

    @PostConstruct
    public void init() throws IOException {
        Path path = Paths.get(uploadPathForTemp);

        if (Files.notExists(path)) {
            Files.createDirectories(path);
            log.info("디렉토리가 생성되었습니다: {}", uploadPathForTemp);
        } else {
            log.info("디렉토리가 이미 존재합니다: {}", uploadPathForTemp);
        }
    }
}
