package com.hta2405.unite.action;

import java.io.IOException;
import java.sql.SQLException;
import com.hta2405.unite.dao.EmpInfoDao;
import com.hta2405.unite.dto.EmpInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpInfoUpdateAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ActionForward forward = new ActionForward();
		EmpInfoDao dao = new EmpInfoDao();
		EmpInfo empinfo = new EmpInfo();

		empinfo.setEmpId(req.getParameter("id"));
		empinfo.setEmail(req.getParameter("email"));
		empinfo.setTel(req.getParameter("tel"));
		empinfo.setMobile(req.getParameter("mobile"));
		empinfo.setMobile2(req.getParameter("mobile2"));
		empinfo.setAddress(req.getParameter("address"));
		empinfo.setMarried("Y".equals(req.getParameter("married")));

		try {
			dao.updateEmpInfo(empinfo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		forward.setPath("/empInfo/view?id=" + empinfo.getEmpId());
		forward.setRedirect(false);
		return forward;
	}
}
