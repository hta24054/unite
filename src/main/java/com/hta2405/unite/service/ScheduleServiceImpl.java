package com.hta2405.unite.service;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.mybatis.mapper.ScheduleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleMapper dao;
    public ScheduleServiceImpl(ScheduleMapper schedule) {
        this.dao = schedule;
    }

    @Override
    public List<Schedule> getListSchedule(String id) {
        return dao.getListSchedule(id);
    }

    @Override
    public int insertSchedule(Schedule schedule) {
        return dao.insertSchedule(schedule);
    }

    @Override
    public int updateSchedule(Schedule schedule) {
        return dao.updateSchedule(schedule);
    }

    @Override
    public int dragUpdateSchedule(Schedule schedule) {
        return dao.dragUpdateSchedule(schedule);
    }

    @Override
    public int deleteSchedule(int scheduleId) {
        return dao.deleteSchedule(scheduleId);
    }

    @Override
    public List<Schedule> getListSharedSchedule(String empId) {
        return dao.getListSharedSchedule(empId);
    }


}
