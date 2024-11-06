package com.hta2405.unite.action;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EmpChangePwAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		/* 브라우저에서 건너온 세션 확인 
		 * 로그인 상태에선 접근 못하게 설정 */
		HttpSession session = req.getSession(true);
		if((String)session.getAttribute("id") != null) {
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.println("<script>");
			out.println("alert('접근 권한이 없습니다.');");
			out.println("location.href='../emp/home';");
			out.println("</script>");
		}
		
		
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("/WEB-INF/views/emp/changePw.jsp");
		return forward;
	}

}
