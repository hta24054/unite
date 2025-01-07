package com.hta2405.unite.service;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.mybatis.mapper.ScheduleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleMapper scheduleDAO;

    public ScheduleServiceImpl(ScheduleMapper scheduleDAO) {
        this.scheduleDAO = scheduleDAO;
    }

    @Override
    public List<Schedule> getListSchedule(String id) {
        return scheduleDAO.getListSchedule(id);
    }

    @Override
    public int insertSchedule(Schedule schedule) {
        return scheduleDAO.insertSchedule(schedule);
    }

    @Override
    public int updateSchedule(Schedule schedule) {
        return scheduleDAO.updateSchedule(schedule);
    }

    @Override
    public int dragUpdateSchedule(Schedule schedule) {
        return scheduleDAO.dragUpdateSchedule(schedule);
    }

    @Override
    public int deleteSchedule(int scheduleId) {
        return scheduleDAO.deleteSchedule(scheduleId);
    }
}
