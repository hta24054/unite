package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostModifyDTO {
    private Long postId;
    private String postSubject;
    private String postContent;
}
