package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Post;
import com.hta2405.unite.domain.PostFile;
import com.hta2405.unite.dto.*;
import com.hta2405.unite.mybatis.mapper.BoardPostMapper;
import com.hta2405.unite.util.ConfigUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardPostService {
    private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("post.upload.directory");
    private final BoardPostMapper boardPostMapper;
    private final EmpService empService;

    public List<BoardPostEmpDTO> getBoardListAll(String empId) {
        Emp emp = empService.getEmpById(empId).orElseThrow(() -> new RuntimeException("Emp not found"));
        Long deptId = emp.getDeptId();

        List<BoardPostEmpDTO> boardPostEmpDTOList = boardPostMapper.getBoardListAll(deptId);

        if (boardPostEmpDTOList.isEmpty()) {
            log.warn("No board data found for deptId: " + deptId);
        } else {
            log.info("Board data: {}", boardPostEmpDTOList);
        }

        log.info("결과 : "+ boardPostEmpDTOList.isEmpty());
        return boardPostEmpDTOList;
    }

    @Transactional
    public boolean addPost(BoardDTO boardDTO, PostDTO postDTO, List<MultipartFile> files) {
        try {
            log.info("boardDTO: "+ boardDTO.toString());

            // 1. 게시판 ID 가져오기
            Long boardId = boardPostMapper.findBoardIdByName1Name2(boardDTO);
            if (boardId == null) {
                throw new IllegalArgumentException("게시판 이름을 찾을 수 없습니다.");
            }
            postDTO.setBoardId(boardId);

            // 2. 게시글 삽입
            int postInsert = boardPostMapper.insertPost(postDTO);
            if (postInsert <= 0) {
                throw new RuntimeException("게시글 삽입 실패");
            }

            // 3. 첨부파일 처리
            if (files != null && !files.isEmpty()) {
                List<PostFileDTO> postFileDTOList = new ArrayList<>();

                for (MultipartFile file : files) {
                    String originalFilename = file.getOriginalFilename();
                    String uuid = UUID.randomUUID().toString();
                    String fileName = uuid + "_" + originalFilename;

                    // 파일 저장
                    File uploadPath = new File(UPLOAD_DIRECTORY);
                    if (!uploadPath.exists() && !uploadPath.mkdirs()) {
                        throw new IOException("업로드 폴더를 생성할 수 없습니다: " + UPLOAD_DIRECTORY);
                    }

                    File savedFile = new File(uploadPath, fileName);
                    file.transferTo(savedFile);

                    // PostFile 객체 생성
                    PostFileDTO postFileDTO = new PostFileDTO();
                    postFileDTO.setPostFilePath(UPLOAD_DIRECTORY);
                    postFileDTO.setPostFileOriginal(originalFilename);
                    postFileDTO.setPostFileUUID(uuid);
                    postFileDTO.setPostFileType(file.getContentType());

                    postFileDTOList.add(postFileDTO);
                }

                // 첨부파일 삽입
                int filesInserted = postFile_insert(postFileDTOList, false);
                if (filesInserted != postFileDTOList.size()) {
                    throw new RuntimeException("첨부파일 삽입 실패");
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("게시글 등록 중 오류가 발생했습니다.", e);
        }
    }

    public int postFile_insert(List<PostFileDTO> postFileDTOList, boolean postIdCheck) {
        int num = 0;
        num += boardPostMapper.insertPostFile(postFileDTOList, postIdCheck);
        return num;
    }
}
