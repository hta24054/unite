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
			forward.setPath("emailVerification");
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
	
	public static String maskEmail(String email) {
        // 이메일이 null이거나 비어있을 경우 그대로 반환
        if (email == null || email.isEmpty()) {
            return email;
        }

        // 이메일을 '@' 기준으로 나눕니다.
        String[] parts = email.split("@");

        String username = parts[0];
        String domain = parts[1];

        int length = username.length();
        if (length <= 3) {
            // 사용자명이 3자리이하면 모두 감추기
            username = "*".repeat(length);
        } else {
            // 사용자명이 3자리이상이면 첫 두글자 제외하고 모두 감추기
            username = username.substring(0, 2)
                    + "*".repeat(length - 2);
        }

        // 마스킹된 사용자명과 도메인을 합쳐서 반환
        return username + "@" + domain;
    }

}
