package com.hta2405.unite.controller;

import com.hta2405.unite.action.reservation.GetResourceListAction;
import com.hta2405.unite.action.reservation.ReservationWeekCalenderAction;
import com.hta2405.unite.action.reservation.ResourceSelectChangeAction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/reservation/*")
public class ReservationFrontController extends AbstractFrontController {
	
	@Override
    public void init() throws ServletException {
		actionMap.put("/weekCalendar", new ReservationWeekCalenderAction());
		actionMap.put("/getResourceList", new GetResourceListAction());
		actionMap.put("/resourceSelectChange", new ResourceSelectChangeAction());
		
	}

}

