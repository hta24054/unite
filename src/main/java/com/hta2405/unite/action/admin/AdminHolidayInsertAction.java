package com.hta2405.unite.action.admin;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.HolidayDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

import static com.hta2405.unite.util.CommonUtil.alertAndGoBack;

public class AdminHolidayInsertAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate date = LocalDate.parse(req.getParameter("date"));
        String HolidayName = req.getParameter("holidayName");
        if (new HolidayDao().getHolidayName(date) != null) {
            return alertAndGoBack(resp, "휴일 등록 실패, 이미 등록된 휴일입니다.");
        }
        if (new HolidayDao().insertHoliday(date, HolidayName) != 1) {
            return alertAndGoBack(resp, "휴일 등록 실패");
        }
        return new ActionForward(true, req.getContextPath() + "/admin/holiday");
    }
}
