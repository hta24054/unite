package com.hta2405.unite.dto;

import com.hta2405.unite.enums.AttendType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VacationDTO {
    private int vacationCount;
    private AttendType vacationType;
    private LocalDate docCreateDate;
    private String docContent;
    private LocalDate vacationStart;
    private LocalDate vacationEnd;
}
