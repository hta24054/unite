package com.hta2405.unite.controller;

import com.hta2405.unite.domain.PostComment;
import com.hta2405.unite.dto.PostCommentRequestDTO;
import com.hta2405.unite.dto.PostCommentUpdateDTO;
import com.hta2405.unite.service.PostCommentService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 5,   // 메모리 내 파일 크기 제한 (5MB)
        maxFileSize = 1024 * 1024 * 10,        // 파일 하나의 최대 크기 (10MB)
        maxRequestSize = 1024 * 1024 * 50      // 요청 전체 크기 (50MB)
)
@RestController
@RequestMapping("/comments")
@Slf4j
public class PostCommentController {
    private final PostCommentService postCommentService;

    public PostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    @PostMapping("/list")
    public HashMap<String, Object> commentList(Long postId, int commentListOrder) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("empMap", postCommentService.getIdToENameMap());
        map.put("postCommentList", postCommentService.getCommentList(postId, commentListOrder));
        map.put("listCount", postCommentService.getListCount(postId));
        return map;
    }

    @PostMapping("/add")
    public Boolean addComment(
            PostCommentRequestDTO commentRequestDTO,
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(value = "attachFile", required = false) MultipartFile file) {

        PostComment.PostCommentBuilder postCommentBuilder = PostComment.builder()
                .postId(commentRequestDTO.getPostId())
                .postCommentWriter(user.getUsername())
                .postCommentContent(commentRequestDTO.getPostCommentContent())
                .postCommentReLev(0L)
                .postCommentReSeq(0L);

        return postCommentService.commentsInsert(postCommentBuilder, file);
    }

    @ResponseBody
    @GetMapping("/down")
    public void postCommentFileDown(Long commentId, HttpServletResponse response) {
        PostComment postComment = postCommentService.getPostCommentByCommentId(commentId);
        postCommentService.postCommentFileDown(postComment, response);
    }

    @ResponseBody
    @PostMapping("/update")
    public Boolean updateComment(
            PostCommentUpdateDTO commentUpdateDTO,
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(value = "attachFile", required = false) MultipartFile file,
            String[] deletedFile) {

        PostComment.PostCommentBuilder postCommentBuilder = PostComment.builder()
                .commentId(commentUpdateDTO.getCommentId())
                .postCommentContent(commentUpdateDTO.getPostCommentContent());

        return postCommentService.commentsUpdate(postCommentBuilder, file, deletedFile);
    }

    @ResponseBody
    @PostMapping("/reply")
    public Boolean replyComment(
            PostCommentRequestDTO commentRequestDTO,
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(value = "attachFile", required = false) MultipartFile file) {

        PostComment.PostCommentBuilder postCommentBuilder = PostComment.builder()
                .postId(commentRequestDTO.getPostId())
                .postCommentWriter(user.getUsername())
                .postCommentContent(commentRequestDTO.getPostCommentContent())
                .postCommentReLev(commentRequestDTO.getPostCommentReLev())
                .postCommentReSeq(commentRequestDTO.getPostCommentReSeq())
                .postCommentReRef(commentRequestDTO.getPostCommentReRef());

        return postCommentService.commentsInsert(postCommentBuilder, file);
    }

    @ResponseBody
    @PostMapping("/delete")
    public Boolean deleteComment(Long commentId) {
        return postCommentService.commentsDelete(commentId);
    }
}
