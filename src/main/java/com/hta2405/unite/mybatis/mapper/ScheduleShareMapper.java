package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Schedule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleShareMapper {
    // 공유 일정 등록
    public int insertScheduleShare(Schedule schedule, com.hta2405.unite.domain.ScheduleShare scheduleShare);

    // 공유 일정 리스트
    public List<Schedule> getListSharedSchedule(String empId);

}
