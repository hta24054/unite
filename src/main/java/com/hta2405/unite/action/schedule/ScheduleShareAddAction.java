package com.hta2405.unite.action.schedule;

import java.io.IOException;
import java.time.LocalDateTime;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ScheduleDAO;
import com.hta2405.unite.dto.Schedule;
import com.hta2405.unite.dto.ScheduleShare;
import com.hta2405.unite.util.CalendarDateTimeUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleShareAddAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String empId = request.getParameter("emp_id");
		String scheduleName = request.getParameter("schedule_name");
		String description = request.getParameter("description");
		String startAt = request.getParameter("startAt");
		String endAt = request.getParameter("endAt");
		String bgColor = request.getParameter("bgColor");
		int allDay = request.getParameter("allDay") == null ? 0 : Integer.parseInt(request.getParameter("allDay"));
		String shareEmp = request.getParameter("share_emp"); // 공유자 ID(선택된 직원)
		
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
		
		ScheduleShare share = new ScheduleShare();
		share.setShareEmp(shareEmp);

		ScheduleDAO scheduleDao = new ScheduleDAO();
		
		int ok = scheduleDao.insertScheduleShare(schedule, share);
		response.getWriter().print(ok);
		return null;
	}
}
