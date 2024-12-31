package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Holiday;
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

    public AdminController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping("/holiday")
    public String showHolidayPage() {
        return "/admin/holiday";
    }

    @PostMapping("/holiday")
    @ResponseBody
    public String addCustomHoliday(LocalDate date, String holidayName) {
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

    @PostMapping("/holiday/weekend")
    @ResponseBody
    public String addWeekend() {
        LocalDate nowDate = LocalDate.now();
        LocalDate endDate = nowDate.plusYears(1);
        holidayService.addWeekend(nowDate.minusYears(1), endDate);
        return "주말을 등록하였습니다.";
    }

    @PostMapping("/holiday/api")
    @ResponseBody
    public String addHolidayWithApi() {
        holidayService.insertYearlyHoliday();
        return "공휴일을 등록하였습니다.";
    }
}
