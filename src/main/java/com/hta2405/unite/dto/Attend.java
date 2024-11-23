package com.hta2405.unite.dto;

import com.hta2405.unite.enums.AttendType;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Attend {
    private Integer attendId;
    private String empId;
    private LocalDate attendDate;
    private LocalDateTime attendIn;
    private LocalDateTime attendOut;
    private Duration workTime;
    private AttendType attendType; //일반, 출장, 외근, 휴가...

    public Attend(LocalDate attendDate) {
        this.attendDate = attendDate;
    }
}
