package com.hta2405.unite.action.empInfo;

import java.io.IOException;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpDetailViewAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 로그 추가
        System.out.println("EmpDetailViewAction - execute() 호출됨");

        String empId = req.getParameter("empId");
        System.out.println("EmpDetailViewAction - 받은 empId: " + empId);

        EmpDao empDao = new EmpDao();
        Emp emp = empDao.getEmpById(empId);
        System.out.println("EmpDetailViewAction - 조회된 emp: " + emp);

        if (emp == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.setAttribute("error", "Employee not found");
            ActionForward forward = new ActionForward();
            forward.setPath("/error.jsp");
            forward.setRedirect(false);
            System.out.println("EmpDetailViewAction - 직원 정보 없음");
            return forward;
        }

        req.setAttribute("details", emp);
        ActionForward forward = new ActionForward();
        forward.setPath("/WEB-INF/views/empInfo/empInfo.jsp");
        forward.setRedirect(false);
        System.out.println("EmpDetailViewAction - 요청 포워딩: /WEB-INF/views/empInfo/empInfo.jsp");
        return forward;
    }
}
