package com.hta2405.unite.action.reservation;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ReservationWeekCalenderAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ActionForward forward = new ActionForward();
	    forward.setRedirect(false); 
	    forward.setPath("/WEB-INF/views/reservation/weekCalendar.jsp");

	    return forward;
	}
}
