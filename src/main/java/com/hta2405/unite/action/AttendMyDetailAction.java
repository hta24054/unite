package com.hta2405.unite.action;

import com.hta2405.unite.dao.AttendDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.HolidayDao;
import com.hta2405.unite.dto.Attend;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.Holiday;
import com.hta2405.unite.util.AttendUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendMyDetailAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String empId = (String) req.getSession().getAttribute("id");
        Emp emp = new EmpDao().getEmpById(empId);

        //처음 페이지 방문하면 당월 및 본인계정으로 GET 요청
        if (req.getParameter("year") == null || req.getParameter("month") == null) {
            return new ActionForward(false, "/attend/my?&year=" + LocalDate.now().getYear() + "&month=" + LocalDate.now().getMonthValue());
        }

        return new AttendUtil().getAttendDetail(req, emp);
    }
}
