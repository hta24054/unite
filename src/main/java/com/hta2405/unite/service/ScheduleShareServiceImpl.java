package com.hta2405.unite.service;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import com.hta2405.unite.mybatis.mapper.ScheduleMapper;
import com.hta2405.unite.mybatis.mapper.ScheduleShareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public

class ScheduleShareServiceImpl implements ScheduleShareService {

    private ScheduleShareMapper scheduleShareDAO;
    private ScheduleMapper scheduleDAO;

    @Autowired
    public ScheduleShareServiceImpl(ScheduleShareMapper scheduleShareDAO, ScheduleMapper scheduleDAO) {
        this.scheduleShareDAO = scheduleShareDAO;
        this.scheduleDAO = scheduleDAO;
    }

    @Override
    public List<Schedule> getListSharedSchedule(String empId) {
        return scheduleShareDAO.getListSharedSchedule(empId);
    }


    @Override
    public int insertScheduleShare(Schedule schedule, ScheduleShare scheduleShare) {
        String empId = schedule.getEmpId();
        schedule.setEmpId(empId);
        System.out.printf("empId 값 확인", empId);
        return scheduleShareDAO.insertScheduleShare(schedule, scheduleShare);
    }

    /*
    @Override
    public int insertScheduleShare(Schedule schedule, ScheduleShare scheduleShare) {
        // 공유자 리스트 생성
        String[] shareEmpArray = scheduleShare.getShareEmp().split(","); // 쉼표로 분리
        List<String> shareEmpList = Arrays.asList(shareEmpArray);

        Map<String, Object> params = new HashMap<>();
        params.put("schedule", schedule);
        params.put("shareEmpList", shareEmpList);

        return scheduleShareDAO.insertScheduleShare(params);
    }*/

}
