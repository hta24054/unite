package com.hta2405.unite.action;

import java.io.IOException;
import java.io.PrintWriter;
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
		
		int result = sdao.scheduleInsert(s);
		
		//result=0; //일정 추가 실패 확인 위한 명령
		if (result == 0) {
			System.out.println("일정 추가 실패입니다.");
			ActionForward forward = new ActionForward();
			forward.setRedirect(false);
			request.setAttribute("message", "일정 추가 실패입니다.");
			forward.setPath("/WEB-INF/views/error/error.jsp");
			return forward;
		}
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print("<script>");
		out.print("location.href='../schedule/calender';");
		out.print("</script>");
		out.close();
		return null;
	}
}
