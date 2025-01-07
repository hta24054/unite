package com.hta2405.unite.domain;

import com.hta2405.unite.enums.AttendType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class Attend {
    private Long attendId;
    private String empId;
    private LocalDate attendDate;
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private AttendType attendType;
}
