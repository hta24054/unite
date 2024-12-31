package com.hta2405.unite.service;

import com.hta2405.unite.domain.Holiday;
import com.hta2405.unite.mybatis.mapper.HolidayMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminService {
    private final HolidayMapper holidayMapper;

    public AdminService(HolidayMapper holidayMapper) {
        this.holidayMapper = holidayMapper;
    }

    public List<Holiday> getHolidayList(LocalDate startDate, LocalDate endDate) {
        return holidayMapper.getHolidayList(startDate, endDate);
    }
}
