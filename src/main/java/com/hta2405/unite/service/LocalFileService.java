package com.hta2405.unite.service;

import com.hta2405.unite.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

//@Service
@Slf4j
public class LocalFileService implements FileService {
    private final String BASE_DIR;

    public LocalFileService(@Value("${file.upload.base-directory}") String BASE_DIR) {
        this.BASE_DIR = BASE_DIR;
    }

    @Override
    public FileDTO uploadFile(MultipartFile file, String subDirectory) {
        String fileUUID = String.valueOf(UUID.randomUUID());
        String fileName = fileUUID + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(BASE_DIR, subDirectory, fileName);

        try {
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath.toFile());
            return new FileDTO(file.getOriginalFilename(), filePath.toString(), file.getContentType(), fileUUID);
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String subDirectory, String fileUUID, String fileName) {
        Path filePath = Paths.get(BASE_DIR, subDirectory, fileUUID + "_" + fileName);
        if (ObjectUtils.isEmpty(fileUUID)) {
            filePath = Paths.get(BASE_DIR, subDirectory, fileName);
        }

        if (!Files.exists(filePath)) {
            throw new RuntimeException("File not found");
        }

        try {
            Resource resource = new InputStreamResource(Files.newInputStream(filePath));

            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName)
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("File download failed", e);
        }
    }

    @Override
    public void deleteFile(String fileUUID, String subDirectory, String originalFileName) {
        Path filePath = Paths.get(BASE_DIR, subDirectory, fileUUID + "_" + originalFileName);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            log.info("{}", e.getMessage());
        }
    }
}
