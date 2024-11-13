package com.hta2405.unite.controller;

import com.hta2405.unite.action.schedule.ScheduleAddProcessAction;
import com.hta2405.unite.action.schedule.ScheduleCalenderAction;
import com.hta2405.unite.action.schedule.ScheduleDeleteAction;
import com.hta2405.unite.action.schedule.ScheduleDragUpdateAction;
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
        actionMap.put("/calender", new ScheduleCalenderAction());
        actionMap.put("/scheduleList", new ScheduleListAction());
        actionMap.put("/scheduleAdd", new ScheduleAddProcessAction());
        actionMap.put("/scheduleUpdate", new ScheduleUpdateAction());
        actionMap.put("/scheduleDragUpdate", new ScheduleDragUpdateAction());
        actionMap.put("/scheduleDelete", new ScheduleDeleteAction());
        
        // 공유 일정 등록
        actionMap.put("/scheduleShare", new ScheduleShareAction());
        actionMap.put("/sharedScheduleList", new SharedScheduleListAction()); 
        actionMap.put("/scheduleShareAdd", new ScheduleShareAddAction()); 
    }
}

