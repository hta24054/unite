package com.hta2405.unite.action.reservation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

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

		Reservation reservation = new Reservation();
		Resource resource = new Resource();
		
		String empId = request.getParameter("emp_id"); // 예약자
		int allDay = request.getParameter("allDay") == null ? 0 : Integer.parseInt(request.getParameter("allDay"));
		String startAt = request.getParameter("startAt");
		String endAt = request.getParameter("endAt");
		String reservationInfo = request.getParameter("reservationInfo");		
		String resourceId = request.getParameter("resourceId"); // resc_id
		
		/*
		reservation.setEmpId(empId);
		reservation.setReservationAllDay(allDay);
		LocalDateTime startDateTime = CalendarDateTimeUtil.parseDateTimeWithoutT(startAt);
		LocalDateTime endDateTime = CalendarDateTimeUtil.parseDateTimeWithoutT(endAt);
		reservation.setReservationStart(startDateTime);
		reservation.setReservationEnd(endDateTime);
		reservation.setReservationInfo(reservationInfo);
		reservation.setResourceId(Integer.parseInt(resourceId));
		System.out.println("Received resourceId: " + resourceId);
		*/
		
		ReservationDAO reservationDao = new ReservationDAO();

		// 리소스 예약을 데이터베이스에 삽입
        //int insertResult = reservationDao.insertResourceBooking(reservation);
        //response.getWriter().print("Insert Result: " + insertResult);  // 삽입 결과 반환
        
        // 리소스 예약 정보 가져오기
        HashMap<String, String> resourceMap = reservationDao.getResourceBookingDetail(reservation, resource);
        Set<String> keys = resourceMap.keySet();
        for (String key : keys) {
            response.getWriter().println(key + " = " + resourceMap.get(key));
        }
        
        return null;
	}
}
