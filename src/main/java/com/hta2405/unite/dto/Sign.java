package com.hta2405.unite.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sign {
    private Long signId;
    private String empId;
    private Long docId;
    private int signOrder;
    private LocalDateTime signTime;
}
