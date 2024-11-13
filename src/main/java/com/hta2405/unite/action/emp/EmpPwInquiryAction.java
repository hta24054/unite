package com.hta2405.unite.action.emp;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpPwInquiryAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		return new ActionForward(false,"/WEB-INF/views/emp/pwInquiryForm.jsp");
	}

}
