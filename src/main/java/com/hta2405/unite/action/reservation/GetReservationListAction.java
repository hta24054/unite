package com.hta2405.unite.action.reservation;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ReservationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetReservationListAction implements Action {
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String resourceId = req.getParameter("resourceId"); // resc_id
		
		ReservationDAO reservationDao = new ReservationDAO();
		JsonArray reservationArray = reservationDao.getReservationList(resourceId);
		
		Gson gson = new Gson();
        String json = gson.toJson(reservationArray);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);

        return null;
	}
}
