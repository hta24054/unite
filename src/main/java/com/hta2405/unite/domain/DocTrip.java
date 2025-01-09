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
public class DocTrip {
    private Long docTripId;
    private Long docId;
    private LocalDate tripStart;
    private LocalDate tripEnd;
    private String tripLoc;
    private String tripPhone;
    private String tripInfo;
    private LocalDate cardStart;
    private LocalDate cardEnd;
    private LocalDate cardReturn;
}
