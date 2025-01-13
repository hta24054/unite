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

    int getAnnualAppliedVacationCount(String empId, int year);

    void insertVacation(String empId, List<LocalDate> list, AttendType vacationType);

    void insertTrip(String empId, List<LocalDate> list);

    void deleteAllAttend(String empId, List<LocalDate> list);
}
