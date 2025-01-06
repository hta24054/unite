package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.domain.ScheduleShare;
import com.hta2405.unite.dto.ScheduleDTO;
import com.hta2405.unite.service.ScheduleShareService;
import com.hta2405.unite.util.CalendarDateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/schedule")
public class ScheduleShareController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleShareController.class);

    private ScheduleShareService scheduleShareService;

    public ScheduleShareController(ScheduleShareService scheduleShareService) {
        this.scheduleShareService = scheduleShareService;
    }

    @GetMapping("/scheduleShare")
    public String shareSchedule(Schedule schedule) {
        return "schedule/scheduleShare";
    }

    @ResponseBody
    @GetMapping("/sharedScheduleList")
    public List<Schedule> getListSharedSchedule() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ArrayList<>();
        }
        String empId = authentication.getName(); // 로그인한 사용자의 ID (username)
        //System.out.println("공유 일정 empId " + empId);

        List<Schedule> sharedSchedules = scheduleShareService.getListSharedSchedule(empId);
        System.out.println("공유 일정 " + sharedSchedules);

        return sharedSchedules;
    }

    @ResponseBody
    @PostMapping("/scheduleShareAdd")
    public HashMap<String, Object> insertScheduleShare(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleDTO.getSchedule();
        ScheduleShare scheduleShare = scheduleDTO.getScheduleShare();

        System.out.println("Schedule ID: " + schedule.getScheduleId());

        // CalendarDateTimeUtil을 사용하여 날짜 변환
        if (schedule != null) {
            String scheduleStartStr = scheduleDTO.getScheduleStart();
            String scheduleEndStr = scheduleDTO.getScheduleEnd();

            // "T" 제거 후 LocalDateTime으로 변환
            schedule.setScheduleStart(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleStartStr));
            schedule.setScheduleEnd(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleEndStr));
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("schedule", schedule);
        map.put("scheduleShare", scheduleShare);

        return map;


    }

    /*
    @ResponseBody
    @PostMapping("/scheduleShareAdd")
    public int insertScheduleShare(@RequestBody ScheduleDTO scheduleDTO) {

        Schedule schedule = scheduleDTO.getSchedule();
        ScheduleShare scheduleShare = scheduleDTO.getScheduleShare();

        // CalendarDateTimeUtil을 사용하여 날짜 변환
        if (schedule != null) {
            String scheduleStartStr = scheduleDTO.getScheduleStart();
            String scheduleEndStr = scheduleDTO.getScheduleEnd();

            // "T" 제거 후 LocalDateTime으로 변환
            schedule.setScheduleStart(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleStartStr));
            schedule.setScheduleEnd(CalendarDateTimeUtil.parseDateTimeWithoutT(scheduleEndStr));
        }

        System.out.println("Received JSON: " + scheduleDTO);

        int result = scheduleShareService.insertScheduleShare(schedule, scheduleShare);
        System.out.println("공유 일정 등록: " + result);

        return result;
    }
    */
}
