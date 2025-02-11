package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Holiday;
import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.dto.ScheduleDTO;
import com.hta2405.unite.service.HolidayService;
import com.hta2405.unite.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/schedule")
public class ScheduleController {
    private ScheduleService scheduleService;
    private HolidayService holidayService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, HolidayService holidayService) {
        this.scheduleService = scheduleService;
        this.holidayService = holidayService;
    }

    @GetMapping("/calender")
    public String calender() {
        return "schedule/scheduleCalender";
    }

    @ResponseBody
    @GetMapping("/scheduleList")
    public List<Schedule> getListSchedule(@AuthenticationPrincipal UserDetails user, String startDate, String endDate) {
        String empId = user.getUsername();
        return scheduleService.getListSchedule(empId, startDate, endDate);
    }

    @ResponseBody
    @PostMapping("/scheduleAdd")
    public int insertSchedule(Schedule schedule) {
        return scheduleService.insertSchedule(schedule);
    }

    @ResponseBody
    @PostMapping("/scheduleUpdate")
    public int updateSchedule(Schedule schedule) {
        return scheduleService.updateSchedule(schedule);
    }

    @ResponseBody
    @PostMapping("/scheduleDragUpdate")
    public int dragUpdateSchedule(Schedule schedule) {
        return scheduleService.dragUpdateSchedule(schedule);
    }

    @ResponseBody
    @PostMapping("/scheduleDelete")
    public int deleteSchedule(int scheduleId) {
        return scheduleService.deleteSchedule(scheduleId);
    }

    // 공유 일정 등록 페이지
    @GetMapping("/scheduleShare")
    public String shareSchedule(Schedule schedule) {
        return "schedule/scheduleShare";
    }

    // 공유 일정 불러오기
    @ResponseBody
    @GetMapping("/sharedScheduleList")
    public List<ScheduleDTO> getListSharedSchedule(@AuthenticationPrincipal UserDetails user) {
        String empId = user.getUsername();
        return scheduleService.getSharedSchedules(empId);
    }

    // 공유 일정 등록
    @ResponseBody
    @PostMapping("/scheduleShareAdd")
    public int insertScheduleShare(@RequestBody ScheduleDTO scheduleDTO) {
        return scheduleService.insertScheduleShare(scheduleDTO);
    }

    // 부서 일정 등록 페이지
    @GetMapping("/scheduleDept")
    public String scheduleDept(Schedule schedule) {
        return "schedule/scheduleDept";
    }

    // 부서 일정 등록
    @ResponseBody
    @PostMapping("/scheduleDeptAdd")
    public int insertScheduleDept(@RequestBody ScheduleDTO scheduleDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String empId = authentication.getName();
        scheduleDTO.setEmpId(empId);

        return scheduleService.insertScheduleDept(scheduleDTO);
    }

    @ResponseBody
    @GetMapping("/deptScheduleList")
    public List<ScheduleDTO> getListDeptSchedule(@AuthenticationPrincipal UserDetails user) {
        String empId = user.getUsername();
        List<ScheduleDTO> scheduleDTOList = scheduleService.getScheduleDTOList(empId);
        return scheduleDTOList;
    }

    // 공휴일 불러오기
    @ResponseBody
    @GetMapping("/getHoliday")
    public List<Holiday> getHoliday(int year, int month) {

        // 공휴일 목록 불러오기
        List<Holiday> holidayList = holidayService.getCalendarHolidayList(year, month);

        // 토요일, 일요일 제외
        return holidayList.stream()
                .filter(holiday -> !(holiday.getHolidayName().equals("토요일") || holiday.getHolidayName().equals("일요일")))
                .collect(Collectors.toList());
    }
}
