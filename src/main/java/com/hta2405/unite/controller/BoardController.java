package com.hta2405.unite.controller;

import com.hta2405.unite.domain.BoardManagement;
import com.hta2405.unite.domain.PaginationResult;
import com.hta2405.unite.domain.PostFile;
import com.hta2405.unite.dto.*;
import com.hta2405.unite.service.BoardPostService;
import com.hta2405.unite.service.EmpService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {
    private final BoardPostService boardPostService;
    private final EmpService empService;

    public BoardController(BoardPostService boardPostService, EmpService empService) {
        this.boardPostService = boardPostService;
        this.empService = empService;
    }

    @GetMapping(value = "/home")
    public String boardHome(String message, Model model, RedirectAttributes rattr) {
        //sidebar 부서게시판 설정
        boardSidebarDept(model);

        rattr.addAttribute("message", message);
        return "board/boardHome";
    }

    //sidebar 부서게시판 설정
    private void boardSidebarDept(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String empId = auth.getName();
        List<Object> boardScope = boardPostService.getBoardListByEmpId(empId);
        List<BoardAndManagementDTO> boardManagementList = boardPostService.getBoardAndManagement(empId, null);

        model.addAttribute("userId", empId);
        model.addAttribute("boardManagementList", boardManagementList);
        model.addAttribute("boardScope", boardScope);
    }

    @ResponseBody
    @GetMapping(value = "/homeProcess")
    public Object boardProcess(@AuthenticationPrincipal UserDetails user) {
        String empId = user.getUsername();
        List<BoardPostEmpDTO> boardPostEmpDTOList = boardPostService.getBoardListAll(empId, null);

        if (boardPostEmpDTOList.isEmpty()) {
            return null;
        }
        HashMap<String, Object> ajaxMap = new HashMap<>();
        ajaxMap.put("list", boardPostEmpDTOList);
        ajaxMap.put("empMap", empService.getIdToENameMap());
        return ajaxMap;
    }

    @GetMapping("/boardList")
    public Object boardList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            BoardDTO boardDTO,
            Model model,
            HttpSession session) {

        boardSidebarDept(model);
        session.setAttribute("referer", "list");

        Long boardId = boardPostService.getBoardId(boardDTO);
        String boardDescription = boardPostService.getBoardById(boardId).getBoardDescription();
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
        model.addAttribute("empMap", empService.getIdToENameMap());
        model.addAttribute("boardManagements", boardPostService.getBoardModify(boardId));
        model.addAttribute("boardDescription", boardDescription);
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

        boardSidebarDept(model);
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
            ajaxMap.put("empMap", empService.getIdToENameMap());
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

        boardSidebarDept(model);

        HashMap<String, Object> map = boardPostService.getSearchListCountByBoardId(page, limit, boardDTO, category, query);
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
        ajaxMap.put("empMap", empService.getIdToENameMap());
        return ajaxMap;
    }

    @GetMapping(value = "/post/postWrite")
    public String boardPostWrite(Model model, @AuthenticationPrincipal UserDetails user) {
        boardSidebarDept(model);
        HashMap<String, Object> map = boardPostService.getBoardNames(user.getUsername());
        model.addAttribute("boardMap", map);
        return "post/postWrite";
    }

    @ResponseBody
    @PostMapping(value = "/post/add")
    public HashMap<String, Object> boardPostAdd(
            PostAddDTO postAddDTO,
            BoardDTO boardDTO,
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(value = "attachFiles", required = false) List<MultipartFile> files) {

        HashMap<String, Object> map = new HashMap<>();
        String postWriter = user.getUsername();
        postAddDTO.setPostWriter(postWriter);

        map.put("postId", boardPostService.addPost(boardDTO, postAddDTO, files));
        map.put("boardDTO", boardDTO);
        return map;
    }

    @GetMapping("/post/detail")
    public String boardPostDetail(@RequestParam(defaultValue = "1") int page, BoardDTO boardDTO,
                                  Long no, Model model, RedirectAttributes rattr,
                                  @AuthenticationPrincipal UserDetails user) {
        boardSidebarDept(model);
        String id = user.getUsername();

        boardPostService.setReadCountUpdate(no);
        PostDetailDTO postDetailDTO = boardPostService.getDetail(no);

        if (postDetailDTO.getPostId() != null) {
            List<BoardAndManagementDTO> boardManagements = boardPostService.getBoardModify(postDetailDTO.getBoardId());

            model.addAttribute("empMap", empService.getIdToENameMap());
            model.addAttribute("postDetailDTO", postDetailDTO);
            model.addAttribute("boardDTO", boardDTO);
            model.addAttribute("boardManagements", boardManagements);
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
    public String boardPostModify(BoardDTO boardDTO, Long no, @AuthenticationPrincipal UserDetails user,
                                  Model model, RedirectAttributes rattr) {
        boardSidebarDept(model);
        PostDetailDTO postDetailDTO = boardPostService.getDetail(no);

        if (postDetailDTO.getPostId() != null) {
            HashMap<String, Object> map = boardPostService.getBoardNames(user.getUsername());
            model.addAttribute("boardMap", map);
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
    public Object boardPostModifyProcess(PostModifyDTO postModifyDTO, BoardDTO boardDTO,
                                         @RequestParam(value = "addFiles", required = false) List<MultipartFile> addFiles,
                                         @RequestParam(value = "deletedFiles", required = false) List<String> deletedFiles) {
        boolean result = boardPostService.modifyPost(postModifyDTO, addFiles, deletedFiles);

        HashMap<String, Object> map = new HashMap<>();
        if (result) {
            map.put("modifyCheck", true);
        } else {
            map.put("modifyCheck", false);
        }
        if (boardDTO.getBoardName1() == null) {
            boardDTO.setBoardName1(boardDTO.getBoardName1Hidden());
            boardDTO.setBoardName2(boardDTO.getBoardName2Hidden());
        }
        map.put("boardDTO", boardDTO);
        map.put("postId", postModifyDTO.getPostId());
        return map;
    }

    @ResponseBody
    @GetMapping("/post/down")
    public ResponseEntity<Resource> postFileDown(Long postFileId) {
        PostFile postFile = boardPostService.getPostFile(postFileId);
        return boardPostService.postFileDown(postFile);
    }

    @PostMapping("/post/delete")
    public String boardPostDelete(int page, BoardDTO boardDTO, Long no, RedirectAttributes rattr) {
        boolean result = boardPostService.postDelete(no);

        if (result) {
            rattr.addFlashAttribute("result", "게시글 삭제 성공");
        } else {
            rattr.addFlashAttribute("result", "게시글 삭제 실패");
        }

        // 리다이렉트 URL에 파라미터 추가
        rattr.addAttribute("page", page);
        rattr.addAttribute("boardName1", boardDTO.getBoardName1());
        rattr.addAttribute("boardName2", boardDTO.getBoardName2());

        return "redirect:../boardList";
    }

    @GetMapping("/post/reply")
    public String boardPostReply(Long no, BoardDTO boardDTO,
                                 @AuthenticationPrincipal UserDetails user, Model model) {
        boardSidebarDept(model);

        HashMap<String, Object> postMap = boardPostService.selectPostInfo(no);
        if (postMap == null) {
            throw new RuntimeException("답글 불러오기 실패");
        }

        HashMap<String, Object> boardMap = boardPostService.getBoardNames(user.getUsername());
        model.addAttribute("boardMap", boardMap);
        model.addAttribute("postMap", postMap);
        model.addAttribute("boardDTO", boardDTO);
        return "post/postReply";
    }

    @ResponseBody
    @PostMapping("/post/replyProcess")
    public HashMap<String, Object> replyProcess(PostReplyDTO postReplyDTO,
                                                BoardDTO boardDTO,
                                                @AuthenticationPrincipal UserDetails user,
                                                @RequestParam(value = "attachFiles", required = false) List<MultipartFile> files) {
        HashMap<String, Object> map = new HashMap<>();
        String postWriter = user.getUsername();
        postReplyDTO.setPostWriter(postWriter);

        map.put("postId", boardPostService.replyPost(boardDTO, postReplyDTO, files));
        map.put("boardDTO", boardDTO);
        return map;
    }

    @GetMapping("/create")
    public String boardCreate(@AuthenticationPrincipal UserDetails user, Model model) {
        boardSidebarDept(model);
        model.addAttribute("empId", user.getUsername());
        return "board/boardCreate";
    }

    @PostMapping("/createProcess")
    public String boardCreateProcess(BoardRequestDTO boardRequestDTO, @AuthenticationPrincipal UserDetails user,
                                     Model model, RedirectAttributes rattr) {
        if (!user.getUsername().equals("admin")) {
            if (Objects.equals(boardRequestDTO.getBoardName1(), "전사게시판")) {
                rattr.addFlashAttribute("message", "fail");
                return "redirect:/board/home";
            }
        }

        boolean result = boardPostService.createBoard(boardRequestDTO);

        if (result) {
            rattr.addFlashAttribute("message", "success");
            return "redirect:/board/home";
        } else {
            model.addAttribute("errorMessage", "게시판 추가 실패");
            return "error/error";
        }
    }

    @PostMapping("/modify")
    public String boardModify(Long boardId, Model model, RedirectAttributes rattr,
                              @AuthenticationPrincipal UserDetails user) {
        boardSidebarDept(model);

        String empId = user.getUsername();

        if (!user.getUsername().equals("admin")) {
            List<String> boardManagerList = boardPostService.findBoardManagerById(boardId);
            if (boardManagerList == null) {
                rattr.addFlashAttribute("message", "fail");
                return "redirect:/board/home";
            }

            if (!boardManagerList.contains(user.getUsername())) {
                rattr.addFlashAttribute("message", "fail");
                return "redirect:/board/home";
            }
        }

        List<BoardAndManagementDTO> boardManagements = boardPostService.getBoardModify(boardId);

        model.addAttribute("empMap", empService.getIdToENameMap());
        model.addAttribute("empId", empId);
        model.addAttribute("boardManagements", boardManagements);
        return "board/boardModify";
    }

    @PostMapping("/modifyProcess")
    public String boardModifyProcess(@AuthenticationPrincipal UserDetails user, BoardRequestDTO boardRequestDTO,
                                     RedirectAttributes rattr) {
        String empId = user.getUsername();

        if (!empId.equals("admin")) {
            if (Objects.equals(boardRequestDTO.getBoardName1(), "전사게시판")) {
                rattr.addFlashAttribute("message", "fail");
                return "redirect:/board/home";
            }
        }

        boolean result = boardPostService.modifyBoard(empId, boardRequestDTO);

        if (result) {
            rattr.addFlashAttribute("message", "수정 성공");
        } else {
            rattr.addFlashAttribute("message", "수정 실패");
        }
        return "redirect:/board/home";
    }

    @PostMapping("/delete")
    public ResponseEntity<String> boardDelete(@AuthenticationPrincipal UserDetails user, @RequestBody HashMap<String, String> requestData) {
        Long boardId = Long.valueOf(requestData.get("boardId"));

        if (!user.getUsername().equals("admin")) {
            List<String> boardManagerList = boardPostService.findBoardManagerById(boardId);
            if (boardManagerList == null) {
                return ResponseEntity.badRequest().body("Invalid boardManagement by boardId");
            }

            if (!boardManagerList.contains(user.getUsername())) {
                return ResponseEntity.badRequest().body("User access denied");
            }
        }

        int result = boardPostService.deleteBoard(boardId);
        if (result == 0) {
            return ResponseEntity.badRequest().body("삭제 실패");
        }
        return ResponseEntity.ok("삭제 성공");
    }
}
