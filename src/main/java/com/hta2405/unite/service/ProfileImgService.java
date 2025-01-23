package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.mybatis.mapper.EmpMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ProfileImgService {
    private static final String DEFAULT_PROFILE_IMAGE = "profile_navy_round.png";
    private static final String REDIS_KEY_PREFIX = "profile:";
    private final RedisTemplate<String, String> redisTemplate;
    private final String FILE_DIR;
    private final FileService fileService;
    private final EmpMapper empMapper;

    public ProfileImgService(@Value("${profile.upload.directory}") String FILE_DIR,
                             FileService fileService,
                             EmpMapper empMapper,
                             RedisTemplate<String, String> redisTemplate) {
        this.FILE_DIR = FILE_DIR;
        this.fileService = fileService;
        this.empMapper = empMapper;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public ResponseEntity<Resource> getProfileImage(Emp emp) {
        String redisKey = REDIS_KEY_PREFIX + emp.getEmpId();

        // Redis 캐시에서 이미지 가져오기
        ResponseEntity<Resource> cachedResponse = getCachedImageFromRedis(redisKey);
        if (cachedResponse != null) {
            return cachedResponse;
        }

        // S3에서 이미지 다운로드
        String fileUUID = ObjectUtils.isEmpty(emp.getImgOriginal()) ? "" : emp.getImgUUID();
        String fileName = ObjectUtils.isEmpty(fileUUID) ? DEFAULT_PROFILE_IMAGE : empMapper.getImgOriginal(fileUUID);
        ResponseEntity<Resource> s3Response = fileService.downloadFile(FILE_DIR, fileUUID, fileName);

        try {
            byte[] imageData = Objects.requireNonNull(s3Response.getBody()).getInputStream().readAllBytes();

            // Redis에 이미지 저장
            cacheImage(redisKey, imageData);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(new ByteArrayResource(imageData));
        } catch (IOException e) {
            log.error("이미지 처리 실패", e);
            throw new RuntimeException("이미지 처리 중 오류 발생");
        }
    }

    public FileDTO insertProfileImg(MultipartFile file) {
        if (ObjectUtils.isEmpty(file)) {
            return new FileDTO();
        }
        return fileService.uploadFile(file, FILE_DIR);
    }

    @Transactional
    public FileDTO updateProfileImg(MultipartFile file, String beforeFileName, Emp emp) {
        String redisKey = REDIS_KEY_PREFIX + emp.getEmpId();

        // 기존 프로필 사진이 없는 경우
        if (ObjectUtils.isEmpty(emp.getImgOriginal())) {
            // 새로 업로드된 파일 없음
            if (ObjectUtils.isEmpty(file)) {
                return new FileDTO();
            }
            // 새로 업로드된 파일이 있는 경우
            FileDTO uploadedFile = fileService.uploadFile(file, FILE_DIR);
            cacheImage(redisKey, file);
            return uploadedFile;
        }

        // 기존 파일명이 변경되지 않은 경우
        if (beforeFileName != null) {
            return new FileDTO(emp.getImgOriginal(), emp.getImgPath(), emp.getImgType(), emp.getImgUUID());
        }

        // 업로드된 파일이 없는 경우 -> 기존 프로필 사진 삭제
        if (ObjectUtils.isEmpty(file)) {
            fileService.deleteFile(emp.getImgUUID(), FILE_DIR, emp.getImgOriginal());
            try {
                redisTemplate.delete(redisKey);
            } catch (Exception e) {
                log.error("Redis 서버 오류", e);
            }
            return new FileDTO();
        }

        // 기존 프로필 사진이 있고 새 파일로 변경된 경우
        fileService.deleteFile(emp.getImgUUID(), FILE_DIR, emp.getImgOriginal());
        FileDTO uploadedFile = fileService.uploadFile(file, FILE_DIR);
        cacheImage(redisKey, file);
        return uploadedFile;
    }

    private void cacheImage(String redisKey, MultipartFile file) {
        try {
            byte[] fileData = file.getBytes();
            redisTemplate.opsForValue().set(redisKey, Base64.getEncoder().encodeToString(fileData), 1, TimeUnit.HOURS);
            log.info("Redis 캐시 완료, key = {}", redisKey);
        } catch (Exception e) {
            log.error("Redis cache error: {}", redisKey, e);
        }
    }

    private void cacheImage(String redisKey,  byte[] imageData) {
        try {
            redisTemplate.opsForValue().set(redisKey, Base64.getEncoder().encodeToString(imageData), 1, TimeUnit.HOURS);
            log.info("Redis 캐시 완료, key = {}", redisKey);
        } catch (Exception e) {
            log.error("Redis cache error: {}", redisKey, e);
        }
    }

    private ResponseEntity<Resource> getCachedImageFromRedis(String redisKey) {
        try {
            String cachedData = redisTemplate.opsForValue().get(redisKey);
            if (cachedData != null) {
                log.info("Redis Cache hit : {}", redisKey);
                byte[] imageData = Base64.getDecoder().decode(cachedData);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(new ByteArrayResource(imageData));
            }
        } catch (Exception e) {
            log.error("Redis 서버 오류, key = {}", redisKey, e);
        }
        return null; // 캐시 미스
    }
}
