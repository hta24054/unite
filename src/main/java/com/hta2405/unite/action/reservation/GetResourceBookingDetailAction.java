package com.hta2405.unite.action.reservation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ReservationDAO;
import com.hta2405.unite.dto.Reservation;
import com.hta2405.unite.dto.Resource;
import com.hta2405.unite.util.CalendarDateTimeUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetResourceBookingDetailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String resourceId = request.getParameter("resourceId");
		
		ReservationDAO reservationDAO = new ReservationDAO();
		Map<String, Object> resourceData = reservationDAO.getResourceBookingDetail(resourceId);
	    
		Gson gson = new Gson();
        String json = gson.toJson(resourceData);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        
        return null;
	}
}
