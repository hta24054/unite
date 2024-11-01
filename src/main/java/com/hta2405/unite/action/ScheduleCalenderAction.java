package com.hta2405.unite.action;

import java.io.IOException;

import com.hta2405.unite.dto.Schedule;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleCalenderAction implements Action {
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int scheduleId = Integer.parseInt(request.getParameter("schedule_id"));
		String empId = request.getParameter("emp_id");
		String schedule_name = request.getParameter("schedule_name");
		String schedule_start = request.getParameter("schedule_start");
		String schedule_end = request.getParameter("schedule_end");
		//bgColor값 추가 필요
		
		Schedule schedule = new Schedule();
		
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(false); //포워딩 방식으로 주소가 바뀌지 않아요
		forward.setPath("/WEB-INF/views/calender/calenderSchedule.jsp");
		
		return forward;
	}
}
