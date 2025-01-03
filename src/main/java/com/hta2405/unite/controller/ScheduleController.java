package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Controller
@Slf4j
@RequestMapping("/schedule")
public class ScheduleController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/calender")
    public String calender() {
        return "schedule/scheduleCalender";
    }

    @ResponseBody
    @GetMapping("/scheduleList")
    public List<Schedule> getListSchedule() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("로그인되지 않은 사용자입니다.");
        }

        String id = authentication.getName(); // 로그인한 사용자의 ID (username)

        List<Schedule> schedules = scheduleService.getListSchedule(id);
        //System.out.println("schedules " + schedules);

        return schedules;
    }

    @ResponseBody
    @PostMapping("/scheduleAdd")
    public int insertSchedule(Schedule schedule) {
        int result = scheduleService.insertSchedule(schedule);

        //System.out.println("schedule 등록 " + schedule);
        return result;
    }

    @ResponseBody
    @PostMapping("/scheduleUpdate")
    public int updateSchedule(Schedule schedule) {
        int result =  scheduleService.updateSchedule(schedule);
        //System.out.println("schedule 수정 " + schedule);
        return result;
    }

    @ResponseBody
    @PostMapping("/scheduleDragUpdate")
    public int dragUpdateSchedule(Schedule schedule) {
        int result =  scheduleService.dragUpdateSchedule(schedule);
        System.out.println("schedule 드래그로 수정 " + schedule);
        return result;
    }


}
