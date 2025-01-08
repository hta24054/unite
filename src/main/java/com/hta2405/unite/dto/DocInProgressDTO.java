package com.hta2405.unite.dto;

import com.hta2405.unite.enums.DocType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DocInProgressDTO {
    private Long docId;
    private LocalDateTime createDate;
    private DocType docType;
    private String docTitle;
    private String currSignerName;
}
