package com.hta2405.unite.action.schedule;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ScheduleDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleListAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	String id = (String) request.getSession().getAttribute("id");
        
        ScheduleDAO sdao = new ScheduleDAO();
        JsonArray array = sdao.getListSchedule(id);

        response.setContentType("application/json;charset=utf-8");

        if (array != null) {
            response.getWriter().print(array);
        } else {
            response.getWriter().print("[]"); 
        }
        
        System.out.println("ScheduleListAction array" + array); 
		return null;
    }
}