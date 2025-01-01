package com.hta2405.unite.controller;

import com.hta2405.unite.dto.BoardPostEmpDTO;
import com.hta2405.unite.service.BoardPostService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

        List<BoardPostEmpDTO> boardPostEmpDTO = boardPostService.getBoardListAll(empId)
                .map(List::of) // DTO가 존재하면 리스트로 감싸서 반환
                .orElse(Collections.emptyList()); // Optional이 비었을 경우 빈 리스트 반환

        if(boardPostEmpDTO.isEmpty()){
            return Collections.emptyList();
        }
        return boardPostEmpDTO;
    }

    @GetMapping(value = "/post/postWrite")
    public String boardPostWrite(String boardName2, Model model) {
        model.addAttribute("boardName2", boardName2);
        return "post/postWrite";
    }
}
