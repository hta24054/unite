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
import java.io.InputStream;
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

    public FileDTO insertProfileImg(MultipartFile file) {
        if (ObjectUtils.isEmpty(file)) {
            return new FileDTO();
        }
        return fileService.uploadFile(file, FILE_DIR);
    }

    @Transactional
    public FileDTO updateProfileImg(MultipartFile file, String beforeFileName, Emp emp) {
        String redisKey = REDIS_KEY_PREFIX + emp.getEmpId();
        //기존 프로필 사진 없음
        if (ObjectUtils.isEmpty(emp.getImgOriginal())) {
            //업로드된 파일 없음
            if (ObjectUtils.isEmpty(file)) {
                return new FileDTO();
            }
            //새로 프로필사진 업로드됨
            FileDTO uploadedFile = fileService.uploadFile(file, FILE_DIR);
            cacheProfileImage(redisKey, file);
            return uploadedFile;
        }
        //아래는 프로필사진 있는경우

        //기존 파일명이 파라미터로 옴 -> 파일이 변경되지 않음.
        if (beforeFileName != null) {
            return new FileDTO(emp.getImgOriginal(), emp.getImgPath(), emp.getImgType(), emp.getImgUUID());
        }

        //업로드된 사진이 없는경우 -> 프로필 사진 삭제
        if (ObjectUtils.isEmpty(file)) {
            fileService.deleteFile(emp.getImgUUID(), FILE_DIR, emp.getImgOriginal());
            redisTemplate.delete(redisKey);
            return new FileDTO(); //빈 객체
        }

        //프로필사진 있었고, 변경된 경우
        fileService.deleteFile(emp.getImgUUID(), FILE_DIR, emp.getImgOriginal());
        cacheProfileImage(redisKey, file); // 새 이미지로 Redis 캐시 업데이트
        return fileService.uploadFile(file, FILE_DIR);
    }

    @Transactional
    public ResponseEntity<Resource> getProfileImage(Emp emp) {
        String redisKey = REDIS_KEY_PREFIX + emp.getEmpId();
        String cachedData = redisTemplate.opsForValue().get(redisKey);

        if (cachedData != null) {
            log.info("Redis Cache hit {}", redisKey);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(new ByteArrayResource(Base64.getDecoder().decode(cachedData)));
        }

        log.info("Redis 캐시 miss, key = : {}, S3로부터 다운로드합니다.", redisKey);
        String fileUUID = ObjectUtils.isEmpty(emp.getImgUUID()) ? "" : emp.getImgUUID();
        String fileName = ObjectUtils.isEmpty(fileUUID) ? DEFAULT_PROFILE_IMAGE : empMapper.getImgOriginal(fileUUID);

        // 기존 S3 메서드 호출
        ResponseEntity<Resource> response = fileService.downloadFile(FILE_DIR, fileUUID, fileName);

        // InputStream 복사 후 Redis에 저장
        try (InputStream inputStream = Objects.requireNonNull(response.getBody()).getInputStream()) {
            byte[] imageData = inputStream.readAllBytes();

            // Redis 캐싱
            redisTemplate.opsForValue().set(redisKey, Base64.getEncoder().encodeToString(imageData), 24, TimeUnit.HOURS);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(new ByteArrayResource(imageData));
        } catch (IOException e) {
            log.error("Redis 캐싱 실패, key = {}", redisKey, e);
            throw new RuntimeException("프로필 이미지를 처리하는 중 오류가 발생했습니다.");
        }
    }

    // Redis 캐싱 메서드
    private void cacheProfileImage(String redisKey, MultipartFile file) {
        try {
            byte[] fileData = file.getBytes();
            redisTemplate.opsForValue().set(redisKey, Base64.getEncoder().encodeToString(fileData), 24, TimeUnit.HOURS);
            log.info("Redis 캐시 완료, key = : {}", redisKey);
        } catch (IOException e) {
            log.error("Redis 캐시 실패, key = : {}", redisKey, e);
        }
    }
}
