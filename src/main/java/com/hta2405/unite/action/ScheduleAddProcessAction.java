package com.hta2405.unite.action;

import java.io.IOException;
import java.time.LocalDateTime;

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
		String startAt = request.getParameter("startAt");
		System.out.println("startAt=" + startAt);
		String endAt = request.getParameter("endAt");
		System.out.println("endAt=" + endAt);
		String description = request.getParameter("description");
		String bgColor = request.getParameter("bgColor");
		
		Schedule s = new Schedule();
		
		s.setEmpId(empId);
		s.setScheduleName(scheduleName);
		s.setScheduleContent(description);
		
		LocalDateTime startDateTime = ScheduleDateTimeUtil.parseDateTimeWithoutT(startAt);
		System.out.println(startDateTime);
		LocalDateTime endDateTime = ScheduleDateTimeUtil.parseDateTimeWithoutT(endAt);
		System.out.println(endDateTime);
		s.setScheduleStart(startDateTime);
		s.setScheduleEnd(endDateTime);
		s.setScheduleColor(bgColor);
		
		ScheduleDAO sdao = new ScheduleDAO();
		
		int ok = sdao.scheduleInsert(s);
		response.getWriter().print(ok);
		return null;
	}
}
