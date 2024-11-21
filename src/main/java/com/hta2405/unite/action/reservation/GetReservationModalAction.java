package com.hta2405.unite.action.reservation;

import java.io.IOException;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ReservationDAO;
import com.hta2405.unite.dto.Resource;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetReservationModalAction implements Action {
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String empId = request.getParameter("emp_id"); // 예약자
		String reservationId = request.getParameter("reservation_id"); //자원테이블 예약 ID

		ReservationDAO reservationDAO = new ReservationDAO();
		Resource resource = new Resource();

		resource = reservationDAO.getReservationModal(empId, reservationId);

		Gson gson = new Gson();
        String json = gson.toJson(resource);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);

        return null;
	}
}
