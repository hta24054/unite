package com.hta2405.unite.controller;

import com.hta2405.unite.action.project.ProjectCommentAction;
import com.hta2405.unite.action.project.ProjectCommentAddAction;
import com.hta2405.unite.action.project.ProjectCommentDeleteAction;
import com.hta2405.unite.action.project.ProjectCommentListAction;
import com.hta2405.unite.action.project.ProjectCommentReplyAction;
import com.hta2405.unite.action.project.ProjectCommentUpdateAction;
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
        actionMap.put("/write", ProjectWriteAction::new);
        actionMap.put("/membertask", ProjectMemberTaskAction::new);
        actionMap.put("/list", ProjectTaskListAction::new);
        actionMap.put("/down", ProjectDownAction::new);
        actionMap.put("/comm", ProjectCommentAction::new);
        actionMap.put("/modify", ProjectModifyAction::new);
        actionMap.put("/modifyProcess", ProjectModifyProcessAction::new);
        actionMap.put("/delete", ProjectDeleteAction::new);
        actionMap.put("/commentadd", ProjectCommentAddAction::new);
        actionMap.put("/commentlist", ProjectCommentListAction::new);
        actionMap.put("/reply", ProjectCommentReplyAction()::new);
        actionMap.put("/commentdelete", ProjectCommentDeleteAction()::new);
        actionMap.put("/update", ProjectCommentUpdateAction()::new);
    }
}