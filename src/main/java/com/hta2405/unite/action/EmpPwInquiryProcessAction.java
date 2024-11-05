package com.hta2405.unite.action;

import java.io.IOException;
import java.io.PrintWriter;

import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EmpPwInquiryProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String id = req.getParameter("id");
		
		EmpDao dao = new EmpDao();
		Emp emp = dao.getEmpById(id);
		
		if(emp != null) {
			String dbEmail = emp.getEmail();
			HttpSession session = req.getSession();
			session.setAttribute("email", dbEmail);
			session.setMaxInactiveInterval(5*60);//세션 유효시간 5분 설정
			
			ActionForward forward = new ActionForward();
			forward.setRedirect(true);
			forward.setPath("EmailVerification");
			return forward;
		}
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print("<script>");
		out.print("alert('잘못된 아이디입니다. 다시 입력해주세요');");
		out.print("history.back();");
		out.print("</script>");
		out.close();
		return null;
		
	}

}
