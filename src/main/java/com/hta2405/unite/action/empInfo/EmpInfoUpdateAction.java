package com.hta2405.unite.action.empInfo;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 직원 정보 업데이트를 위한 액션 클래스입니다.
 * 요청된 정보에 따라 직원 정보를 업데이트합니다.
 */
public class EmpInfoUpdateAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ActionForward forward = new ActionForward();

        // 세션에서 사용자 ID를 가져옵니다.
        String id = (String) req.getSession().getAttribute("id");

        // DAO 객체를 생성합니다.
        EmpDao empDao = new EmpDao();
        Emp emp = empDao.getEmpById(id);

        // 요청 파라미터에서 직원 정보를 설정합니다.
        emp.setEmpId(req.getParameter("id"));
        emp.setEmail(req.getParameter("email"));
        emp.setTel(req.getParameter("tel"));
        emp.setMobile(req.getParameter("mobile"));
        emp.setMobile2(req.getParameter("mobile2"));
        emp.setAddress(req.getParameter("address"));
        emp.setMarried("Y".equals(req.getParameter("married")));

        // 직원 정보를 업데이트합니다.
        empDao.updateMyEmp(emp);

        // 업데이트 후 리다이렉트할 경로를 설정합니다.
        forward.setPath(req.getContextPath() + "/empInfo/view?id=" + emp.getEmpId());
        forward.setRedirect(true);
        return forward;
    }
}
