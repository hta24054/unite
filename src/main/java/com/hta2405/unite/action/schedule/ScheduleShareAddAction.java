package com.hta2405.unite.action.schedule;

import java.io.IOException;
import java.time.LocalDateTime;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ScheduleDAO;
import com.hta2405.unite.dto.Schedule;
import com.hta2405.unite.dto.ScheduleShare;

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
		
		//request.getSession().setAttribute("share_emp", shareEmp);
		
		Schedule s = new Schedule();
	
		s.setEmpId(empId);
		s.setScheduleName(scheduleName);
		s.setScheduleContent(description);
		LocalDateTime startDateTime = ScheduleDateTimeUtil.parseDateTimeWithoutT(startAt);
		LocalDateTime endDateTime = ScheduleDateTimeUtil.parseDateTimeWithoutT(endAt);
		s.setScheduleStart(startDateTime);
		s.setScheduleEnd(endDateTime);
		s.setScheduleColor(bgColor);
		s.setScheduleAllDay(allDay);
		
		ScheduleShare share = new ScheduleShare();
		share.setShareEmp(shareEmp);

		ScheduleDAO sdao = new ScheduleDAO();
		
		int ok = sdao.insertScheduleShare(s, share);
		response.getWriter().print(ok);
		return null;
	}
}
