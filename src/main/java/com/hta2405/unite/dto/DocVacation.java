package com.hta2405.unite.dto;

import com.hta2405.unite.enums.DocType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class DocVacation extends Doc{
    public DocVacation(Long docId,
                       String docWriter,
                       DocType docType,
                       String docTitle,
                       String docContent,
                       LocalDateTime docCreateDate,
                       boolean signFinish,
                       Long docVacationId,
                       LocalDate vacationApply,
                       LocalDate vacationStart,
                       LocalDate vacationEnd,
                       int vacationCount,
                       String vacationType,
                       String vacationFilePath,
                       String vacationFileOriginal,
                       String vacationFileUUID,
                       String vacationFileType) {
        super(docId, docWriter, docType, docTitle, docContent, docCreateDate, signFinish);
        this.docVacationId = docVacationId;
        this.vacationApply = vacationApply;
        this.vacationStart = vacationStart;
        this.vacationEnd = vacationEnd;
        this.vacationCount = vacationCount;
        this.vacationType = vacationType;
        this.vacationFilePath = vacationFilePath;
        this.vacationFileOriginal = vacationFileOriginal;
        this.vacationFileUUID = vacationFileUUID;
        this.vacationFileType = vacationFileType;
    }
    private Long docVacationId;
    private LocalDate vacationApply;
    private LocalDate vacationStart;
    private LocalDate vacationEnd;
    private int vacationCount;
    private String vacationType;
    private String vacationFilePath;
    private String vacationFileOriginal;
    private String vacationFileUUID;
    private String vacationFileType;
}
