package com.hta2405.unite.controller;

import com.hta2405.unite.action.reservation.GetReservationInfoAction;
import com.hta2405.unite.action.reservation.GetReservationListAction;
import com.hta2405.unite.action.reservation.GetResourceIdAction;
import com.hta2405.unite.action.reservation.GetResourceListAction;
import com.hta2405.unite.action.reservation.ReservationWeekCalenderAction;
import com.hta2405.unite.action.reservation.ResourceReservationAction;
import com.hta2405.unite.action.reservation.ResourceSelectChangeAction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/reservation/*")
public class ReservationFrontController extends AbstractFrontController {
	
	@Override
    public void init() throws ServletException {
    actionMap.put("/weekCalendar", ReservationWeekCalenderAction::new);
		actionMap.put("/getResourceList", GetResourceListAction::new);
		actionMap.put("/resourceSelectChange", ResourceSelectChangeAction::new);
		actionMap.put("/getResourceId", GetResourceIdAction::new);
		
		actionMap.put("/resourceReservation", ResourceReservationAction::new);
		actionMap.put("/getReservationList", GetReservationListAction::new);
		actionMap.put("/getReservationInfo", GetReservationInfoAction::new);	
	}
}
