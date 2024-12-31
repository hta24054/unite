package com.hta2405.unite.service;

import com.hta2405.unite.domain.Holiday;
import com.hta2405.unite.mybatis.mapper.HolidayMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HolidayService {
    private final HolidayMapper holidayMapper;

    public HolidayService(HolidayMapper holidayMapper) {
        this.holidayMapper = holidayMapper;
    }

    public List<Holiday> getHolidayList(LocalDate startDate, LocalDate endDate) {
        return holidayMapper.getHolidayList(startDate, endDate);
    }

    public String addHoliday(Holiday holiday) {
        if (holidayMapper.getHolidayName(holiday.getHolidayDate()) != null) {
            return "휴일 등록을 실패하였습니다. 이미 등록된 휴일입니다.";
        }
        int result = holidayMapper.insertHoliday(holiday);
        if (result != 1) {
            return "휴일 등록 실패";
        }
        return "휴일 등록 성공";
    }

    public String deleteHoliday(LocalDate date) {
        int result = holidayMapper.deleteHoliday(date);
        if (result != 1) {
            return "휴일 삭제를 실패하였습니다.";
        }
        return "휴일을 삭제하였습니다.";
    }

}
