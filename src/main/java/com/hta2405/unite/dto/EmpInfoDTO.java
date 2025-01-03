package com.hta2405.unite.dto;

import com.hta2405.unite.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class EmpInfoDTO {
    String loginEmpId;
    private Emp emp;
    private List<Lang> langList;
    private List<Cert> certList;
    private List<Job> jobList;
    private List<Dept> deptList;
}
