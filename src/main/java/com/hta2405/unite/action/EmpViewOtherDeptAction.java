package com.hta2405.unite.action;

import java.io.IOException;

import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.EmpInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpViewOtherDeptAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("/WEB-INF/views/empInfo/otherDeptInfo.jsp");
		
		return forward;
	}

}
