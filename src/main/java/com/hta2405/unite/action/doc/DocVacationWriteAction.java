package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.AttendDao;
import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

public class DocVacationWriteAction implements com.hta2405.unite.action.Action {
    private final AttendDao attendDao = new AttendDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");
        Emp emp = new EmpDao().getEmpById(empId);
        req.setAttribute("emp", emp);
        req.setAttribute("dept", new DeptDao().getDeptByEmpId(empId));
        req.setAttribute("today", LocalDate.now());
        int usedCount = attendDao.getUsedAnnualVacationCount(empId, LocalDate.now().getYear());
        req.setAttribute("usedCount", usedCount);
        return new ActionForward(false, "/WEB-INF/views/doc/vacation_write.jsp");
    }
}
