package com.hta2405.unite.controller;

import com.hta2405.unite.action.reservation.GetResourceBookingDetailAction;
import com.hta2405.unite.action.reservation.GetResourceBookingListAction;
import com.hta2405.unite.action.reservation.GetResourceIdAction;
import com.hta2405.unite.action.reservation.GetResourceListAction;
import com.hta2405.unite.action.reservation.ReservationWeekCalenderAction;
import com.hta2405.unite.action.reservation.ResourceBookingAction;
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
		actionMap.put("/resourceBooking", ResourceBookingAction::new));
		actionMap.put("/getResourceBookingList", GetResourceBookingListAction::new);
		actionMap.put("/getResourceBookingDetail", GetResourceBookingDetailAction::new);	
	}
}
