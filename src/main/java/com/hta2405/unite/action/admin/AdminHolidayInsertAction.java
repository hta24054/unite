package com.hta2405.unite.action.admin;

import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.HolidayDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

public class AdminHolidayInsertAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate date = LocalDate.parse(req.getParameter("date"));
        String HolidayName = req.getParameter("holidayName");

        String message = "휴일을 등록하였습니다.";
        if (new HolidayDao().getHolidayName(date) != null) {
            message = "휴일 등록을 실패하였습니다. 이미 등록된 휴일입니다.";
        } else if (new HolidayDao().insertHoliday(date, HolidayName) != 1) {
            message = "휴일 등록을 실패하였습니다.";
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", message);
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(jsonObject);
        return null;
    }
}
