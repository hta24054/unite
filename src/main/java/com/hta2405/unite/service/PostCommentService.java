package com.hta2405.unite.service;

import com.hta2405.unite.domain.PostComment;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.mybatis.mapper.PostCommentMapper;
import com.hta2405.unite.util.ConfigUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public Boolean commentsInsert(PostComment.PostCommentBuilder postCommentBuilder, MultipartFile file) {
        //파일업로드
        if (file != null && !file.isEmpty()) {
            FileDTO fileDTO = fileService.uploadFile(file, UPLOAD_DIRECTORY);

            postCommentBuilder.postCommentFileOriginal(fileDTO.getFileOriginal())
                    .postCommentFilePath(fileDTO.getFilePath())
                    .postCommentFileType(fileDTO.getFileType())
                    .postCommentFileUUID(fileDTO.getFileUUID());
        }
        PostComment postComment = postCommentBuilder.build();

        int postCommentInsert = 0;
        int update = 1;

        if (postComment.getPostCommentReRef() == null) {
            //댓글 추가
            postCommentInsert = postCommentMapper.commentsInsert(postComment);
            update = postCommentMapper.refUpdate(postComment.getCommentId());
        } else {
            //댓글 답글 추가
            postCommentMapper.replyUpdate(postComment.getPostCommentReRef(), postComment.getPostCommentReSeq());
            postCommentInsert = postCommentMapper.commentsReply(postComment);
        }

        return postCommentInsert > 0 && update == 1;
    }

    public void postCommentFileDown(PostComment postComment, HttpServletResponse response) {
        fileService.downloadFile(UPLOAD_DIRECTORY, postComment.getPostCommentFileUUID(), postComment.getPostCommentFileOriginal(), response);
    }

    public PostComment getPostCommentByCommentId(Long commentId) {
        return postCommentMapper.getPostCommentByCommentId(commentId);
    }

    public Boolean commentsUpdate(PostComment.PostCommentBuilder postCommentBuilder, MultipartFile file, String[] deletedFile) {
        Boolean fileUpdateCheck = false;
        //파일삭제
        if (deletedFile != null && deletedFile.length > 0) {
            fileService.deleteFile(deletedFile[0], UPLOAD_DIRECTORY, deletedFile[1]);
            fileUpdateCheck = true;
        }
        //파일 추가
        if (file != null && !file.isEmpty()) {
            FileDTO fileDTO = fileService.uploadFile(file, UPLOAD_DIRECTORY);

            postCommentBuilder.postCommentFileOriginal(fileDTO.getFileOriginal())
                    .postCommentFilePath(fileDTO.getFilePath())
                    .postCommentFileType(fileDTO.getFileType())
                    .postCommentFileUUID(fileDTO.getFileUUID());
            fileUpdateCheck = true;
        }
        PostComment postComment = postCommentBuilder.build();

        int postCommentUpdate = postCommentMapper.commentsUpdate(postComment, fileUpdateCheck);
        return postCommentUpdate > 0;
    }

    public Boolean commentsDelete(Long commentId) {
        PostComment postComment = postCommentMapper.getPostCommentByCommentId(commentId);
        if (postComment.getPostCommentFileUUID() != null) {
            fileService.deleteFile(postComment.getPostCommentFileUUID(), UPLOAD_DIRECTORY, postComment.getPostCommentFileOriginal());
        }
        //게시글 첨부파일 삭제
        return postCommentMapper.commentsDelete(commentId) == 1;
    }
}
