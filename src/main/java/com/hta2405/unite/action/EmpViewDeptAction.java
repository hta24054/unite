package com.hta2405.unite.action;

import java.io.IOException;
import java.util.List;
import com.hta2405.unite.dao.EmpInfoDao;
import com.hta2405.unite.dto.EmpInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpViewDeptAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String deptId = req.getParameter("deptId");
        
        // 임시 deptId 
        if (deptId == null || deptId.isEmpty()) {
            deptId = "1"; // 하드코딩된 부서 ID
        }

        EmpInfoDao dao = new EmpInfoDao();
        List<EmpInfo> empList = dao.getEmpInfoByDeptId(deptId);

        if (empList == null || empList.isEmpty()) {
            throw new ServletException("No employees found in department with ID: " + deptId);
        }

        req.setAttribute("empList", empList);
        req.setAttribute("deptName", empList.get(0).getDeptName());

        ActionForward forward = new ActionForward();
        forward.setRedirect(false);
        forward.setPath("/WEB-INF/views/empInfo/deptInfo.jsp");

        return forward;
    }
}
