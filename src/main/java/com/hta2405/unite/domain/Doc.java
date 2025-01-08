package com.hta2405.unite.domain;

import com.hta2405.unite.enums.DocType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class Doc {
    private Long docId;
    private String docWriter;
    private DocType docType;
    private String docTitle;
    private String docContent;
    private LocalDateTime docCreateDate;
    private boolean signFinish;
}
