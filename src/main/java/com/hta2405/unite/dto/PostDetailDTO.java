package com.hta2405.unite.dto;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.Post;
import com.hta2405.unite.domain.PostFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class PostDetailDTO {
    private Long postId;
    private Long boardId;
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

    private List<PostFile> postFiles;
}
