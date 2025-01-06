package com.hta2405.unite.service;

import com.hta2405.unite.domain.Board;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardPostService {
    private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("post.upload.directory");
    private final BoardPostMapper boardPostMapper;
    private final EmpService empService;
    private final FileService fileService;

    public List<BoardHomeDeptDTO> getBoardListByEmpId(String empId) {
        Emp emp = empService.getEmpById(empId).orElseThrow(() -> new RuntimeException("Emp not found"));
        Long deptId = emp.getDeptId();

        return boardPostMapper.getBoardListByDeptId(deptId);
    }

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
        if(files != null && !files.isEmpty()) {
            List<FileDTO> fileDTOList = new ArrayList<>();
            for (MultipartFile file : files) {
                fileDTOList.add(fileService.uploadFile(file, UPLOAD_DIRECTORY));
            }

            int num = 0;
            num += boardPostMapper.insertPostFile(post.getPostId(), fileDTOList);
            return num > 0;
        }
        return true;
    }



    public HashMap<String, Object> getBoardListAndListCount(int page, int limit, BoardDTO boardDTO) {
        Long boardId = boardPostMapper.findBoardIdByName1Name2(boardDTO);
        if (boardId == null) {
            throw new IllegalArgumentException("게시판 이름을 찾을 수 없습니다.");
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("listCount", getListCountByBoardId(boardId));
        map.put("postList", getPostListByBoardId(page, limit, boardId));
        return map;
    }

    public int getListCountByBoardId(Long boardId) {
        return boardPostMapper.getListCountByBoardId(boardId);
    }

    public List<Post> getPostListByBoardId(int page, int endRow, Long boardId) {
        int startRow = (page - 1) * endRow;
        List<Post> postList = boardPostMapper.getPostListByBoardId(startRow, endRow, boardId);
        log.info("postList: " + postList.toString());
        return postList;
    }

    public HashMap<String, Object> getSearchListCountByBoardId(int page, int limit, BoardDTO boardDTO, String category, String query) {
        Long boardId = boardPostMapper.findBoardIdByName1Name2(boardDTO);
        if (boardId == null) {
            throw new IllegalArgumentException("게시판 이름을 찾을 수 없습니다.");
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("listCount", getSearchListCountByBoardId(boardId,category,query));
        map.put("postList", getSearchListByBoardId(page,limit,boardId,category,query));
        return map;
    }

    public int getSearchListCountByBoardId(Long boardId, String category, String query) {
        return boardPostMapper.getSearchListCountByBoardId(boardId,category,query);
    }

    public List<Post> getSearchListByBoardId(int page, int endRow, Long boardId, String category, String query) {
        int startRow = (page - 1) * endRow;
        List<Post> postList = boardPostMapper.getSearchListByBoardId(startRow,endRow,boardId,category,query);
        log.info("postList: " + postList.toString());
        return postList;
    }

    public void setReadCountUpdate(Long postId) {
        boardPostMapper.setReadCountUpdate(postId);
    }

    public PostDetailDTO getDetail(Long postId) {
        //Map<String, Object> detail = boardPostMapper.getDetail(postId);
        PostDetailDTO postDetailDTO = boardPostMapper.getDetail(postId);
        List<PostFile> postFiles = boardPostMapper.getDetailPostFile(postId);
        postDetailDTO.setPostFiles(postFiles);
//        List<Object> result = new ArrayList<>();
//        result.add(postDetailDTO.get("post")); // Post 객체
//        result.add(postDetailDTO.get("emp"));  // Emp 객체
//        result.add(postFiles);          // 파일 리스트

        return postDetailDTO;
    }
}
