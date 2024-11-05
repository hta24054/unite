package com.hta2405.unite.action;

import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.AttendUtil;
import com.hta2405.unite.util.EmpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class AttendEmpDetailAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EmpDao empDao = new EmpDao();

        Emp loginEmp = empDao.getEmpById((String) req.getSession().getAttribute("id"));
        Emp targetEmp = empDao.getEmpById(req.getParameter("emp"));

        //본인이 부서장인지 확인
        if (!EmpUtil.isManager(loginEmp)) {
            PrintWriter out = resp.getWriter();
            resp.setContentType("text/html;charset=utf-8");
            out.print("<script>alert('부서장이 아닙니다.');");
            out.print("history.back()</script>");
            return null;
        }

        //해당 타겟 직원의 부서장인지 확인
        if (!EmpUtil.isValidToAccessEmp(loginEmp, targetEmp)) {
            PrintWriter out = resp.getWriter();
            resp.setContentType("text/html;charset=utf-8");
            out.print("<script>alert('해당 직원을 확인할 수 없습니다.');");
            out.print("history.back()</script>");
            return null;
        }
        return new AttendUtil().getAttendDetail(req, targetEmp);
    }
}
