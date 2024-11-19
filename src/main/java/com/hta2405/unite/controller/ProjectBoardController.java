package com.hta2405.unite.controller;

import com.hta2405.unite.action.project.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;


@WebServlet("/projectb/*")
public class ProjectBoardController extends AbstractFrontController {

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
        actionMap.put("/reply", ProjectCommentReplyAction::new);
        actionMap.put("/commentdelete", ProjectCommentDeleteAction::new);
        actionMap.put("/update", ProjectCommentUpdateAction::new);
    }
}