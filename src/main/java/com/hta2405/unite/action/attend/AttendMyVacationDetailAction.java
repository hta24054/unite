package com.hta2405.unite.action.attend;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.util.AttendUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

public class AttendMyVacationDetailAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginEmpId = (String) req.getSession().getAttribute("id");

        if (req.getParameter("year") == null) {
            return new ActionForward(false, "/attend/vacation/my?&year=" + LocalDate.now().getYear());
        }
        return new AttendUtil().getVacationDetail(req, loginEmpId);
    }
}
