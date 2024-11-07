package com.hta2405.unite.action;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.hta2405.unite.dao.ScheduleDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleCalenderAction implements Action {
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 세션에서 로그인한 id값을 getAttribute로 가져온다.
		String id = (String) request.getSession().getAttribute("id");
		
		ScheduleDAO sdao = new ScheduleDAO();
        
		JsonArray array = sdao.getSchedule(id);

        response.setContentType("application/json;charset=utf-8");

        if (array != null) {
            response.getWriter().print(array);
        } else {
            response.getWriter().print("[]"); 
        }
        
        System.out.println(array); 

        ActionForward forward = new ActionForward();
        forward.setRedirect(false); 
        forward.setPath("/WEB-INF/views/schedule/scheduleCalender.jsp");

        return forward;
	}
}