package com.hta2405.unite.action.admin;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminEmpRegisterAction implements Action {
    private final DeptDao deptDao = new DeptDao();
    private final JobDao jobDao = new JobDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("deptList", deptDao.getAllDept());
        req.setAttribute("jobList", jobDao.getAllJob());
        return new ActionForward(false, "/WEB-INF/views/admin/emp_register.jsp");
    }
}
