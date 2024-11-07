package com.hta2405.unite.action.attend;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.AttendUtil;
import com.hta2405.unite.util.CommonUtil;
import com.hta2405.unite.util.EmpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AttendVacationEmpDetailAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EmpDao empDao = new EmpDao();

        Emp loginEmp = empDao.getEmpById((String) req.getSession().getAttribute("id"));
        Emp targetEmp = empDao.getEmpById(req.getParameter("emp"));

        //본인이 인사부서인지 확인
        if (!EmpUtil.isHrDept(loginEmp)) {
            CommonUtil.alertAndGoBack(resp, "인사부서가 아닙니다.");
        }

        return new AttendUtil().getAttendDetail(req, targetEmp);
    }
}
