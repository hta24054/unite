package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.PostComment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostCommentMapper {
    int getListCount(Long postId);

    List<PostComment> getCommentList(Long postId, String order);

    int commentsInsert(PostComment postComment);

    int refUpdate(Long postId);

    PostComment getPostCommentByCommentId(Long commentId);

    int commentsUpdate(PostComment postComment, Boolean fileUpdateCheck);

    void replyUpdate(Long postCommentReRef, Long postCommentReSeq);

    int commentsReply(PostComment postComment);

    int commentsDelete(Long commentId);
}
