package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class PostCommentUpdateDTO {
    private Long commentId;
    private String postCommentContent;
    private MultipartFile attachFile;
}
