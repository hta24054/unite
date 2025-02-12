package com.hta2405.unite.service;

import com.hta2405.unite.dto.FileDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileDTO uploadFile(MultipartFile file, String subDirectory);

    ResponseEntity<Resource> downloadFile(String subDirectory, String fileUUID, String fileName);

    void deleteFile(String fileUUID, String subDirectory, String originalFileName);

    long getFileSize(String subDirectory, String fileUUID, String fileName);
}
