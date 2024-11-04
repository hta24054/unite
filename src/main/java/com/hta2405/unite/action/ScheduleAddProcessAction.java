package com.hta2405.unite.action;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.hta2405.unite.dao.ScheduleDAO;
import com.hta2405.unite.dto.Schedule;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleAddProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		String empId = request.getParameter("emp_id");
		String scheduleName = request.getParameter("schedule_name");
		String scheduleContent = request.getParameter("schedule_content");
		String scheduleStart = request.getParameter("schedule_start");
		String scheduleEnd = request.getParameter("schedule_end");
		String scheduleColor = request.getParameter("schedule_color");
		
		Schedule s = new Schedule();
		
		s.setEmpId(empId);
		s.setScheduleName(scheduleName);
		s.setScheduleContent(scheduleContent);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startDateTime = LocalDateTime.parse(scheduleStart, formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(scheduleEnd, formatter);
		
		s.setScheduleStart(startDateTime);
		s.setScheduleEnd(endDateTime);
		s.setScheduleColor(scheduleColor);
		
		ScheduleDAO sdao = new ScheduleDAO();
		
		int ok = sdao.scheduleInsert(s);
		response.getWriter().print(ok);
		return null;
	}
}
