package com.hta2405.unite.action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpLogoutAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getSession().invalidate();
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(true);
		forward.setPath("../emp/login");
		return forward;
	}

}