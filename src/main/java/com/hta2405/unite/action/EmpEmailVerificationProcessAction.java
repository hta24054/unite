package com.hta2405.unite.action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpEmailVerificationProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		req.getSession().setAttribute("step2Complete", true);//세션 플래그(비밀번호 변경 페이지)
		
		return new ActionForward(true, "changePw");
	}

}
