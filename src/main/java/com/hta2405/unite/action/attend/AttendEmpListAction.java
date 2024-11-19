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
import java.util.List;

import static com.hta2405.unite.util.AttendUtil.checkAttendRole;
import static com.hta2405.unite.util.EmpUtil.isManager;

public class AttendEmpListAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");
        EmpDao empDao = new EmpDao();
        Emp emp = empDao.getEmpById(empId);
        DeptDao deptDao = new DeptDao();
        if (!isManager(emp)) {
            return CommonUtil.alertAndGoBack(resp, "부서장이 아닙니다.");
        }
        deptDao.getIdToDeptNameMap();
        List<Emp> empList = empDao.getSubEmpListByEmp(emp);


        checkAttendRole(emp, req);
        req.setAttribute("role", empList);
        req.setAttribute("empList", empList);
        req.setAttribute("jobMap", new JobDao().getIdToJobNameMap());
        req.setAttribute("deptMap", deptDao.getIdToDeptNameMap());
        req.setAttribute("deptName", deptDao.getDeptNameById(emp.getDeptId()));
        return new ActionForward(false, "/WEB-INF/views/attend/attendEmpList.jsp");
    }
}
