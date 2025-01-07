package com.hta2405.unite.service;

import com.hta2405.unite.domain.Board;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Post;
import com.hta2405.unite.domain.PostFile;
import com.hta2405.unite.dto.*;
import com.hta2405.unite.mybatis.mapper.BoardPostMapper;
import com.hta2405.unite.util.ConfigUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardPostService {
    private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("post.upload.directory");
    private final BoardPostMapper boardPostMapper;
    private final EmpService empService;
    private final FileService fileService;

    public List<BoardHomeDeptDTO> getBoardListByEmpId(String empId) {
        Emp emp = empService.getEmpById(empId);
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
        PostDetailDTO postDetailDTO = boardPostMapper.getDetail(postId);
        List<PostFile> postFiles = boardPostMapper.getDetailPostFile(postId);
        postDetailDTO.setPostFiles(postFiles);
        return postDetailDTO;
    }

    @Transactional
    public boolean modifyPost(BoardDTO boardDTO, PostModifyDTO postModifyDTO,
                              List<MultipartFile> addFiles, List<String> deletedFiles) {
        Long postId = postModifyDTO.getPostId();
        PostDetailDTO postDetailDTO = getDetail(postId);

        postDetailDTO.setBoardId(boardPostMapper.findBoardIdByName1Name2(boardDTO));
        postDetailDTO.setPostSubject(postModifyDTO.getPostSubject());
        postDetailDTO.setPostContent(postModifyDTO.getPostContent());

        Long boardId = boardPostMapper.findBoardIdByName1Name2(boardDTO);

        Post post = Post.builder()
                .postId(postId)
                .boardId(boardId)
                .postSubject(postModifyDTO.getPostSubject())
                .postContent(postModifyDTO.getPostContent()).build();

        // 2. 게시글 업데이트
        int postUpdate = boardPostMapper.updatePost(post);

        if (postUpdate <= 0) {
            throw new RuntimeException("게시글 수정 실패");
        }

        // 3. 파일업로드
        //파일 삭제
        if (deletedFiles != null && !deletedFiles.isEmpty()) {
            List<String> deletePostFileUUIDList = new ArrayList<>();

            for (String file : deletedFiles) {
                String fileUUID = null;
                String originalFileName = null;
                // MultipartFile의 내용을 문자열로 읽음
                String fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);

                // JSON 데이터에서 postFileUUID와 originalFileName 추출 (정규식 활용)
                Pattern pattern = Pattern.compile("\"postFileUUID\":\"([a-f0-9\\-]+)\"");
                Matcher matcher = pattern.matcher(fileContent);

                Pattern pattern2 = Pattern.compile("\"name\":\"([^\"]+)\"");
                Matcher matcher2 = pattern2.matcher(fileContent);

                if (matcher.find()) {
                    fileUUID = matcher.group(1); // 추출한 postFileUUID
                }
                if (matcher2.find()) {
                    originalFileName = matcher2.group(1); // 추출한 originalFileName
                }
                deletePostFileUUIDList.add(fileUUID);

                // 삭제 파일 DTO 생성 및 리스트에 추가
                fileService.deleteFile(fileUUID, UPLOAD_DIRECTORY, originalFileName);
            }
            int deleteCount = boardPostMapper.deletePostFile(deletePostFileUUIDList);
            if (deleteCount <= 0) {
                throw new RuntimeException("게시글 첨부파일 삭제 에러");
            }
        }

        //파일 추가
        if(addFiles != null && !addFiles.isEmpty()) {
            List<FileDTO> fileDTOList = new ArrayList<>();
            for (MultipartFile file : addFiles) {
                fileDTOList.add(fileService.uploadFile(file, UPLOAD_DIRECTORY));
            }

            int num = 0;
            num += boardPostMapper.insertPostFile(post.getPostId(), fileDTOList);
            return num > 0;
        }

        return true;
    }

    public PostFile getPostFile(Long postFileId) {
        return boardPostMapper.getPostFile(postFileId);
    }

    public void postFileDown(PostFile postFile, HttpServletResponse response) {
        fileService.downloadFile(UPLOAD_DIRECTORY, postFile.getPostFileUUID(), postFile.getPostFileOriginal(), response);
    }

    public boolean postDelete(int postId) {
        // 게시글 정보 조회
        HashMap<String, Integer> map = boardPostMapper.selectPostInfo(postId);

        if (map == null) {
            throw new RuntimeException("게시글 정보 조회 에러");
        }

        // 게시글 삭제
        int result = boardPostMapper.deletePost(map.get("post_re_ref"),
                                                map.get("post_re_lev"),
                                                map.get("post_re_seq"));
        return result >= 1; // 삭제 성공 여부 반환
    }
}
