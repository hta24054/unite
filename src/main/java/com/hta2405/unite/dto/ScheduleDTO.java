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
    private String scheduleStart;
    private String scheduleEnd;
    private Schedule schedule;
    private ScheduleShare scheduleShare;
}