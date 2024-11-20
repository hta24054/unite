package com.hta2405.unite.action.reservation;

import java.io.IOException;
import java.io.PrintWriter;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ReservationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CancelReservationAction implements Action {
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String reservationId = request.getParameter("reservation_id"); //자원테이블 예약 ID
		String empId = request.getParameter("emp_id"); // 예약자
		
		ReservationDAO reservationDAO = new ReservationDAO();
		int result = reservationDAO.cancelReservation(reservationId, empId);

        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.close();

        return null;
	}
}
