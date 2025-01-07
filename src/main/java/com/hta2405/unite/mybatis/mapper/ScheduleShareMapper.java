package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScheduleShareMapper {
    // 공유 일정 등록
    public int insertScheduleShare(@Param("schedule") Schedule schedule, @Param("shareEmp") ScheduleShare scheduleShare);

    // 공유 일정 리스트
    public List<Schedule> getListSharedSchedule(String empId);


}
