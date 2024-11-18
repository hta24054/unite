package com.hta2405.unite.action.schedule;

import java.io.IOException;
import java.time.LocalDateTime;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ScheduleDAO;
import com.hta2405.unite.dto.Schedule;
import com.hta2405.unite.util.CalendarDateTimeUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleAddProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		String empId = request.getParameter("emp_id");
		String scheduleName = request.getParameter("schedule_name");
		String description = request.getParameter("description");
		String startAt = request.getParameter("startAt");
		String endAt = request.getParameter("endAt");
		//System.out.println("startAt=" + startAt);
		//System.out.println("endAt=" + endAt);
		String bgColor = request.getParameter("bgColor");
		int allDay = request.getParameter("allDay") == null ? 0 : Integer.parseInt(request.getParameter("allDay"));
		
		Schedule schedule = new Schedule();
		
		schedule.setEmpId(empId);
		schedule.setScheduleName(scheduleName);
		schedule.setScheduleContent(description);
		//LocalDateTime startDateTime = ScheduleDateTimeUtil.parseDateTimeWithoutT(startAt);
		//LocalDateTime endDateTime = ScheduleDateTimeUtil.parseDateTimeWithoutT(endAt);
		LocalDateTime startDateTime = CalendarDateTimeUtil.parseDateTimeWithoutT(startAt);
		LocalDateTime endDateTime = CalendarDateTimeUtil.parseDateTimeWithoutT(endAt);
		
		schedule.setScheduleStart(startDateTime);
		schedule.setScheduleEnd(endDateTime);
		schedule.setScheduleColor(bgColor);
		schedule.setScheduleAllDay(allDay);
		
		ScheduleDAO sdao = new ScheduleDAO();
		
		int ok = sdao.insertSchedule(schedule);
		response.getWriter().print(ok);
		return null;
	}
}