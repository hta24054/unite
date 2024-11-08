package com.hta2405.unite.controller;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.action.HomeAction;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;

public abstract class AbstractFrontController extends HttpServlet {

    protected HashMap<String, Action> actionMap = new HashMap<>();

    protected void doProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String command = req.getPathInfo();
        System.out.println(command);

        // command가 null이고, 요청이 /home인 경우 홈 페이지로 이동
        if (command == null && req.getServletPath().equals("/home")) {
            ActionForward forward = new HomeAction().execute(req, resp);
            if (forward.isRedirect()) {
                resp.sendRedirect(forward.getPath());
            } else {
                RequestDispatcher dispatcher = req.getRequestDispatcher(forward.getPath());
                dispatcher.forward(req, resp);
            }
            return;
        }

        // 유효하지 않은 URL
        if (command == null || !actionMap.containsKey(command)) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error/404.jsp");
            dispatcher.forward(req, resp);
            return;
        }

        // command에 해당하는 Action 처리
        Action action = actionMap.get(command);
        ActionForward forward = action.execute(req, resp);

        if (forward != null) {
            if (forward.isRedirect()) {
                resp.sendRedirect(forward.getPath());
            } else {
                RequestDispatcher dispatcher = req.getRequestDispatcher(forward.getPath());
                dispatcher.forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doProcess(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doProcess(req, resp);
    }
}
