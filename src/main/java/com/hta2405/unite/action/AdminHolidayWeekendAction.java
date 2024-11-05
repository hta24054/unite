package com.hta2405.unite.action;

import com.hta2405.unite.dao.HolidayDao;
import com.hta2405.unite.util.CommonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class AdminHolidayWeekendAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);
        HolidayDao holidayDao = new HolidayDao();

        addWeekendHolidays(startDate, endDate, holidayDao);
        return CommonUtil.alertAndGoBack(resp, "향후 1년간의 주말이 업데이트 되었습니다.");
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
