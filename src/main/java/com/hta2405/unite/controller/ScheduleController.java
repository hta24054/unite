package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Schedule;
import com.hta2405.unite.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
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
    public List<Schedule> getListSchedule(HttpSession session) {
        String id = (String) session.getAttribute("id");

        List<Schedule> schedules = scheduleService.getListSchedule(id);
        return schedules;
    }

    @ResponseBody
    @PostMapping("/scheduleAdd")
    public String scheduleAdd(Schedule schedule) {
        scheduleService.insertSchedule(schedule);
        return "redirect:calender";
    }


}
