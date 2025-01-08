package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import com.hta2405.unite.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ScheduleMapper {

    // 일정 리스트
    public List<Schedule> getListSchedule(String id);

    // 일정 등록
    public int insertSchedule(Schedule schedule);

    // 일정 수정
    public int updateSchedule(Schedule schedule);

    // 일정 수정(드래그)
    public int dragUpdateSchedule(Schedule schedule);

    // 일정 삭제
    public int deleteSchedule(int scheduleId);

    // 공유 일정 등록
    public int insertScheduleShareUsers(HashMap<String, Object> hashMap);

    // 공유 일정 등록
    public void insertScheduleShare(Schedule schedule);

    //public List<Schedule> getSharedSchedule(String empId, HashMap<String, Object> hashMap);

    // 공유 일정 리스트
    public List<Schedule> getListSharedSchedule(String empId);

    public List<ScheduleShare> getScheduleSharesByScheduleId(int scheduleId);

    public String getShareEmpNames(int scheduleId);

    public String getEmpIdName(String empId);

    // 공휴일 리스트
    // public List<Holiday> getHoliday(LocalDate startDate, LocalDate endDate);


}
