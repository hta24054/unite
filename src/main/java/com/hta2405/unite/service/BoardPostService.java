package com.hta2405.unite.service;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Post;
import com.hta2405.unite.dto.BoardDTO;
import com.hta2405.unite.dto.BoardPostEmpDTO;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.dto.PostDTO;
import com.hta2405.unite.mybatis.mapper.BoardPostMapper;
import com.hta2405.unite.util.ConfigUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardPostService {
    private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("post.upload.directory");
    private final BoardPostMapper boardPostMapper;
    private final EmpService empService;
    private final FileService fileService;

    public List<BoardPostEmpDTO> getBoardListAll(String empId) {
        Emp emp = empService.getEmpById(empId);
        Long deptId = emp.getDeptId();

        List<BoardPostEmpDTO> boardPostEmpDTOList = boardPostMapper.getBoardListAll(deptId);

        if (boardPostEmpDTOList.isEmpty()) {
            log.warn("No board data found for deptId: " + deptId);
        } else {
            log.info("Board data: {}", boardPostEmpDTOList);
        }

        log.info("결과 : " + boardPostEmpDTOList.isEmpty());
        return boardPostEmpDTOList;
    }

    @Transactional
    public boolean addPost(BoardDTO boardDTO, PostDTO postDTO, List<MultipartFile> files) {
        log.info("boardDTO: " + boardDTO.toString());

        // 1. 게시판 ID 가져오기
        Long boardId = boardPostMapper.findBoardIdByName1Name2(boardDTO);
        if (boardId == null) {
            throw new IllegalArgumentException("게시판 이름을 찾을 수 없습니다.");
        }

        Post post = Post.builder()
                .boardId(boardId)
                .postWriter(postDTO.getPostWriter())
                .postSubject(postDTO.getPostSubject())
                .postContent(postDTO.getPostContent()).build();


        // 2. 게시글 삽입
        int postInsert = boardPostMapper.insertPost(post);
        int update = boardPostMapper.refUpdate(post.getPostId());

        if (postInsert <= 0 || update != 1) {
            throw new RuntimeException("게시글 삽입 실패");
        }

        //파일업로드
        List<FileDTO> fileDTOList = new ArrayList<>();
        for (MultipartFile file : files) {
            fileDTOList.add(fileService.uploadFile(file, UPLOAD_DIRECTORY));
        }

        int num = 0;
        num += boardPostMapper.insertPostFile(post.getPostId(), fileDTOList);
        return num == files.size();
    }
}
