package com.hta2405.unite.test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TestClass member = new TestClass("홍길동", 20);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/index.jsp");
        req.setAttribute("member", member);
        dispatcher.forward(req, resp);
    }
}
