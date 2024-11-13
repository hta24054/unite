package com.hta2405.unite.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocVacation extends Doc{
    private LocalDateTime vacationApply;
    private LocalDateTime vacationStart;
    private LocalDateTime vacationEnd;
    private String vacationFilePath;
    private String vacationFileOriginal;
    private String vacationFileUUID;
    private String vacationFileType;
}
