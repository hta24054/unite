package com.hta2405.unite.dto;

import com.hta2405.unite.domain.Dept;
import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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
    private Long deptId;
    private Dept dept;

    public ScheduleShare getScheduleShare() {
        // 만약 null이면 새로운 ScheduleShare 객체를 생성해서 반환
        if (this.scheduleShare == null) {
            this.scheduleShare = new ScheduleShare();
        }
        return this.scheduleShare;
    }
}