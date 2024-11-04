package com.hta2405.unite.action;

import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class DeptAttendAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = "241208"; //로그인 구현 시 세션 내 "emp"로 대체
        EmpDao empDao = new EmpDao();
        DeptDao deptDao = new DeptDao();
        Emp emp = empDao.getEmpById(empId);
        System.out.println(emp);
        if (!isManager(emp, deptDao)) {
            //부서장이 아닐 시 권한없음 알림 및 에러페이지로 포워딩
            resp.getWriter().print("<script>alert('부서장이 아닙니다.')</script>");
            return new ActionForward(false, "/WEB-INF/views/error/forbidden.jsp");
        }

        List<Emp> empList = empDao.getEmpListByDeptId(emp.getDeptId());
        req.setAttribute("empList", empList);
        req.setAttribute("deptName", getDeptName(emp.getDeptId(), deptDao));
        return new ActionForward(false, "/WEB-INF/views/attend/attendDeptList.jsp");
    }

    private String getDeptName(Long deptId, DeptDao deptDao) {
        return deptDao.getDeptNameById(deptId);
    }

    private static boolean isManager(Emp emp, DeptDao deptDao) {
        System.out.println("deptId= " + emp.getDeptId());
        String managerId = deptDao.getManagerIdByDeptId(emp.getDeptId()); //소속 부서의 부서장을 확인함
        System.out.println("managerId = " + managerId);
        return emp.getEmpId().equals(managerId);
    }
}
