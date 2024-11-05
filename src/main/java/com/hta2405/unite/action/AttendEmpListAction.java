package com.hta2405.unite.action;

import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.hta2405.unite.util.EmpUtil.isManager;

public class AttendEmpListAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");
        EmpDao empDao = new EmpDao();
        Emp emp = empDao.getEmpById(empId);
        DeptDao deptDao = new DeptDao();
        if (!isManager(emp)) {
            //부서장이 아닐 시 권한없음 알림 및 에러페이지로 포워딩
            PrintWriter out = resp.getWriter();
            resp.setContentType("text/html;charset=utf-8");
            out.print("<script>alert('부서장이 아닙니다.');");
            out.print("history.back()</script>");
            return null;
        }
        List<Emp> empList = empDao.getSubEmpListByEmp(emp);
        req.setAttribute("empList", empList);
        req.setAttribute("deptName", getDeptName(emp.getDeptId(), deptDao));
        return new ActionForward(false, "/WEB-INF/views/attend/attendEmpList.jsp");
    }

    private String getDeptName(Long deptId, DeptDao deptDao) {
        return deptDao.getDeptNameById(deptId);
    }
}
