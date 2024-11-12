package com.hta2405.unite.controller;

import com.hta2405.unite.action.schedule.ScheduleAddProcessAction;
import com.hta2405.unite.action.schedule.ScheduleCalenderAction;
import com.hta2405.unite.action.schedule.ScheduleDeleteAction;
import com.hta2405.unite.action.schedule.ScheduleDragUpdateAction;
import com.hta2405.unite.action.schedule.ScheduleListAction;
import com.hta2405.unite.action.schedule.ScheduleShareAction;
import com.hta2405.unite.action.schedule.ScheduleShareEmployAction;
import com.hta2405.unite.action.schedule.ScheduleUpdateAction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/schedule/*")
public class ScheduleFrontController extends AbstractFrontController {

    @Override
    public void init() throws ServletException {
        actionMap.put("/calender", new ScheduleCalenderAction());
        actionMap.put("/ScheduleList", new ScheduleListAction());
        actionMap.put("/ScheduleAdd", new ScheduleAddProcessAction());
        actionMap.put("/ScheduleUpdate", new ScheduleUpdateAction());
        actionMap.put("/ScheduleDragUpdate", new ScheduleDragUpdateAction());
        actionMap.put("/ScheduleDelete", new ScheduleDeleteAction());
        
        // 공유 일정 등록
        actionMap.put("/scheduleShare", new ScheduleShareAction()); 
        actionMap.put("/ScheduleShareEmploy", new ScheduleShareEmployAction()); 
        
    }
}

