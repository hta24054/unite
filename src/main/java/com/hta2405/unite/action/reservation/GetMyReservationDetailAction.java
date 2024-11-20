package com.hta2405.unite.action.reservation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ReservationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetMyReservationDetailAction implements Action {
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String empId = request.getParameter("emp_id"); // 예약자
	
		ReservationDAO reservationDAO = new ReservationDAO();
        List<Map<String, Object>> reservationList = reservationDAO.getMyReservationList(empId);
        
        ActionForward forward = new ActionForward();
		forward.setPath("/WEB-INF/views/reservation/myReservationList.jsp");
		forward.setRedirect(false);
        request.setAttribute("reservationList", reservationList);
        
        return forward;
	}
}
