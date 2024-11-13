package com.hta2405.unite.dto;

import com.hta2405.unite.enums.DocType;
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
    private DocType docType;
    private String docTitle;
    private String docContent;
    private LocalDateTime docCreateDate;
    private boolean signFinish;
}
