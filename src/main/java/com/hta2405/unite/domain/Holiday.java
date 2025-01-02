package com.hta2405.unite.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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
