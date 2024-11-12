package com.hta2405.unite.action.emp;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpEmailVerificationAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		if(req.getSession().getAttribute("email")==null) {
			req.getSession().setAttribute("message","세션이 만료되었습니다.");
            return new ActionForward(true, "login");
		}
		return new ActionForward(false, "/WEB-INF/views/emp/emailVerificationForm.jsp");
	}

}
