package com.hta2405.unite.action;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.hta2405.unite.dao.ScheduleDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleListAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	System.out.println("scheduleId" + request.getParameter("schedule_id")); 
    	
        
    	int scheduleId = Integer.parseInt(request.getParameter("schedule_id"));
    	//System.out.println("scheduleId" + scheduleId); 
    	
    	String empId = request.getParameter("emp_id"); 
        
        ScheduleDAO sdao = new ScheduleDAO();
        JsonArray array = sdao.getListSchedule(scheduleId, empId);

        response.setContentType("application/json;charset=utf-8");

        if (array != null) {
            response.getWriter().print(array);
        } else {
            response.getWriter().print("[]"); 
        }
        
        System.out.println(array); 
		return null;
    }
}
