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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public HashMap<String, Object> getBoardNames(String empId) {
        Emp emp = empService.getEmpById(empId);
        Long deptId = emp.getDeptId();

        List<Board> list = boardPostMapper.getBoardNamesByDeptId(deptId);
        List<String> companyBoardList = new ArrayList<>();
        List<String> generalBoardList = new ArrayList<>();
        List<String> departmentBoardList = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();

        for (Board board : list) {
            if (board.getBoardName1().equals("전사게시판")) {
                companyBoardList.add(board.getBoardName2());
            } else if (board.getBoardName1().equals("일반게시판")) {
                generalBoardList.add(board.getBoardName2());
            } else {
                departmentBoardList.add(board.getBoardName2());
            }
        }
        map.put("전사게시판", companyBoardList);
        map.put("일반게시판", generalBoardList);
        map.put("부서게시판", departmentBoardList);
        return map;
    }

    public List<Object> getBoardListByEmpId(String empId) {
        Emp emp = empService.getEmpById(empId);
        Long deptId = emp.getDeptId();

        List<BoardHomeDeptDTO> boardDeptList = boardPostMapper.getBoardListByDeptId(deptId);
        List<Board> boardCommponeyList = boardPostMapper.getBoardListByName1("전사게시판");
        List<Board> boardGeneralList = boardPostMapper.getBoardListByName1("일반게시판");

        List<Object> boardScope = new ArrayList<>();
        boardScope.add(boardCommponeyList);
        boardScope.add(boardGeneralList);
        boardScope.add(boardDeptList);
        return boardScope;
    }

    public Long getBoardId(BoardDTO boardDTO) {
        return boardPostMapper.findBoardIdByName1Name2(boardDTO);
    }

    public Board getBoardById(Long boardId) {
        return boardPostMapper.findBoardByBoardId(boardId);
    }

    public List<BoardAndManagementDTO> getBoardAndManagement(String boardManager, Long boardId) {
        return boardPostMapper.getBoardAndManagement(boardManager, boardId);
    }

    public List<BoardPostEmpDTO> getBoardListAll(String empId, Integer limit) {
        Emp emp = empService.getEmpById(empId);
        Long deptId = emp.getDeptId();

        List<BoardPostEmpDTO> boardPostEmpDTOList = boardPostMapper.getBoardListAll(deptId, limit);

        if (boardPostEmpDTOList.isEmpty()) {
            log.warn("No board data found for deptId: " + deptId);
        } else {
            log.info("Board data: {}", boardPostEmpDTOList);
        }

        log.info("결과 : " + boardPostEmpDTOList.isEmpty());
        return boardPostEmpDTOList;
    }

    @Transactional
    public Long addPost(BoardDTO boardDTO, PostAddDTO postAddDTO, List<MultipartFile> files) {

        // 1. 게시판 ID 가져오기
        Long boardId = boardPostMapper.findBoardIdByName1Name2(boardDTO);
        if (boardId == null) {
            throw new IllegalArgumentException("게시판 이름을 찾을 수 없습니다.");
        }

        Post post = Post.builder()
                .boardId(boardId)
                .postWriter(postAddDTO.getPostWriter())
                .postSubject(postAddDTO.getPostSubject())
                .postContent(postAddDTO.getPostContent()).build();


        // 2. 게시글 삽입
        int postInsert = boardPostMapper.insertPost(post);
        int update = boardPostMapper.refUpdate(post.getPostId());

        if (postInsert <= 0 || update != 1) {
            throw new RuntimeException("게시글 삽입 실패");
        }

        List<FileDTO> fileDTOList = new ArrayList<>();
        //파일업로드
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                fileDTOList.add(fileService.uploadFile(file, UPLOAD_DIRECTORY));
            }

            int num = 0;
            num += boardPostMapper.insertPostFile(post.getPostId(), fileDTOList);
            if (num <= 0) {
                return -1L;
            }
        }
        return post.getPostId();
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
        map.put("listCount", getSearchListCountByBoardId(boardId, category, query));
        map.put("postList", getSearchListByBoardId(page, limit, boardId, category, query));
        return map;
    }

    public int getSearchListCountByBoardId(Long boardId, String category, String query) {
        return boardPostMapper.getSearchListCountByBoardId(boardId, category, query);
    }

    public List<Post> getSearchListByBoardId(int page, int endRow, Long boardId, String category, String query) {
        int startRow = (page - 1) * endRow;
        List<Post> postList = boardPostMapper.getSearchListByBoardId(startRow, endRow, boardId, category, query);
        log.info("postList: " + postList.toString());
        return postList;
    }

    public void setReadCountUpdate(Long postId) {
        boardPostMapper.setReadCountUpdate(postId);
    }

    public PostDetailDTO getDetail(Long postId) {
        PostDetailDTO postDetailDTO = boardPostMapper.getDetail(postId);
        List<PostFile> postFiles = boardPostMapper.getDetailPostFile(postId);

        List<Long> postFileSizes = new ArrayList<>();
        for (PostFile postFile : postFiles) {
            long fileSize = fileService.getFileSize(UPLOAD_DIRECTORY, postFile.getPostFileUUID(), postFile.getPostFileOriginal());
            postFileSizes.add(fileSize);
        }

        System.out.println("postFIleSize = " + postFileSizes);

        postDetailDTO.setPostFiles(postFiles);
        postDetailDTO.setPostFileSizes(postFileSizes);
        return postDetailDTO;
    }

    @Transactional
    public boolean modifyPost(BoardDTO boardDTO, PostModifyDTO postModifyDTO,
                              List<MultipartFile> addFiles, List<String> deletedFiles) {
        Long postId = postModifyDTO.getPostId();
        if(boardDTO.getBoardName1() == null){
            boardDTO.setBoardName1(boardDTO.getBoardName1Hidden());
            boardDTO.setBoardName2(boardDTO.getBoardName2Hidden());
        }

        PostDetailDTO postDetailDTO = getDetail(postId);
        Long boardId = boardPostMapper.findBoardIdByName1Name2(boardDTO);

        postDetailDTO.setBoardId(boardId);
        postDetailDTO.setPostSubject(postModifyDTO.getPostSubject());
        postDetailDTO.setPostContent(postModifyDTO.getPostContent());

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
        if (addFiles != null && !addFiles.isEmpty()) {
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

    public ResponseEntity<Resource> postFileDown(PostFile postFile) {
        return fileService.downloadFile(UPLOAD_DIRECTORY, postFile.getPostFileUUID(), postFile.getPostFileOriginal());
    }

    public boolean postDelete(Long postId) {
        // 게시글 정보 조회
        HashMap<String, Object> map = selectPostInfo(postId);
        if (map == null) {
            throw new RuntimeException("게시글 정보 조회 에러");
        }

        //게시글 첨부파일 삭제
        List<PostFile> postFiles = boardPostMapper.getDetailPostFile(postId);
        if (postFiles != null && !postFiles.isEmpty()) {
            for (PostFile postFile : postFiles) {
                fileService.deleteFile(postFile.getPostFileUUID(), UPLOAD_DIRECTORY, postFile.getPostFileOriginal());
            }
        }

        // 게시글 삭제
        int result = boardPostMapper.deletePost((int) map.get("post_re_ref"),
                (int) map.get("post_re_lev"),
                (int) map.get("post_re_seq"));
        return result >= 1; // 삭제 성공 여부 반환
    }

    public PostFile getPostFileByUUID(String uuid) {
        return boardPostMapper.getPostFileByUUID(uuid);
    }

    public HashMap<String, Object> selectPostInfo(Long postId) {
        return boardPostMapper.selectPostInfo(postId);
    }

    @Transactional
    public Long replyPost(BoardDTO boardDTO, PostReplyDTO postReplyDTO, List<MultipartFile> files) {

        // 1. 게시판 ID 가져오기
        Long boardId = boardPostMapper.findBoardIdByName1Name2(boardDTO);
        if (boardId == null) {
            throw new IllegalArgumentException("게시판 이름을 찾을 수 없습니다.");
        }

        Post post = Post.builder()
                .boardId(boardId)
                .postWriter(postReplyDTO.getPostWriter())
                .postSubject(postReplyDTO.getPostSubject())
                .postContent(postReplyDTO.getPostContent())
                .postReRef(postReplyDTO.getPostReRef())
                .postReLev(postReplyDTO.getPostReLev())
                .postReSeq(postReplyDTO.getPostReSeq()).build();


        // 2. 게시글 삽입
        boardPostMapper.updateReplySeq(post.getPostReRef(), post.getPostReSeq()); //게시글 순서 업데이트
        int postReply = boardPostMapper.replyPostInsert(post);

        if (postReply <= 0) {
            throw new RuntimeException("게시글 삽입 실패");
        }

        //파일업로드
        if (files != null && !files.isEmpty()) {
            List<FileDTO> fileDTOList = new ArrayList<>();
            for (MultipartFile file : files) {
                fileDTOList.add(fileService.uploadFile(file, UPLOAD_DIRECTORY));
            }

            int num = boardPostMapper.insertPostFile(post.getPostId(), fileDTOList);
            if (num <= 0) {
                return -1L;
            }
        }
        return post.getPostId();
    }

    @Transactional
    public boolean createBoard(BoardRequestDTO boardRequestDTO) {
        String ceoId = empService.getEmpListByDeptId(1000L).get(0).getEmpId();
        List<String> managerIdList = boardRequestDTO.getManagerId();
        if (managerIdList == null) {
            managerIdList = new ArrayList<>();
        }

        String boardName1 = boardRequestDTO.getBoardName1();
        String boardName2 = boardRequestDTO.getBoardName2();
        if (boardName1.equals("일반게시판")) {
            //일반게시판을 만들었을 경우 대표이사도 운영자 리스트에 추가
            if (!managerIdList.contains("admin")){
                managerIdList.add("admin");
            }
            if (!managerIdList.contains(ceoId)) {
                managerIdList.add(ceoId);
            }
        }

        Board board = Board.builder()
                .boardName1(boardName1)
                .boardName2(boardName2)
                .boardDescription(boardRequestDTO.getBoardDescription())
                .deptId(0L).build();

        int insertResult = boardPostMapper.createBoard(board);

        int insertManager = 0;
        if (!managerIdList.isEmpty()) {
            insertManager += boardPostMapper.insertBoardManagement(board.getBoardId(), managerIdList);
        } else {
            insertManager = 1;
        }
        return insertManager > 0 && insertResult > 0;
    }

    public List<BoardAndManagementDTO> getBoardModify(Long boardId) {
        List<BoardAndManagementDTO> boardManagementList = getBoardAndManagement(null, boardId);

        BoardAndManagementDTO boardManagement = new BoardAndManagementDTO();
        if (boardManagementList.isEmpty()) {
            Board board = boardPostMapper.findBoardByBoardId(boardId);
            boardManagement.setBoardId(boardId);
            boardManagement.setBoardName1(board.getBoardName1());
            boardManagement.setBoardName2(board.getBoardName2());
            boardManagement.setBoardManager("admin");
            boardManagement.setBoardDescription(board.getBoardDescription());
            boardManagementList.add(boardManagement);
        }
        return boardManagementList;
    }

    @Transactional
    public boolean modifyBoard(String boardManager, BoardRequestDTO boardRequestDTO) {
        String ceoId = empService.getEmpListByDeptId(1000L).get(0).getEmpId();
        List<String> managerIdList = boardRequestDTO.getManagerId();
        if (managerIdList == null) {
            managerIdList = new ArrayList<>();
        }

        String boardName1 = boardRequestDTO.getBoardName1();
        String boardName2 = boardRequestDTO.getBoardName2();
        if (boardName1 == null || boardName1.equals("일반게시판")) {
            boardName1 = "일반게시판";

            //일반게시판을 만들었을 경우 대표이사도 운영자 리스트에 추가
            if (!managerIdList.contains("admin")){
                managerIdList.add("admin");
            }
            if (!managerIdList.contains(ceoId)) {
                managerIdList.add(ceoId);
            }
        }

        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardName1(boardRequestDTO.getOriginalBoardName1());
        boardDTO.setBoardName2(boardRequestDTO.getOriginalBoardName2());

        Long boardId = boardPostMapper.findBoardIdByName1Name2(boardDTO);
        if (boardId == null) {
            return false;
        }
        Board board = Board.builder()
                .boardId(boardId)
                .boardName1(boardName1)
                .boardName2(boardName2)
                .boardDescription(boardRequestDTO.getBoardDescription()).build();

        int updateResult = boardPostMapper.updateBoard(board);

        List<BoardAndManagementDTO> boardAndManagementDTOS;
        if(boardManager.equals("admin")){
            boardAndManagementDTOS = boardPostMapper.getBoardAndManagement(null, boardId);
        }else{
            boardAndManagementDTOS = boardPostMapper.getBoardAndManagement(boardManager, boardId);
        }

        boolean check = false;
        for (BoardAndManagementDTO boardAndManagementDTO : boardAndManagementDTOS) {
            if (!managerIdList.contains(boardAndManagementDTO.getBoardManager())) {
                check = true;
                break;
            }
        }

        if (check || boardAndManagementDTOS.size() != managerIdList.size()) {
            boardPostMapper.deleteBoardManagement(boardId);
            int insertManager = 0;
            if (!managerIdList.isEmpty()) {
                insertManager += boardPostMapper.insertBoardManagement(boardId, managerIdList);
            } else {
                insertManager = 1;
            }
            return insertManager > 0 && updateResult > 0;
        }

        return updateResult > 0;
    }

    public List<String> findBoardManagerById(Long boardId) {
        return boardPostMapper.findBoardManagerById(boardId);
    }

    public int deleteBoard(Long boardId) {
        boardPostMapper.deleteBoardManagement(boardId);
        return boardPostMapper.deleteBoard(boardId);
    }

    @Transactional
    public ResponseEntity<Resource> getSummerNoteImage(PostFile postFile) {
//        String redisKey = REDIS_KEY_PREFIX + emp.getEmpId();
//
//        // Redis 캐시에서 이미지 가져오기
//        ResponseEntity<Resource> cachedResponse = getCachedImageFromRedis(redisKey);
//        if (cachedResponse != null) {
//            return cachedResponse;
//        }

        // S3에서 이미지 다운로드
        String fileUUID = postFile.getPostFileUUID();
        String fileName = postFile.getPostFileOriginal();
        ResponseEntity<Resource> s3Response = fileService.downloadFile(UPLOAD_DIRECTORY, fileUUID, fileName);

        try {
            byte[] imageData = Objects.requireNonNull(s3Response.getBody()).getInputStream().readAllBytes();

//            // Redis에 이미지 저장
//            cacheImage(redisKey, imageData);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(new ByteArrayResource(imageData));
        } catch (IOException e) {
            log.error("이미지 처리 실패", e);
            throw new RuntimeException("이미지 처리 중 오류 발생");
        }
    }

    public HashMap<String, Object> insertSummerNoteImg(List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();
        List<FileDTO> fileDTOList = new ArrayList<>();
        for (MultipartFile file : files) {
            FileDTO fileDTO = fileService.uploadFile(file, UPLOAD_DIRECTORY);

            String imageUrl = "/board/summernote-image?uuid=" + fileDTO.getFileUUID();
            imageUrls.add(imageUrl);
            fileDTOList.add(fileDTO);
        }
        boardPostMapper.insertPostFile(null, fileDTOList);

        List<String> fileUUIDList = fileDTOList.stream()
                .map(FileDTO::getFileUUID)
                .toList();

        HashMap<String, Object> map = new HashMap<>();
        map.put("urls", imageUrls);
        map.put("uuidList", fileUUIDList);
        return map;
    }

    public void deleteImageFile(String uuid) {
        List<String> uuidList = new ArrayList<>();
        uuidList.add(uuid);
        PostFile postFile = boardPostMapper.getPostFileByUUID(uuid);
        boardPostMapper.deletePostFile(uuidList);

        fileService.deleteFile(uuid, UPLOAD_DIRECTORY, postFile.getPostFileOriginal());
    }
}
