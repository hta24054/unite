package com.hta2405.unite.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class EmpTreeDTO {
    private String empId;
    private String ename;
    private String jobName;
    private String mobile;
    private String tel;
    private String email;
    private String deptName;
}
