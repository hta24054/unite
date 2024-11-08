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

public class AdminHolidayDeleteAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate date = LocalDate.parse(req.getParameter("date"));
        HolidayDao holidayDao = new HolidayDao();
        String message = "휴일을 삭제하였습니다.";

        if (holidayDao.getHolidayName(date) == null) {
            message = "휴일 삭제 실패, 휴일이 아닙니다.";
        }else if (holidayDao.deleteHoliday(date) != 1) {
            message = "휴일 삭제 실패";
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", message);

        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(jsonObject);
        return null;
    }
}
