package com.hta2405.unite.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class Dept {
    private Long deptId;
    private String deptName;
    private String deptManager;
}
