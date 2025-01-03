package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Board;
import com.hta2405.unite.domain.PaginationResult;
import com.hta2405.unite.dto.BoardDTO;
import com.hta2405.unite.dto.BoardHomeDeptDTO;
import com.hta2405.unite.dto.BoardPostEmpDTO;
import com.hta2405.unite.dto.PostDTO;
import com.hta2405.unite.service.BoardPostService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 5,   // 메모리 내 파일 크기 제한 (5MB)
        maxFileSize = 1024 * 1024 * 10,        // 파일 하나의 최대 크기 (10MB)
        maxRequestSize = 1024 * 1024 * 50      // 요청 전체 크기 (50MB)
)
@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {
    private final BoardPostService boardPostService;

    public BoardController(BoardPostService boardPostService) {
        this.boardPostService = boardPostService;
    }

    @GetMapping(value = "/home")//board/write
    public String boardHome(Model model) {
        //sidebar 부서게시판 설정
        boardSidebar_dept(model);

        return "board/boardHome";
    }

    //sidebar 부서게시판 설정
    private void boardSidebar_dept(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String empId = authentication.getName();

        List<BoardHomeDeptDTO> BoardDeptList = boardPostService.getBoardListByEmpId(empId);
        model.addAttribute("boardScope", BoardDeptList);
    }

    @ResponseBody
    @GetMapping(value = "/homeProcess")
    public List<BoardPostEmpDTO> boardProcess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String empId = authentication.getName();

        List<BoardPostEmpDTO> boardPostEmpDTOList = boardPostService.getBoardListAll(empId);

        if (boardPostEmpDTOList.isEmpty()) {
            return Collections.emptyList();
        }
        return boardPostEmpDTOList;
    }

    @GetMapping("/boardList")
    public String boardList(
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int limit,
                        BoardDTO boardDTO,
                        Model model,
                        HttpSession session) {
        boardSidebar_dept(model);
        session.setAttribute("referer", "list");


        HashMap<String, Object> map =  boardPostService.getBoardListAndListCount(page, limit, boardDTO);

        PaginationResult result = new PaginationResult(page, limit, (int) map.get("listCount"));

        model.addAttribute("page", page);
        model.addAttribute("maxPage", result.getMaxpage());
        model.addAttribute("startPage", result.getStartpage());
        model.addAttribute("endPage", result.getEndpage());
        model.addAttribute("listCount", map.get("listCount"));
        model.addAttribute("postList", map.get("postList"));
        model.addAttribute("limit", limit);
        model.addAttribute("boardName2", boardDTO.getBoardName2());
        return "board/boardList";
    }

    @GetMapping(value = "/post/postWrite")
    public String boardPostWrite(Model model) {
        boardSidebar_dept(model);
        return "post/postWrite";
    }

    @ResponseBody
    @PostMapping(value = "/post/add")
    public boolean boardPostAdd(
            PostDTO postDTO,
            BoardDTO boardDTO,
            @RequestParam(value = "attachFiles", required = false) List<MultipartFile> files) {

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
