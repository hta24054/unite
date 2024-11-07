package com.hta2405.unite.action;

import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.AttendUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

public class AttendVacationDetailAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");
        Emp emp = new EmpDao().getEmpById(empId);
        if (req.getParameter("year") == null) {
            return new ActionForward(false, "/attend/vacation/my?&year=" + LocalDate.now().getYear());
        }
        return new AttendUtil().getVacationDetail(req, emp);
    }
}
