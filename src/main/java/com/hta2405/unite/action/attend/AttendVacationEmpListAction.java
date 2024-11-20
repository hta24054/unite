package com.hta2405.unite.action.attend;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.JobDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.CommonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.hta2405.unite.util.AttendUtil.checkAttendRole;
import static com.hta2405.unite.util.EmpUtil.isHrDept;

public class AttendVacationEmpListAction implements Action {
    private final EmpDao empDao = new EmpDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");
        Emp emp = empDao.getEmpById(empId);

        if (!isHrDept(emp)) {
            return CommonUtil.alertAndGoBack(resp, "인사부서가 아닙니다.");
        }
        checkAttendRole(emp, req);
        return new ActionForward(false, "/WEB-INF/views/attend/attendVacationEmpList.jsp");
    }
}
