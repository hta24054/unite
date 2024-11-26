package com.hta2405.unite.controller;

import com.hta2405.unite.action.schedule.ScheduleAddProcessAction;
import com.hta2405.unite.action.schedule.ScheduleCalenderAction;
import com.hta2405.unite.action.schedule.ScheduleDeleteAction;
import com.hta2405.unite.action.schedule.ScheduleDragUpdateAction;
import com.hta2405.unite.action.schedule.ScheduleGetHolidayAction;
import com.hta2405.unite.action.schedule.ScheduleListAction;
import com.hta2405.unite.action.schedule.ScheduleShareAction;
import com.hta2405.unite.action.schedule.ScheduleShareAddAction;
import com.hta2405.unite.action.schedule.ScheduleUpdateAction;
import com.hta2405.unite.action.schedule.SharedScheduleListAction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/schedule/*")
public class ScheduleFrontController extends AbstractFrontController {

    @Override
    public void init() throws ServletException {
        actionMap.put("/calender", ScheduleCalenderAction::new);
        actionMap.put("/scheduleList", ScheduleListAction::new);
        actionMap.put("/scheduleAdd", ScheduleAddProcessAction::new);
        actionMap.put("/scheduleUpdate", ScheduleUpdateAction::new);
        actionMap.put("/scheduleDragUpdate", ScheduleDragUpdateAction::new);
        actionMap.put("/scheduleDelete", ScheduleDeleteAction::new);
        
        // 공유 일정 등록
        actionMap.put("/scheduleShare", ScheduleShareAction::new);
        actionMap.put("/sharedScheduleList", SharedScheduleListAction::new);
        actionMap.put("/scheduleShareAdd", ScheduleShareAddAction::new);
        
        // 공휴일
        actionMap.put("/getHoliday", ScheduleGetHolidayAction::new);
    }
}

