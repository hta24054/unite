package com.hta2405.unite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DocWriteDTO {
    private String empId;
    private String ename;
    private String deptName;
    private LocalDate date;
}
