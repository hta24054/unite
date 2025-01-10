package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostCommentRequestDTO {
    private Long postId;
    private String postCommentContent;
    private Long postCommentReRef;
    private Long postCommentReLev;
    private Long postCommentReSeq;
}
