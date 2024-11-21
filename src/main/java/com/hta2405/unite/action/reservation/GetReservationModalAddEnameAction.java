package com.hta2405.unite.action.reservation;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ReservationDAO;
import com.hta2405.unite.dto.Resource;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetReservationModalAddEnameAction implements Action {
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String reservationId = request.getParameter("reservation_id"); //자원테이블 예약 ID
		String empId = request.getParameter("emp_id"); // 예약자
		
		ReservationDAO reservationDAO = new ReservationDAO();
		Resource resource = new Resource();

		Map<String, Object> modalData = reservationDAO.getReservationModalAddEname(reservationId, empId);
		request.setAttribute("resource", modalData.get("resource"));
		request.setAttribute("ename", modalData.get("ename"));
		
		Gson gson = new Gson();
        String json = gson.toJson(resource);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        
        System.out.println(json);

        return null;
	}
}
