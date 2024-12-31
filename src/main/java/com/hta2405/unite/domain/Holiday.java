package com.hta2405.unite.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class Holiday {
    private Long holidayId;
    private LocalDate holidayDate;
    private String holidayName;
}
