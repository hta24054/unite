package com.hta2405.unite.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/first")
public class FirstFrontController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = (String) req.getSession().getAttribute("id");
        boolean isLoggedIn = id != null;
        //로그인 여부에 따라 메인 페이지 또는 로그인 페이지로 리다이렉트
        if (isLoggedIn) {
            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
            resp.sendRedirect(req.getContextPath() + "/emp/login");
        }
    }
}