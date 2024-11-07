package com.hta2405.unite.controller;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.action.mypage.MyPagePasswordChangeAction;
import com.hta2405.unite.action.mypage.MyPagePasswordChangeProcessAction;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;

@WebServlet("/mypage/*")
public class MyPageFrontController extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    HashMap<String, Action> actionMap = new HashMap<>();

    //아래에 URL, Action 추가
    @Override
    public void init() throws ServletException {
        actionMap.put("/password", new MyPagePasswordChangeAction());
        actionMap.put("/password/process", new MyPagePasswordChangeProcessAction());
    }

    protected void doProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("요청 주소 : " + req.getRequestURL());

        String requestURI = req.getRequestURI();
        System.out.println("requestURI = " + requestURI);

        String contextPath = req.getContextPath();
        System.out.println("contextPath = " + contextPath);

        String command = requestURI.substring(contextPath.length() + "/mypage".length());
        System.out.println("command = " + command);

        if (!actionMap.containsKey(command)) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error/404.jsp");
            dispatcher.forward(req, resp);
            return;
        }
        Action action = actionMap.get(command);
        ActionForward forward = action.execute(req, resp);

        if (forward != null) {
            if (forward.isRedirect()) {
                resp.sendRedirect(forward.getPath());
            } else {
                //null을 반환할 경우
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
