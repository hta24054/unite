package com.hta2405.unite.service;

import com.hta2405.unite.domain.PostComment;
import com.hta2405.unite.mybatis.mapper.PostCommentMapper;
import com.hta2405.unite.util.ConfigUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostCommentService {
    private static final String UPLOAD_DIRECTORY = ConfigUtil.getProperty("postComment.upload.directory");
    private final PostCommentMapper postCommentMapper;
    private final EmpService empService;
    private final FileService fileService;

    public Map<String, String> getIdToENameMap() {
        return empService.getIdToENameMap();
    }

    public int getListCount(Long postId) {
        return postCommentMapper.getListCount(postId);
    }

    public List<PostComment> getCommentList(Long postId, int order) {
        return postCommentMapper.getCommentList(postId, order == 1 ? "asc" : "desc");
    }
}
