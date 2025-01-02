package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Holiday;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface HolidayMapper {
    int insertHoliday(Holiday holiday);

    String getHolidayName(LocalDate date);

    List<Holiday> getHolidayList(@Param("startDate") LocalDate statDate, @Param("endDate") LocalDate endDate);

    int deleteHoliday(LocalDate date);
}
