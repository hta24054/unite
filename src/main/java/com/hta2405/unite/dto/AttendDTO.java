package com.hta2405.unite.dto;

import com.hta2405.unite.enums.AttendType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AttendDTO {
    public AttendDTO(LocalDate attendDate) {
        this.attendDate = attendDate;
    }
    private LocalDate attendDate;
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private Duration workTime;
    private AttendType attendType;
}
