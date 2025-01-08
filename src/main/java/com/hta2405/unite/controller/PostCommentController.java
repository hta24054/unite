package com.hta2405.unite.controller;

import com.hta2405.unite.service.PostCommentService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
