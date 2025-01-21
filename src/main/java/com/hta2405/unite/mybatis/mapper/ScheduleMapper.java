package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface ScheduleMapper {

    // 일정 리스트
    public List<Schedule> getListSchedule(String id);


    // 오늘 일정 리스트
    List<Schedule> getDailyScheduleList(String empId, LocalDate date);

    // 일정 등록
    public int insertSchedule(Schedule schedule);

    // 일정 수정
    public int updateSchedule(Schedule schedule);

    // 일정 수정(드래그)
    public int dragUpdateSchedule(Schedule schedule);

    // 일정 삭제
    public int deleteSchedule(int scheduleId);

    // 공유 일정 등록
    public void insertScheduleShare(Schedule schedule);

    public int insertScheduleShareUsers(HashMap<String, Object> hashMap);

    // 공유 일정 리스트
    public List<Schedule> getListSharedSchedule(String empId);

    public List<ScheduleShare> getScheduleSharesByScheduleId(int scheduleId);

    public String getShareEmpNames(int scheduleId);

    public String getEmpIdName(String empId);

    // 부서 일정 등록
    public int insertScheduleDept(Schedule schedule);

    public int insertScheduleShareWithDept(HashMap<String, Object> hashMap);

    public String getDeptIdByEmpId(String empId);

    public List<Schedule> getScheduleForDept(String deptId, String empId);

    // 공휴일 리스트
    // public List<Holiday> getHoliday(LocalDate startDate, LocalDate endDate);


}
