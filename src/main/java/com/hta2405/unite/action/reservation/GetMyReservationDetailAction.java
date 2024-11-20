package com.hta2405.unite.action.reservation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ReservationDAO;
import com.hta2405.unite.dto.Resource;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetMyReservationDetailAction implements Action {
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String reservationId = request.getParameter("reservation_id"); //자원테이블 예약 ID
		String empId = request.getParameter("emp_id"); // 예약자 ID
		
		ReservationDAO reservationDAO = new ReservationDAO();
		Resource resource = new Resource();

		resource = reservationDAO.getMyReservationDetail(reservationId, empId);
		
		System.out.println("Received reservation_id: " + reservationId);
		System.out.println("Received emp_id: " + empId);
        
		Gson gson = new Gson();
        String json = gson.toJson(resource);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);

        return null;
	}
}
