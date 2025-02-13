package com.hta2405.unite.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.hta2405.unite.dto.FileDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3FileService implements FileService {
    @Value("${cloud.aws.s3.base-directory}")
    private String BASE_DIR;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public FileDTO uploadFile(MultipartFile file, String subDirectory) {
        try {
            String fileUUID = String.valueOf(UUID.randomUUID());
            String filePath = subDirectory.substring(1) + "/" + fileUUID + "_" + file.getOriginalFilename();

            // S3 객체 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // S3에 파일 업로드
            amazonS3Client.putObject(bucket, filePath, file.getInputStream(), metadata);

            // 업로드된 파일의 URL 반환
            return new FileDTO(file.getOriginalFilename(), filePath, file.getContentType(), fileUUID);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed");
        }
    }


    public ResponseEntity<Resource> downloadFile(String subDirectory, String fileUUID, String fileName) {
        String filePath = subDirectory.substring(1) + "/" + fileUUID + "_" + fileName;
        log.info("filePath = {}", filePath);
        try {
            // S3에서 파일 가져오기
            S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, filePath));
            S3ObjectInputStream inputStream = s3Object.getObjectContent();

            // 파일 리소스로 변환
            InputStreamResource resource = new InputStreamResource(inputStream);

            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");

            // ResponseEntity를 사용하여 파일 다운로드
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName)
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public void deleteFile(String fileUUID, String subDirectory, String originalFileName) {
        String filePath = subDirectory.substring(1) + "/" + fileUUID + "_" + originalFileName;
        log.info("delete filePath = {}", filePath);
        amazonS3Client.deleteObject(bucket, filePath);
    }
}

