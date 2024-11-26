package com.hta2405.unite.controller;

import com.hta2405.unite.action.project.ProjectCancelAction;
import com.hta2405.unite.action.project.ProjectChartAction;
import com.hta2405.unite.action.project.ProjectCompleteAction;
import com.hta2405.unite.action.project.ProjectCreateAction;
import com.hta2405.unite.action.project.ProjectDetailAction;
import com.hta2405.unite.action.project.ProjectDoCreateAction;
import com.hta2405.unite.action.project.ProjectEmployAction;
import com.hta2405.unite.action.project.ProjectGetOngoingAction;
import com.hta2405.unite.action.project.ProjectMainAction;
import com.hta2405.unite.action.project.ProjectNoticeAction;
import com.hta2405.unite.action.project.ProjectProgressAction;
import com.hta2405.unite.action.project.ProjectUpdateProgressAction;
import com.hta2405.unite.action.project.ProjectUpdateTaskDesignAction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/project/*")
public class ProjectFrontController extends AbstractFrontController {

    //아래에 URL, Action 추가
    @Override
    public void init() throws ServletException {
        actionMap.put("/main", ProjectMainAction::new); //메인
        actionMap.put("/create", ProjectCreateAction::new);
        actionMap.put("/cancel", ProjectCancelAction::new);
        actionMap.put("/detail", ProjectDetailAction::new);
        actionMap.put("/progress", ProjectProgressAction::new);
        actionMap.put("/orgchart", ProjectChartAction::new);
        actionMap.put("/complete", ProjectCompleteAction::new);
        // actionMap.put("/write", ProjectWriteAction::new);
        // actionMap.put("/add", ProjectAddAction::new);
        actionMap.put("/employ", ProjectEmployAction::new);
        actionMap.put("/doCreate", ProjectDoCreateAction::new);
        actionMap.put("/getOngoingProjects", ProjectGetOngoingAction::new);
        actionMap.put("/updateprogress", ProjectUpdateProgressAction::new);
        actionMap.put("/updatetaskdesign", ProjectUpdateTaskDesignAction::new);
        actionMap.put("/notice", ProjectNoticeAction::new);
        
    }
}