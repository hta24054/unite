package com.hta2405.unite.action.attend;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.AttendUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

import static com.hta2405.unite.util.AttendUtil.checkAttendRole;

public class AttendMyDetailAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String empId = (String) req.getSession().getAttribute("id");
        Emp emp = new EmpDao().getEmpById(empId);
        checkAttendRole(emp, req);
        //처음 페이지 방문하면 당월 및 본인계정으로 GET 요청
        if (req.getParameter("year") == null || req.getParameter("month") == null) {
            return new ActionForward(false, "/attend/my?&year=" + LocalDate.now().getYear() + "&month=" + LocalDate.now().getMonthValue());
        }

        return new AttendUtil().getAttendDetail(req, emp);
    }
}
