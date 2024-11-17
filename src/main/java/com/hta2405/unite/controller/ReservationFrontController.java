package com.hta2405.unite.controller;


import com.hta2405.unite.action.reservation.GetResourceListAction;
import com.hta2405.unite.action.reservation.GetResourceTypeAction;
import com.hta2405.unite.action.reservation.ReservationWeekCalenderAction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/reservation/*")
public class ReservationFrontController extends AbstractFrontController {
	
	@Override
    public void init() throws ServletException {
		actionMap.put("/weekCalendar", new ReservationWeekCalenderAction());
		actionMap.put("/getResourceType", new GetResourceTypeAction());
		actionMap.put("/getResourceList", new GetResourceListAction());
		
	}

}


