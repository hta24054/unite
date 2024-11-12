package com.hta2405.unite.action.schedule;

import java.io.IOException;
import java.io.PrintWriter;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ScheduleDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		int scheduleId = Integer.parseInt(req.getParameter("schedule_id"));
		ScheduleDAO sdao = new ScheduleDAO();
		int result = sdao.deleteSchedule(scheduleId);
		
		resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        
        out.print(result);
        out.close();
        return null;
	}
}
