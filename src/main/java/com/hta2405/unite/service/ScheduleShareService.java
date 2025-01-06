package com.hta2405.unite.service;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;

import java.util.List;

public interface ScheduleShareService {
    public List<Schedule> getListSharedSchedule(String empId);

    public int insertScheduleShare(Schedule schedule, ScheduleShare scheduleShare);
}
