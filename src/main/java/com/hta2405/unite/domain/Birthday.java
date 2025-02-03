package com.hta2405.unite.domain;

import com.hta2405.unite.dto.EmpAdminUpdateDTO;
import com.hta2405.unite.dto.EmpSelfUpdateDTO;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

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
