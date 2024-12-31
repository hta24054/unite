package com.hta2405.unite.service;

import com.hta2405.unite.domain.Schedule;

import java.util.List;

public interface ScheduleService {

    public List<Schedule> getListSchedule(String id);

    public int insertSchedule(Schedule schedule);

}
