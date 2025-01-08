package com.hta2405.unite.dto;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private Schedule schedule;
    private ScheduleShare scheduleShare;
}