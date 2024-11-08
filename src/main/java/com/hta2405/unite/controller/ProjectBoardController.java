package com.hta2405.unite.controller;

import java.io.IOException;
import java.util.HashMap;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.action.project.ProjectWriteAction;

import jakarta.servlet.RequestDispatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;


@WebServlet("/projectb/*")
public class ProjectBoardController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    HashMap<String, Action> actionMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        actionMap.put("/write", new ProjectWriteAction());
    }
}
