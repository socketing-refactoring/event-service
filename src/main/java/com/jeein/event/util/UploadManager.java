package com.jeein.event.util;

import com.jeein.event.exception.ErrorCode;
import com.jeein.event.exception.EventException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
public class UploadManager {
    private final ResourceLoader resourceLoader;

    @Value("${upload.path}")
    private String uploadPath;

    public String uploadFile(MultipartFile thumbnail, String title)
            throws MultipartException, IOException {
        validateFile(thumbnail);

        String newFileName = generateNewFileName(thumbnail, title);

        File uploadDirectory = resourceLoader.getResource("file://" + uploadPath).getFile();
        log.debug("uploadDirectory: {}", uploadDirectory);

        Path path = Paths.get(uploadDirectory.getAbsolutePath());
        Path savePath = path.resolve(newFileName);
        log.debug("savePath: {}", savePath);

        Files.copy(thumbnail.getInputStream(), savePath);
        log.info("File uploaded successfully with name: {}", newFileName);
        return newFileName;
    }

    private void validateFile(MultipartFile thumbnail) {
        if (thumbnail.isEmpty() || thumbnail.getSize() < 0) {
            throw new EventException(ErrorCode.INVALID_MULTIPARTFILE);
        }
        log.debug(thumbnail.getContentType());
    }

    private String generateNewFileName(MultipartFile thumbnail, String title) {
        String originalFileName = thumbnail.getOriginalFilename();
        log.debug("originalFilename: {}", originalFileName);

        int lastIndexOfDot = originalFileName.lastIndexOf(".");
        String extension = originalFileName.substring(lastIndexOfDot);
        String timestamp =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return String.format("%s_%s%s", title, timestamp, extension);
    }
}
