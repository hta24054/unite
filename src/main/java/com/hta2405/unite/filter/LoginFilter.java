package com.hta2405.unite.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*
    필터는 요청 응답 전 후 처리 가능
    응답 보낸 후에도 작업 가능
    체인방식 처리
    web.xml에 적용 필터 순서대로 작성
*/


//개발 마지막에 필터 적용
public class LoginFilter implements Filter {
    //필터가 실행할 메서드
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        //업캐스팅
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String id = (String) req.getSession().getAttribute("id");

        if (id == null) {
            System.out.println("필터 : id = null입니다.");
            resp.sendRedirect(req.getContextPath() + "/members/login");
            return;
        }

        //다음 필터 전달
        //만약 마지막 필터라면 서블릿 컨테이너에 의해 요청된 서블릿으로 전달
        chain.doFilter(req, resp);

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
