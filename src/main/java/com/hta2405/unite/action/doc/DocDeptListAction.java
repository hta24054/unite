package com.hta2405.unite.action.doc;

import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dao.DocDao;
import com.hta2405.unite.dao.EmpDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DocDeptListAction implements com.hta2405.unite.action.Action {
    private final DocDao docDao = new DocDao();
    private final DeptDao deptDao = new DeptDao();
    private final EmpDao empDao = new EmpDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");

        req.setAttribute("list", docDao.getFinishedDeptDocListByEmpId(empId));
        req.setAttribute("deptMap", deptDao.getIdToDeptNameMap());
        req.setAttribute("deptId", empDao.getEmpById(empId).getDeptId());

        return new ActionForward(false, "/WEB-INF/views/doc/list_dept.jsp");
    }
}
