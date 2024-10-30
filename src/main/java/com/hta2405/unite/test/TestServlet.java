package com.hta2405.unite.test;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TestClass member1 = new TestClass("이순신", 50);
        TestClass member2 = new TestClass("홍길동", 20);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/index.jsp");
        req.setAttribute("member1", member1);
        req.setAttribute("member2", member2);
        dispatcher.forward(req, resp);
    }
}
