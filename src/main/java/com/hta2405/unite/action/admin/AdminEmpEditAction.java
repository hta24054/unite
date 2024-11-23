package com.hta2405.unite.action.admin;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.*;
import com.hta2405.unite.util.ConfigUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminEmpEditAction implements Action {
    private final EmpDao empDao = new EmpDao();
    private final LangDao langDao = new LangDao();
    private final CertDao certDao = new CertDao();
    private final DeptDao deptDao = new DeptDao();
    private final JobDao jobDao = new JobDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = req.getParameter("empId");
        req.setAttribute("emp", empDao.getEmpById(empId));
        req.setAttribute("langList", langDao.getLangByEmpId(empId));
        req.setAttribute("certList", certDao.getCertByEmpId(empId));
        req.setAttribute("deptList", deptDao.getAllDept());
        req.setAttribute("jobList", jobDao.getAllJob());
        return new ActionForward(false, "/WEB-INF/views/admin/emp_edit.jsp");
    }
}
