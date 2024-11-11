package com.hta2405.unite.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String requestURI = req.getRequestURI();

        //로그인, pw찾기 등 /emp 아래 URL은 예외처리함
        if (requestURI.startsWith(req.getContextPath() + "/css/") ||
            requestURI.startsWith(req.getContextPath() + "/js/") ||
            requestURI.startsWith(req.getContextPath() + "/image/") ||
            requestURI.equals(req.getContextPath() + "/emp") ||
            requestURI.startsWith(req.getContextPath() + "/emp/")
            ) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 로그인 확인
        String id = (String) req.getSession().getAttribute("id");
        boolean isLoggedIn = id != null;
        String loginURI = req.getContextPath() + "/emp/login";

        if (!isLoggedIn && !requestURI.equals(loginURI)) {
            resp.sendRedirect(loginURI);
            return;
        }

        chain.doFilter(req, resp);
    }
}
