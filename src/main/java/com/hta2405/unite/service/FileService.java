package com.hta2405.unite.service;

import com.hta2405.unite.dto.FileDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileService {
    private final String BASE_DIR;

    public FileService(@Value("${file.upload.base-directory}") String BASE_DIR) {
        this.BASE_DIR = BASE_DIR;
    }

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

    public void downloadFile(String subDirectory, String fileUUID, String fileName, HttpServletResponse response) {
        Path filePath = Paths.get(BASE_DIR, subDirectory, fileUUID + "_" + fileName);
        if (fileUUID == null || fileUUID.isEmpty()) {
            filePath = Paths.get(BASE_DIR, subDirectory, fileName);
        }
        if (!Files.exists(filePath)) {
            throw new RuntimeException("File not found");
        }

        try {
            // 파일명을 URL 인코딩
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            encodedFileName = encodedFileName.replace("+", "%20"); // 공백 처리

            // 헤더 설정
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);

            // 파일 스트림 전송
            Files.copy(filePath, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException("File download failed", e);
        }
    }

    public void deleteFile(String fileUUID, String subDirectory, String originalFileName) {
        Path filePath = Paths.get(BASE_DIR, subDirectory, fileUUID + "_" + originalFileName);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            log.info("{}", e.getMessage());
        }
    }
}
