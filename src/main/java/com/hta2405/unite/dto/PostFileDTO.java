package com.hta2405.unite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PostFileDTO {
    private Long postId;          // 게시글 ID
    private String postFilePath;  // 파일 경로
    private String postFileOriginal;
    private String postFileUUID;
    private String postFileType;
}
