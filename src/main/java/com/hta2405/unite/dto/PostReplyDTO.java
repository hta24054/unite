package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostReplyDTO {
    private Long boardId;     // 게시판 ID
    private String postWriter;
    private String postSubject;
    private String postContent;
    private Long postReRef;
    private Long postReLev;
    private Long postReSeq;
}
