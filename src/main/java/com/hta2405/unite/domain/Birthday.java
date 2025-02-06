package com.hta2405.unite.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Birthday {
    private String empId;
    private String eName;
    private String deptName;
    private String jobName;
}
