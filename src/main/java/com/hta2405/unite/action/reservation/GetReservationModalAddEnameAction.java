package com.hta2405.unite.action.reservation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ReservationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetReservationModalAddEnameAction implements Action {
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String empId = request.getParameter("emp_id"); // 예약자
		String reservationId = request.getParameter("reservation_id"); //자원테이블 예약 ID

		ReservationDAO reservationDAO = new ReservationDAO();
		List<Map<String, Object>> reservationAddEnameList = reservationDAO.getReservationModalAddEname(empId, reservationId);

        Gson gson = new Gson();
        String json = gson.toJson(reservationAddEnameList);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);

        System.out.println("JSON Response: " + json);

        return null;
	}
}
