package com.hta2405.unite.action.schedule;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ScheduleShareAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ActionForward forward = new ActionForward();
        forward.setRedirect(false); 
        forward.setPath("/WEB-INF/views/schedule/scheduleShare.jsp");

        return forward;
	}
}
