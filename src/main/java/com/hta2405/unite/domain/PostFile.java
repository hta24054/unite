package com.hta2405.unite.domain;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PostFile {
    private Long postFileId;
    private Long postId;
    private String postFilePath;
    private String postFileOriginal;
    private String postFileUUID;
    private String postFileType;
}
