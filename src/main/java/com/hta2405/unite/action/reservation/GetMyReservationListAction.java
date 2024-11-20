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

public class GetMyReservationListAction implements Action {
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("request.getParameter(\"emp_id\")" + request.getParameter("emp_id"));
		System.out.println("request.getParameter(\"reservation_id\")" + request.getParameter("reservation_id"));
		
		String empId = request.getParameter("emp_id"); // 예약자
		String reservationId = request.getParameter("reservation_id"); //자원테이블 예약 ID 
		
		ReservationDAO reservationDAO = new ReservationDAO();
		String getId = reservationDAO.getReservationId(reservationId);
        
        List<Map<String, Object>> reservationList = reservationDAO.getMyReservationList(empId, getId);
        
        ActionForward forward = new ActionForward();
		forward.setPath("/WEB-INF/views/reservation/myReservationList.jsp");
		forward.setRedirect(false);
        request.setAttribute("reservationList", reservationList);
        
        return forward;
	}
}
