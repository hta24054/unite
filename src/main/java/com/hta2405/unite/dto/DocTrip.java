package com.hta2405.unite.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DocTrip {
    private Doc doc;
    private LocalDate tripStart;
    private LocalDate tripEnd;
    private String tripLoc;
    private String tripPhone;
    private String tripInfo;
    private LocalDateTime cardStart;
    private LocalDateTime cardEnd;
    private LocalDateTime cardReturn;
}
