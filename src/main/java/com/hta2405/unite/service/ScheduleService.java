package com.hta2405.unite.service;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import com.hta2405.unite.dto.ScheduleDTO;

import java.util.List;

public interface ScheduleService {

    public List<Schedule> getListSchedule(String id);

    public int insertSchedule(Schedule schedule);

    public int updateSchedule(Schedule schedule);

    public int dragUpdateSchedule(Schedule schedule);

    public int deleteSchedule(int scheduleId);

    public int insertScheduleShare(ScheduleDTO scheduleDTO);

    public List<Schedule> getListSharedSchedule(String empId);

    // scheduleId에 해당하는 ScheduleShare 리스트 가져오는 메서드 추가
    public List<ScheduleShare> getScheduleSharesByScheduleId(int scheduleId);

    public String getShareEmpNames(int scheduleId);

    public String getEmpIdName(String empId);

    // 부서 일정 등록
    public int insertScheduleDept(ScheduleDTO scheduleDTO);
}
