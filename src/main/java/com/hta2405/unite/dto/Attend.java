package com.hta2405.unite.dto;

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
    private String attendType; //일반, 출장, 휴가

    public Attend(LocalDate attendDate) {
        this.attendDate = attendDate;
    }
}
