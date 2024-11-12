package com.hta2405.unite.controller;

import com.hta2405.unite.action.schedule.ScheduleAddProcessAction;
import com.hta2405.unite.action.schedule.ScheduleCalenderAction;
import com.hta2405.unite.action.schedule.ScheduleListAction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/schedule/*")
public class ScheduleFrontController extends AbstractFrontController {

    @Override
    public void init() throws ServletException {
        actionMap.put("/calender", new ScheduleCalenderAction());
        actionMap.put("/ScheduleAddProcessAction", new ScheduleAddProcessAction());
        actionMap.put("/ScheduleListAction", new ScheduleListAction());
    }
}
