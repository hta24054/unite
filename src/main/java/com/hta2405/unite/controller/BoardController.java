package com.hta2405.unite.controller;

import com.hta2405.unite.dto.BoardDTO;
import com.hta2405.unite.dto.BoardPostEmpDTO;
import com.hta2405.unite.dto.PostDTO;
import com.hta2405.unite.service.BoardPostService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 5,   // 메모리 내 파일 크기 제한 (5MB)
    maxFileSize = 1024 * 1024 * 10,        // 파일 하나의 최대 크기 (10MB)
    maxRequestSize = 1024 * 1024 * 50      // 요청 전체 크기 (50MB)
)
@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController{
    private final BoardPostService boardPostService;

    public BoardController(BoardPostService boardPostService) {
        this.boardPostService = boardPostService;
    }

    @GetMapping(value = "/home")//board/write
    public String boardHome() {
        return "board/boardHome";
    }

    @ResponseBody
    @GetMapping(value = "/homeProcess")
    public List<BoardPostEmpDTO> boardProcess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String empId = authentication.getName();

        List<BoardPostEmpDTO> boardPostEmpDTOList = boardPostService.getBoardListAll(empId);

        if(boardPostEmpDTOList.isEmpty()){
            return Collections.emptyList();
        }
        return boardPostEmpDTOList;
    }

    @GetMapping(value = "/post/postWrite")
    public String boardPostWrite() {
        return "post/postWrite";
    }

    @ResponseBody
    @PostMapping(value = "/post/add")
    public boolean boardPostAdd(
            PostDTO postDTO,
            BoardDTO boardDTO,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String postWriter = authentication.getName();
            postDTO.setPostWriter(postWriter);

            return boardPostService.addPost(boardDTO, postDTO, files);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
