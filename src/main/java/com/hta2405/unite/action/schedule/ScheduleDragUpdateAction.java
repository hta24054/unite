package com.hta2405.unite.action.schedule;

import java.io.IOException;
import java.time.LocalDateTime;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ScheduleDAO;
import com.hta2405.unite.dto.Schedule;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleDragUpdateAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		int scheduleId = Integer.parseInt(req.getParameter("schedule_id"));
		String empId = req.getParameter("emp_id");
		String startAt = req.getParameter("startAt");
		String endAt = req.getParameter("endAt");
		int allDay = req.getParameter("allDay") == null ? 0 : Integer.parseInt(req.getParameter("allDay"));
		
		Schedule s = new Schedule();
		
		s.setScheduleId(scheduleId);
		s.setEmpId(empId);
		LocalDateTime startDateTime = ScheduleDateTimeUtil.parseDateTimeWithoutT(startAt);
		LocalDateTime endDateTime = ScheduleDateTimeUtil.parseDateTimeWithoutT(endAt);
		s.setScheduleStart(startDateTime);
		s.setScheduleEnd(endDateTime);
		s.setScheduleAllDay(allDay);
		
		ScheduleDAO sdao = new ScheduleDAO();
		
		int ok = sdao.dragUpdateSchedule(s);
		resp.getWriter().print(ok);
		return null;
	}
}
