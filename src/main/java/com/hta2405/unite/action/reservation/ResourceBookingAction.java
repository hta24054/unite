package com.hta2405.unite.action.reservation;

import java.io.IOException;
import java.time.LocalDateTime;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ReservationDAO;
import com.hta2405.unite.dto.Reservation;
import com.hta2405.unite.dto.Resource;
import com.hta2405.unite.util.CalendarDateTimeUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResourceBookingAction implements Action {
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		Reservation reservation = new Reservation();
		Resource resource = new Resource();
		
		String empId = request.getParameter("emp_id"); // 예약자
		int allDay = request.getParameter("allDay") == null ? 0 : Integer.parseInt(request.getParameter("allDay"));
		String startAt = request.getParameter("startAt");
		String endAt = request.getParameter("endAt");
		String usage = request.getParameter("usage");
		String resourceId = request.getParameter("resourceId"); // resc_id

		reservation.setEmpId(empId);
		reservation.setReservationAllDay(allDay);
		LocalDateTime startDateTime = CalendarDateTimeUtil.parseDateTimeWithoutT(startAt);
		LocalDateTime endDateTime = CalendarDateTimeUtil.parseDateTimeWithoutT(endAt);
		reservation.setReservationStart(startDateTime);
		reservation.setReservationEnd(endDateTime);
		reservation.setReservationInfo(usage);
		resource.setResourceId(Long.parseLong(resourceId)); // resc_id

		ReservationDAO reservationDao = new ReservationDAO();
		
		int ok = reservationDao.insertResourceBooking(reservation, resource);
		response.getWriter().print(ok);
		return null;
	}
}
