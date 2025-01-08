package com.hta2405.unite.dto;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ScheduleDTO {
    private Long scheduleId;
    private String empId;
    private String scheduleName;
    private String scheduleContent;
    private String scheduleStart;
    private String scheduleEnd;
    private String scheduleColor;
    private boolean scheduleAllDay;
    private String shareEmp;
    private String shareEmpNames;
    private String empIdName;
    private Schedule schedule;
    private ScheduleShare scheduleShare;
}