package com.hta2405.unite.domain;

import lombok.Data;

@Data
public class ScheduleShare {
    private int scheduleShareId; //일정공유ID
    private String shareEmp;     //공유직원
    private int scheduleId;      //일정코드
    private Long deptId;         //부서ID
}
