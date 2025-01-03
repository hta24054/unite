package com.hta2405.unite.domain;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lang {
    private Long langId;
    private String langName;
    private String empId;
}
