package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Attend;
import com.hta2405.unite.enums.AttendType;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AttendMapper {
    List<Attend> getAttendListByEmpId(String empId, LocalDate startDate, LocalDate endDate);

    Attend getAttendByEmpId(String empId, LocalDate date);

    int attendIn(String empId, AttendType attendType);

    int attendOut(String empId);
}
