package com.hta2405.unite.controller;

import com.hta2405.unite.domain.PostComment;
import com.hta2405.unite.dto.PostCommentRequestDTO;
import com.hta2405.unite.dto.PostCommentUpdateDTO;
import com.hta2405.unite.service.PostCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

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

    @GetMapping("/down")
    public ResponseEntity<Resource> postCommentFileDown(Long commentId) {
        PostComment postComment = postCommentService.getPostCommentByCommentId(commentId);
        return postCommentService.postCommentFileDown(postComment);
    }

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

    @PostMapping("/delete")
    public Boolean deleteComment(Long commentId) {
        return postCommentService.commentsDelete(commentId);
    }
}
