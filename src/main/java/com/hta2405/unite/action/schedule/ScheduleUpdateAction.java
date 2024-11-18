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

public class ScheduleUpdateAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
			int scheduleId = Integer.parseInt(req.getParameter("schedule_id"));
			String empId = req.getParameter("emp_id");
			String scheduleName = req.getParameter("schedule_name");
			String description = req.getParameter("description");
			String startAt = req.getParameter("startAt");
			String endAt = req.getParameter("endAt");
			String bgColor = req.getParameter("bgColor");
			int allDay = req.getParameter("allDay") == null ? 0 : Integer.parseInt(req.getParameter("allDay"));
			
			Schedule schedule = new Schedule();
			
			schedule.setScheduleId(scheduleId);
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
			
			int ok = sdao.updateSchedule(schedule);
			resp.getWriter().print(ok);
			return null;
	}
}
