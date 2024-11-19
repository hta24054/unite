package com.hta2405.unite.action.reservation;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ReservationDAO;
import com.hta2405.unite.dto.Reservation;
import com.hta2405.unite.dto.Resource;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetResourceBookingListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ReservationDAO reservationDao = new ReservationDAO();
		JsonArray bookingArray = reservationDao.getResourceBookingList();
		
		Gson gson = new Gson();
        String json = gson.toJson(bookingArray);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);

        return null;
	}
}
