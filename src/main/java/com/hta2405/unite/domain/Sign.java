package com.hta2405.unite.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class Sign {
    private Long signId;
    private String empId;
    private Long docId;
    private int signOrder;
    private LocalDateTime signTime;
}
