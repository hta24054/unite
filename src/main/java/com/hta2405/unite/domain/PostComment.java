package com.hta2405.unite.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PostComment {
    private Long commentId;
    private Long postId;
    private String postCommentWriter;
    private String postCommentContent;
    private LocalDateTime postCommentDate;
    private LocalDateTime postCommentUpdateDate;
    private String postCommentFilePath;
    private String postCommentFileOriginal;
    private String postCommentFileUUID;
    private String postCommentFileType;
    private Long postCommentReRef;
    private Long postCommentReLev;
    private Long postCommentReSeq;
}
