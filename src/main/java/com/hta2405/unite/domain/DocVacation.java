package com.hta2405.unite.domain;

import com.hta2405.unite.enums.AttendType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;


@Getter
@AllArgsConstructor
@ToString
@Builder
public class DocVacation {
    private Long docVacationId;
    private Long docId;
    private LocalDate vacationStart;
    private LocalDate vacationEnd;
    private AttendType vacationType;
    private int vacationCount;
    private String vacationFilePath;
    private String vacationFileOriginal;
    private String vacationFileUUID;
    private String vacationFileType;
}
