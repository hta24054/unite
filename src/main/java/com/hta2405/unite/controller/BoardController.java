package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.PaginationResult;
import com.hta2405.unite.domain.PostFile;
import com.hta2405.unite.dto.*;
import com.hta2405.unite.service.BoardPostService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    public Object boardList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            BoardDTO boardDTO,
            Model model,
            HttpSession session) {

        boardSidebar_dept(model);
        session.setAttribute("referer", "list");

        HashMap<String, Object> map = boardPostService.getBoardListAndListCount(page, limit, boardDTO);
        PaginationResult result = new PaginationResult(page, limit, (int) map.get("listCount"));

        model.addAttribute("page", page);
        model.addAttribute("maxPage", result.getMaxpage());
        model.addAttribute("startPage", result.getStartpage());
        model.addAttribute("endPage", result.getEndpage());
        model.addAttribute("listCount", map.get("listCount"));
        model.addAttribute("postList", map.get("postList"));
        model.addAttribute("limit", limit);
        model.addAttribute("boardName1", boardDTO.getBoardName1());
        model.addAttribute("boardName2", boardDTO.getBoardName2());
        return "board/boardList";
    }

    @ResponseBody
    @GetMapping("/boardListAjax")
    public Object boardListAjax(
            boolean ajax,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            BoardDTO boardDTO,
            Model model,
            HttpSession session) {

        boardSidebar_dept(model);
        session.setAttribute("referer", "list");

        HashMap<String, Object> map = boardPostService.getBoardListAndListCount(page, limit, boardDTO);
        PaginationResult result = new PaginationResult(page, limit, (int) map.get("listCount"));

        HashMap<String, Object> ajaxMap = new HashMap<>();
        if (ajax) {
            ajaxMap.put("page", page);
            ajaxMap.put("maxPage", result.getMaxpage());
            ajaxMap.put("startPage", result.getStartpage());
            ajaxMap.put("endPage", result.getEndpage());
            ajaxMap.put("listCount", map.get("listCount"));
            ajaxMap.put("postList", map.get("postList"));
            ajaxMap.put("limit", limit);
            ajaxMap.put("boardName1", boardDTO.getBoardName1());
            ajaxMap.put("boardName2", boardDTO.getBoardName2());
        }
        return ajaxMap;
    }

    @ResponseBody
    @GetMapping("/searchAjax")
    public Object searchBoardListAjax(
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int limit,
                                      BoardDTO boardDTO,
                                      Model model,
                                      String category, String query) {

        boardSidebar_dept(model);

        HashMap<String, Object> map = boardPostService.getSearchListCountByBoardId(page,limit,boardDTO,category,query);
        PaginationResult result = new PaginationResult(page, limit, (int) map.get("listCount"));

        HashMap<String, Object> ajaxMap = new HashMap<>();
        ajaxMap.put("page", page);
        ajaxMap.put("maxPage", result.getMaxpage());
        ajaxMap.put("startPage", result.getStartpage());
        ajaxMap.put("endPage", result.getEndpage());
        ajaxMap.put("listCount", map.get("listCount"));
        ajaxMap.put("postList", map.get("postList"));
        ajaxMap.put("limit", limit);
        ajaxMap.put("boardName1", boardDTO.getBoardName1());
        ajaxMap.put("boardName2", boardDTO.getBoardName2());
        return ajaxMap;
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

    @GetMapping("/post/detail")
    public String boardPostDetail(@RequestParam(defaultValue = "1") int page, BoardDTO boardDTO, Long no, Model model, RedirectAttributes rattr) {
        boardSidebar_dept(model);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        boardPostService.setReadCountUpdate(no);
        PostDetailDTO postDetailDTO = boardPostService.getDetail(no);
        System.out.println("boardDTO"+ boardDTO.getBoardName2());

        if(postDetailDTO.getPostId() != null) {
            model.addAttribute("postDetailDTO", postDetailDTO);
            model.addAttribute("boardDTO", boardDTO);
            model.addAttribute("id", id);
            model.addAttribute("postId", no);
            model.addAttribute("page", page);
            return "post/boardView";
        }
        rattr.addAttribute("boardName1", boardDTO.getBoardName1());
        rattr.addAttribute("boardName2", boardDTO.getBoardName2());
        rattr.addAttribute("page", page);
        return "redirect:/board/boardList";
    }

    @GetMapping("/post/modify")
    public String boardModify(BoardDTO boardDTO, Long no, Model model, RedirectAttributes rattr) {
        boardSidebar_dept(model);

        PostDetailDTO postDetailDTO = boardPostService.getDetail(no);

        if(postDetailDTO.getPostId() != null) {
            model.addAttribute("postDetailDTO", postDetailDTO);
            model.addAttribute("boardDTO", boardDTO);
            return "post/postModify";
        }
        rattr.addAttribute("boardName1", boardDTO.getBoardName1());
        rattr.addAttribute("boardName2", boardDTO.getBoardName2());
        rattr.addAttribute("no", no);
        return "redirect:/board/post/detail";
    }

    @ResponseBody
    @PostMapping("/post/modifyProcess")
    public Object boardModifyProcess(PostModifyDTO postModifyDTO,BoardDTO boardDTO,
                                     @RequestParam(value = "addFiles", required = false) List<MultipartFile> addFiles,
                                     @RequestParam(value = "deletedFiles", required = false) List<String> deletedFiles) {

        System.out.println("addFiles=" + addFiles);
        System.out.println("deleteFiles=" + deletedFiles);

        boolean result = boardPostService.modifyPost(boardDTO,postModifyDTO,addFiles,deletedFiles);

        HashMap<String, Object> map = new HashMap<>();
        if(result) {
            map.put("modifyCheck", true);
        }else{
            map.put("modifyCheck", false);
        }
        map.put("boardDTO", boardDTO);
        map.put("postId", postModifyDTO.getPostId());
        return map;
    }

    @ResponseBody
    @GetMapping("/post/down")
    public void postFileDown(Long postFileId, HttpServletResponse response) {
        PostFile postFile = boardPostService.getPostFile(postFileId);
        boardPostService.postFileDown(postFile, response);
    }

    @PostMapping("/post/delete")
    public String BoardDelete(int no, RedirectAttributes rattr) {
        boolean result = boardPostService.postDelete(no);

        if (result) {
            rattr.addFlashAttribute("result", "게시글 삭제 성공");
        } else {
            rattr.addFlashAttribute("result", "게시글 삭제 실패");
        }

        // 리다이렉트 URL에 파라미터 추가
        rattr.addAttribute("page", 4);
        rattr.addAttribute("boardName1", "전사게시판");
        rattr.addAttribute("boardName2", "공지사항");

        return "redirect:list";
    }
}
