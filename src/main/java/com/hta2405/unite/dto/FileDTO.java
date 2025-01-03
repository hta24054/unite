package com.hta2405.unite.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class FileDTO {
    private String fileOriginal;
    private String filePath;
    private String fileType;
    private String fileUUID;
}
