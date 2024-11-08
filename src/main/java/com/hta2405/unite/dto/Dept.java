package com.hta2405.unite.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Dept {
    private Long deptId;
    private String deptName;
    private String deptManager;
}
