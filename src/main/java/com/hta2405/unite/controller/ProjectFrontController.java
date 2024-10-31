package com.hta2405.unite.controller;

import java.io.IOException;
import java.util.HashMap;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.action.ProjectCompleteAction;
import com.hta2405.unite.action.ProjectCreateAction;
import com.hta2405.unite.action.ProjectDetailAction;
import com.hta2405.unite.action.ProjectMainAction;
import com.hta2405.unite.action.ProjectProgressAction;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/project/*")
public class ProjectFrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    HashMap<String, Action> actionMap = new HashMap<>();

    //아래에 URL, Action 추가
    @Override
    public void init() throws ServletException {
        actionMap.put("/main", new ProjectMainAction());
        actionMap.put("/create", new ProjectCreateAction());
        actionMap.put("/complete", new ProjectCompleteAction());
        actionMap.put("/detail", new ProjectDetailAction());
        actionMap.put("/progress", new ProjectProgressAction());
    }

    protected void doProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("요청 주소 : " + req.getRequestURL());

        String requestURI = req.getRequestURI();
        System.out.println("requestURI = " + requestURI);

        String contextPath = req.getContextPath();
        System.out.println("contextPath = " + contextPath);

        String command = requestURI.substring(contextPath.length() + "/project".length());
        System.out.println("command = " + command);

        //등록된 URL이 아닌경우 404에러페이지 보여줌
        if (!actionMap.containsKey(command)) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/error/404.jsp");
            dispatcher.forward(req, resp);
            return;
        }

        //초기화
        Action action = actionMap.get(command);

        //요청 주소 별 ActionForward(리다이렉트 여부, 다음 주소 정보)객체 생성해서 변수에 담음

        //해시에 저장되어있는 new 객체들은 생성은 되지만, 이때 비로소 'forward' 로 참조변수가 설정됨
        //action.execute 로 리다이렉트 여부, 주소를 forward 변수에 담고, request 변수에 model 객체를 담음
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
