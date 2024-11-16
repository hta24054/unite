package com.hta2405.unite.action.doc;

import com.google.gson.JsonObject;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.HolidayDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DocCountVacationAction implements com.hta2405.unite.action.Action {
    private final HolidayDao holidayDao = new HolidayDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate startDate = LocalDate.parse(req.getParameter("startDate"));
        LocalDate endDate = LocalDate.parse(req.getParameter("endDate"));

        long allCount = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int holidayCount = holidayDao.getHoliday(startDate, endDate).size();
        int vacationCount = (int) (allCount - holidayCount);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dayCount", vacationCount);
        resp.getWriter().print(jsonObject);
        return null;
    }
}
