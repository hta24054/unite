package com.hta2405.unite.action.reservation;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ReservationDAO;
import com.hta2405.unite.dto.Resource;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResourceSelectChangeAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String resourceType = req.getParameter("resourceType"); 
		
        ReservationDAO reservationDao = new ReservationDAO();
        List<Resource> resourceList = reservationDao.resourceSelectChange(resourceType);

        Gson gson = new Gson();
        String json = gson.toJson(resourceList);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);

        return null;
	}
}
