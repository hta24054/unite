package com.hta2405.unite.action;

import com.hta2405.unite.dao.HolidayDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class AdminHolidayInsertAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate date = LocalDate.parse(req.getParameter("date"));
        String HolidayName = req.getParameter("holidayName");
        if (new HolidayDao().getHolidayName(date) != null) {
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter out = resp.getWriter();
            out.print("<script>");
            out.print("alert('휴일 등록 실패, 이미 등록된 휴일입니다.');");
            out.print("location.href = history.back();");
            out.print("</script>");
            out.close();
            return null;
        }

        int result = new HolidayDao().insertHoliday(date, HolidayName);
        if (result != 1) {
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter out = resp.getWriter();
            out.print("<script>");
            out.print("alert('휴일 등록 실패');");
            out.print("location.href = history.back();");
            out.print("</script>");
            out.close();
            return null;
        }
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.print("<script>");
        out.print("alert('휴일 등록 성공');");
        out.print("location.href = history.back();");
        out.print("</script>");
        out.close();
        return new ActionForward(true, req.getContextPath() + "/admin/holiday");
    }
}
