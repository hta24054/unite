package com.hta2405.unite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AttendInfoDTO {
    private String targetEmpName;
    private List<AttendDTO> attendDTOList;
    private int allWork;
    private int myWork;
    private int vacation;
    private int absent;
    private int lateOrLeaveEarly;
}
