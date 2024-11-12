package com.hta2405.unite.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Doc {
    private Long docId;
    private String docWriter;
    private String docType;
    private String docTitle;
    private String docContent;
    private LocalDateTime docCreateDate;
    private boolean signFinish;
}
