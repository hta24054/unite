package com.hta2405.unite.action.empInfo;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpInfoUpdateAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionForward forward = new ActionForward();

		String id = (String) req.getSession().getAttribute("id");

		EmpDao empDao = new EmpDao();
		Emp emp = empDao.getEmpById(id);

		emp.setEmpId(req.getParameter("id"));
		emp.setEmail(req.getParameter("email"));
		emp.setTel(req.getParameter("tel"));
		emp.setMobile(req.getParameter("mobile"));
		emp.setMobile2(req.getParameter("mobile2"));
		emp.setAddress(req.getParameter("address"));
		emp.setMarried("Y".equals(req.getParameter("married")));

		empDao.updateMyEmp(emp);

		forward.setPath(req.getContextPath() + "/empInfo/view?id=" + emp.getEmpId());
		forward.setRedirect(true);
		return forward;
	}
}
