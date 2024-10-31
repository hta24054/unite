package com.hta2405.unite.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

//마지막에 필터 적용


public class ForbiddenFilter implements Filter {
    //초기화
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("ForbiddenFilter");
    }

    //필터가 실행할 메서드
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        //업캐스팅
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String id = (String) req.getSession().getAttribute("id");

        if (!id.equals("admin")) {
            System.out.println("필터 : admin 권한이 없습니다.");
            req.getRequestDispatcher("/error/forbidden.jsp").forward(req, resp);
            return;
        }

        //다음 필터 전달
        //만약 마지막 필터라면 서블릿 컨테이너에 의해 요청된 서블릿으로 전달
        chain.doFilter(req, resp);

    }

    //
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
