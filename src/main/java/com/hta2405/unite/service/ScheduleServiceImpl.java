package com.hta2405.unite.service;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import com.hta2405.unite.dto.ScheduleDTO;
import com.hta2405.unite.mybatis.mapper.ScheduleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
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

        //System.out.println("hashMap" + hashMap);
        return scheduleDAO.insertScheduleShareUsers(hashMap);
    }

    @Override
    public List<Schedule> getListSharedSchedule(String empId) {
        return scheduleDAO.getListSharedSchedule(empId);
    }

    @Override
    public List<ScheduleShare> getScheduleSharesByScheduleId(int scheduleId) {
        return scheduleDAO.getScheduleSharesByScheduleId(scheduleId);
    }

    @Override
    public String getShareEmpNames(int scheduleId) {
        return scheduleDAO.getShareEmpNames(scheduleId);
    }

    @Override
    public String getEmpIdName(String empId) {
        return scheduleDAO.getEmpIdName(empId);
    }

    public int insertScheduleDept(ScheduleDTO scheduleDTO) {
        scheduleDAO.insertScheduleDept(scheduleDTO.getSchedule());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("scheduleId", scheduleDTO.getSchedule().getScheduleId());
        hashMap.put("deptId", scheduleDTO.getDept().getDeptId());

        System.out.println("scheduleDTO.getDept().getDeptId()" + scheduleDTO.getDept().getDeptId());

        return scheduleDAO.insertScheduleShareWithDept(hashMap);
    }

    // 사용자의 부서 ID 조회
    @Override
    public String getDeptIdByEmpId(String empId) {
        String deptId = scheduleDAO.getDeptIdByEmpId(empId);
        System.out.println("\n *** Dept ID for empId 사용자의 부서 ID 조회 = " + empId + ": " + deptId);
        return deptId;
    }

    // 부서의 일정을 조회하는 메서드 (등록자는 제외)
//    @Override
//    public List<Schedule> getListDeptSchedule(String deptId, String empId) {
//        System.out.println("\n ****** deptId: " + deptId);
//        System.out.println("\n ****** empId: " + empId);
//        List<Schedule> deptSchedules = scheduleDAO.getScheduleForDept(deptId, empId);
//        System.out.println("\n ****** Dept schedules retrieved: " + deptSchedules);
//        return deptSchedules;
//    }

    @Override
    public List<Schedule> getListDeptSchedule(String deptId, String empId) {
        System.out.println("\n ****** Dept ID =  " + deptId);
        System.out.println("\n ****** Emp ID = " + empId);
        List<Schedule> deptSchedules = scheduleDAO.getScheduleForDept(deptId, empId);
        System.out.println("\n ****** Dept schedules retrieved = " + deptSchedules);
        if (deptSchedules.isEmpty()) {
            System.out.println("\n ******  No schedules found for deptId: " + deptId + " and empId: " + empId);
        }
        return deptSchedules;
    }

}
