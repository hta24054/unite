package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Holiday;
import com.hta2405.unite.mybatis.mapper.HolidayMapper;
import com.hta2405.unite.service.HolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final HolidayService holidayService;
    private final HolidayMapper holidayMapper;

    public AdminController(HolidayService holidayService, HolidayMapper holidayMapper) {
        this.holidayService = holidayService;
        this.holidayMapper = holidayMapper;
    }

    @GetMapping("/holiday")
    public String showHolidayPage() {
        return "/admin/holiday";
    }

    @PostMapping("/holiday")
    @ResponseBody
    public String addHoliday(LocalDate date, String holidayName) {
        Holiday holiday = Holiday.builder().holidayDate(date).holidayName(holidayName).build();
        return holidayService.addHoliday(holiday);
    }

    @DeleteMapping("/holiday")
    @ResponseBody
    public String deleteHoliday(LocalDate date) {
        return holidayService.deleteHoliday(date);
    }


    @GetMapping("/holiday/list")
    @ResponseBody
    public List<Holiday> getHolidayList(String start, String end, ModelAndView mv) {
        String[] startString = start.split("-");
        String[] endString = end.split("-");

        LocalDate startDate = LocalDate.of(Integer.parseInt(startString[0]), Integer.parseInt(startString[1]), 1);
        YearMonth endYearMonth = YearMonth.of(Integer.parseInt(endString[0]), Integer.parseInt(endString[1]));
        LocalDate endDate = endYearMonth.atEndOfMonth();

        return holidayService.getHolidayList(startDate, endDate);
    }


}
