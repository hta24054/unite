package com.hta2405.unite.action.reservation;

import java.io.IOException;
import java.time.LocalDateTime;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ReservationDAO;
import com.hta2405.unite.dto.Reservation;
import com.hta2405.unite.util.CalendarDateTimeUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResourceReservationAction implements Action {
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		Reservation reservation = new Reservation();
		
		String empId = request.getParameter("emp_id"); // 예약자
		int allDay = request.getParameter("allDay") == null ? 0 : Integer.parseInt(request.getParameter("allDay"));
		String startAt = request.getParameter("startAt");
		String endAt = request.getParameter("endAt");
		String reservationInfo = request.getParameter("reservationInfo");
		String resourceId = request.getParameter("resourceId"); // resc_id

		reservation.setEmpId(empId);
		reservation.setReservationAllDay(allDay);
		LocalDateTime startDateTime = CalendarDateTimeUtil.parseDateTimeWithoutT(startAt);
		LocalDateTime endDateTime = CalendarDateTimeUtil.parseDateTimeWithoutT(endAt);
		reservation.setReservationStart(startDateTime);
		reservation.setReservationEnd(endDateTime);
		reservation.setReservationInfo(reservationInfo);
		reservation.setResourceId(Integer.parseInt(resourceId));
		
		ReservationDAO reservationDao = new ReservationDAO();

		int ok = reservationDao.insertReservation(reservation);
		response.getWriter().print(ok);
		return null;
	}
}
