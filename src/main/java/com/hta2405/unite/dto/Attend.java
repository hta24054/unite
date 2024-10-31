package com.hta2405.unite.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Attend {
    private int attendId;
    private int empId;
    private LocalDate attendDate;
    private LocalDateTime attendIn;
    private LocalDateTime attendOut;
    private String attendType; //일반, 출장, 휴가
}
