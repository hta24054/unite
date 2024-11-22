package com.hta2405.unite.action.reservation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.ReservationDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetReservationModalAction implements Action {
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String reservationId = request.getParameter("reservation_id"); //자원테이블 예약 ID

		ReservationDAO reservationDAO = new ReservationDAO();
		List<Map<String, Object>> reservationAddEnameList = reservationDAO.getReservationModal(reservationId);

        Gson gson = new Gson();
        JsonElement mapToJsonList = gson.toJsonTree(reservationAddEnameList);
        
        HashMap<String, String> empNameMap = new EmpDao().getIdToENameMap();
        JsonElement mapToJson = gson.toJsonTree(empNameMap);
        
        JsonObject arr = new JsonObject();
        arr.add("empName", mapToJson);
        arr.add("empNameList", mapToJsonList);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(arr);

        return null;
	}
}
