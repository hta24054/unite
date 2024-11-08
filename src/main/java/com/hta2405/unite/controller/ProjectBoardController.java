package com.hta2405.unite.controller;

import com.hta2405.unite.action.project.ProjectWriteAction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;


@WebServlet("/projectb/*")
public class ProjectBoardController extends AbstractFrontController{

    @Override
    public void init() throws ServletException {
        actionMap.put("/write", new ProjectWriteAction());
    }
}
