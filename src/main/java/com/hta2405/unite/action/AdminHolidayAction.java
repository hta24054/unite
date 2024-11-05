package com.hta2405.unite.action;

import com.hta2405.unite.dao.HolidayDao;
import com.hta2405.unite.dto.Holiday;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AdminHolidayAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //올해 1월 1일부터, 현재부터 1년 후 시점까지의 휴일을 가져옴
        int year = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.now().plusYears(1);
        List<Holiday> yearlyHoliday = new HolidayDao().getHoliday(startDate, endDate);
        req.setAttribute("holidayList", yearlyHoliday);
        return new ActionForward(false, "/WEB-INF/views/admin/holiday.jsp");
    }
}
