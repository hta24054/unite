package com.hta2405.unite.action;

import com.hta2405.unite.dao.HolidayDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class AdminHolidayWeekendAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);
        HolidayDao holidayDao = new HolidayDao();

        addWeekendHolidays(startDate, endDate, holidayDao);

        System.out.println("주말 추가 성공");
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.print("<script>");
        out.print("alert('향후 1년간의 주말이 업데이트 되었습니다.');");
        out.print("location.href = history.back();");
        out.print("</script>");
        out.close();
        return null;
    }

    private void addWeekendHolidays(LocalDate startDate, LocalDate endDate, HolidayDao holidayDao) {
        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            if (isWeekend(dayOfWeek) && holidayDao.getHolidayName(date) == null) {
                String holidayName = (dayOfWeek == DayOfWeek.SATURDAY) ? "토요일" : "일요일";
                holidayDao.insertHoliday(date, holidayName);
            }
            date = date.plusDays(1); // 날짜를 하루씩 증가
        }
    }

    private boolean isWeekend(DayOfWeek dayOfWeek) {
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
