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
		
		Schedule schedule = new Schedule();
		schedule.setScheduleId(scheduleId);
		schedule.setEmpId(empId);
		LocalDateTime startDateTime = ScheduleDateTimeUtil.parseDateTimeWithoutT(startAt);
		LocalDateTime endDateTime = ScheduleDateTimeUtil.parseDateTimeWithoutT(endAt);
		schedule.setScheduleStart(startDateTime);
		schedule.setScheduleEnd(endDateTime);
		schedule.setScheduleAllDay(allDay);
		
		ScheduleDAO scheduleDao = new ScheduleDAO();
		
		int ok = scheduleDao.dragUpdateSchedule(schedule);
		resp.getWriter().print(ok);
		return null;
	}
}
