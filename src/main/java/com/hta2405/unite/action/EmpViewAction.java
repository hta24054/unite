package com.hta2405.unite.action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.hta2405.unite.dao.*;
import com.hta2405.unite.dto.*;

public class EmpViewAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		EmpInfoDao dao = new EmpInfoDao();
		EmpInfo empinfo = dao.getEmpInfoById(req.getParameter("id"));
		req.setAttribute("empinfo", empinfo);
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("/WEB-INF/views/empInfo/empInfo.jsp");
		
		return forward;
	}
}
