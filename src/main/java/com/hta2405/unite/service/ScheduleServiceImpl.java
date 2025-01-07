package com.hta2405.unite.service;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.dto.ScheduleDTO;
import com.hta2405.unite.mybatis.mapper.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleMapper scheduleDAO;

    @Autowired
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

    @Override
    public int insertScheduleShare(ScheduleDTO scheduleDTO) {
        scheduleDAO.insertScheduleShare(scheduleDTO.getSchedule());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("scheduleId", scheduleDTO.getSchedule().getScheduleId());
        //hashMap.put("shareEmp", scheduleDTO.getShareEmp());
        hashMap.put("shareEmp", scheduleDTO.getScheduleShare().getShareEmp());

        System.out.println("hashMap" + hashMap);
        return scheduleDAO.insertScheduleShareUsers(hashMap);
    }

    @Override
    public List<Schedule> getListSharedSchedule(String empId) {
        return scheduleDAO.getListSharedSchedule(empId);
    }
}
