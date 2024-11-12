package com.hta2405.unite.controller;

import com.hta2405.unite.action.schedule.ScheduleAddProcessAction;
import com.hta2405.unite.action.schedule.ScheduleCalenderAction;
import com.hta2405.unite.action.schedule.ScheduleDeleteAction;
import com.hta2405.unite.action.schedule.ScheduleDragUpdateAction;
import com.hta2405.unite.action.schedule.ScheduleListAction;
import com.hta2405.unite.action.schedule.ScheduleUpdateAction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/schedule/*")
public class ScheduleFrontController extends AbstractFrontController {

    @Override
    public void init() throws ServletException {
        actionMap.put("/calender", new ScheduleCalenderAction());
        actionMap.put("/ScheduleListAction", new ScheduleListAction());
        actionMap.put("/ScheduleAddProcessAction", new ScheduleAddProcessAction());
        actionMap.put("/ScheduleUpdateAction", new ScheduleUpdateAction());
        actionMap.put("/ScheduleDragUpdateAction", new ScheduleDragUpdateAction());
        actionMap.put("/ScheduleDeleteAction", new ScheduleDeleteAction());
    }
}

