package com.hta2405.unite.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Holiday {
    private Long holidayId;
    private LocalDate holidayDate;
    private String holidayName;
}
