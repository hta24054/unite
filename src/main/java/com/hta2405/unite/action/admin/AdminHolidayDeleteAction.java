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

public class AdminHolidayDeleteAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate date = LocalDate.parse(req.getParameter("date"));
        if (new HolidayDao().getHolidayName(date) == null) {
            return alertAndGoBack(resp, "휴일 삭제 실패, 휴일이 아닙니다.");
        }
        if (new HolidayDao().deleteHoliday(date) != 1) {
            return alertAndGoBack(resp, "휴일 삭제 실패");
        }
        req.getSession().setAttribute("message", "휴일을 삭제하였습니다.");
        return new ActionForward(true, req.getContextPath() + "/admin/holiday");
    }
}
