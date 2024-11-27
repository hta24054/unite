package com.hta2405.unite.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String id = (String) req.getSession().getAttribute("id");

        // 역할이 "admin"이 아닌 경우 접근 차단
        if (!id.trim().equals("admin")) {
            resp.sendRedirect(req.getContextPath()+"/home/error");
            return;
        }

        // 관리자 역할인 경우 요청 계속 진행
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}