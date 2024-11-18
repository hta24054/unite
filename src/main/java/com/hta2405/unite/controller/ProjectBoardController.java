package com.hta2405.unite.controller;

import com.hta2405.unite.action.project.ProjectCommentAction;
import com.hta2405.unite.action.project.ProjectCommentAddAction;
import com.hta2405.unite.action.project.ProjectCommentListAction;
import com.hta2405.unite.action.project.ProjectDeleteAction;
import com.hta2405.unite.action.project.ProjectDownAction;
import com.hta2405.unite.action.project.ProjectMemberTaskAction;
import com.hta2405.unite.action.project.ProjectModifyAction;
import com.hta2405.unite.action.project.ProjectModifyProcessAction;
import com.hta2405.unite.action.project.ProjectTaskListAction;
import com.hta2405.unite.action.project.ProjectWriteAction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;


@WebServlet("/projectb/*")
public class ProjectBoardController extends AbstractFrontController{

    @Override
    public void init() throws ServletException {
        actionMap.put("/write", new ProjectWriteAction());
        actionMap.put("/membertask", new ProjectMemberTaskAction());
        actionMap.put("/list", new ProjectTaskListAction());
        actionMap.put("/down", new ProjectDownAction());
        actionMap.put("/comm", new ProjectCommentAction());
        actionMap.put("/modify", new ProjectModifyAction());
        actionMap.put("/modifyProcess", new ProjectModifyProcessAction());
        actionMap.put("/delete", new ProjectDeleteAction());
        actionMap.put("/commentadd", new ProjectCommentAddAction());
        actionMap.put("/commentlist", new ProjectCommentListAction());
    }
}