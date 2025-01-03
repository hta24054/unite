package com.hta2405.unite.service;

import com.hta2405.unite.domain.Schedule;

import java.util.List;

public interface ScheduleService {

    public List<Schedule> getListSchedule(String id);

    public int insertSchedule(Schedule schedule);

    public int updateSchedule(Schedule schedule);

    public int dragUpdateSchedule(Schedule schedule);

    public int deleteSchedule(int scheduleId);

    public List<Schedule> getListSharedSchedule(String empId);
}
