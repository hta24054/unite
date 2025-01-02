package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardPostEmpDTO {
    private Long boardId;
    private String boardName1;
    private String boardName2;
    private Long deptId;

    private Long postId;
    private String postWriter; //emp_id
    private String postSubject;
    private String postContent;
    private LocalDateTime postDate;
    private LocalDateTime postUpdateDate;
    private Long postReRef;
    private Long postReLev;
    private Long postReSeq;
    private Long postView;
    private Long postCommentCnt;

    private String imgPath;
    private String imgOriginal;
    private String imgUUID;
    private String imgType;
}
