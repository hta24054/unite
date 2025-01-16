package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.mybatis.mapper.EmpMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@Slf4j
public class ProfileImgService {
    private static final String DEFAULT_PROFILE_IMAGE = "profile_navy_round.png";
    private final String FILE_DIR;
    private final FileService fileService;
    private final EmpMapper empMapper;

    public ProfileImgService(@Value("${profile.upload.directory}") String FILE_DIR,
                             FileService fileService,
                             EmpMapper empMapper) {
        this.FILE_DIR = FILE_DIR;
        this.fileService = fileService;
        this.empMapper = empMapper;
    }

    public FileDTO insertProfileImg(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new FileDTO();
        }
        return fileService.uploadFile(file, FILE_DIR);
    }

    @Transactional
    public FileDTO updateProfileImg(MultipartFile file, String beforeFileName, Emp emp) {

        //기존 프로필 사진 없음
        if (emp.getImgOriginal() == null || emp.getImgOriginal().isEmpty()) {
            //업로드된 파일 없음
            if (file == null || file.isEmpty()) {
                return new FileDTO(); //빈 객체
            }
            //프로필사진 등록됨
            return fileService.uploadFile(file, FILE_DIR);
        }
        //아래는 프로필사진 있는경우

        //업로드된 사진이 없는경우 -> 프로필 사진 삭제
        if (file == null || file.isEmpty()) {
            fileService.deleteFile(emp.getImgUUID(), FILE_DIR, emp.getImgOriginal());
            return new FileDTO(); //빈 객체
        }

        //기존 파일명이 파라미터로 옴 -> 파일이 변경되지 않음.
        if (beforeFileName != null) {
            return new FileDTO(emp.getImgOriginal(), emp.getImgPath(), emp.getImgType(), emp.getImgUUID());
        }

        //프로필사진 있었고, 변경된 경우
        fileService.deleteFile(emp.getImgUUID(), FILE_DIR, emp.getImgOriginal());
        return fileService.uploadFile(file, FILE_DIR);
    }

    @Transactional
    public ResponseEntity<Resource> getProfileImage(Emp emp) {
        String fileUUID = emp.getImgUUID();
        if (fileUUID == null || fileUUID.isEmpty()) {
            return fileService.downloadFile(FILE_DIR, fileUUID, DEFAULT_PROFILE_IMAGE);
        } else {
            String originalFileName = empMapper.getImgOriginal(fileUUID);
            return fileService.downloadFile(FILE_DIR, fileUUID, Objects.requireNonNullElse(originalFileName, DEFAULT_PROFILE_IMAGE));
        }
    }
}
