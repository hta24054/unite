package com.hta2405.unite.action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmpLoginAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String id ="";
		Cookie[] cookies = req.getCookies();
		if(cookies != null) {
			for(int i=0;i<cookies.length;i++) {
				if(cookies[i].getName().equals("id")) {
					id=cookies[i].getValue();
				}
			}
		}
		req.setAttribute("cookieId", id);
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(false); //주소 변경없이 jsp페이지의 내용을 보여줍니다.
		forward.setPath("/WEB-INF/views/emp/loginForm.jsp");
		return forward;
    }
}