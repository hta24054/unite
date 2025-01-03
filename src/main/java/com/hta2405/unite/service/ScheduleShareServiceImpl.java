package com.hta2405.unite.service;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import com.hta2405.unite.mybatis.mapper.ScheduleShareMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleShareServiceImpl implements ScheduleShareService {

    private ScheduleShareMapper scheduleShareDAO;

    public ScheduleShareServiceImpl(ScheduleShareMapper scheduleShareDAO) {
        this.scheduleShareDAO = scheduleShareDAO;
    }

    @Override
    public List<Schedule> getListSharedSchedule(String empId) {
        return scheduleShareDAO.getListSharedSchedule(empId);
    }

    @Override
    public int insertScheduleShare(Schedule schedule, ScheduleShare scheduleShare) {
        return scheduleShareDAO.insertScheduleShare(schedule, scheduleShare);
    }
}
