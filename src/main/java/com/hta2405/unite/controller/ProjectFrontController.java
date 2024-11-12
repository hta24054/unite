package com.hta2405.unite.controller;

import com.hta2405.unite.action.project.ProjectCancelAction;
import com.hta2405.unite.action.project.ProjectChartAction;
import com.hta2405.unite.action.project.ProjectCompleteAction;
import com.hta2405.unite.action.project.ProjectCreateAction;
import com.hta2405.unite.action.project.ProjectDetailAction;
import com.hta2405.unite.action.project.ProjectDoCreateAction;
import com.hta2405.unite.action.project.ProjectEmployAction;
import com.hta2405.unite.action.project.ProjectGetOngoingAction;
import com.hta2405.unite.action.project.ProjectListAction;
import com.hta2405.unite.action.project.ProjectMainAction;
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
        actionMap.put("/main", new ProjectMainAction()); //메인
        actionMap.put("/create", new ProjectCreateAction());
        actionMap.put("/complete", new ProjectCompleteAction());
        actionMap.put("/cancel", new ProjectCancelAction());
        actionMap.put("/detail", new ProjectDetailAction());
        actionMap.put("/progress", new ProjectProgressAction());
        actionMap.put("/orgchart", new ProjectChartAction());
        actionMap.put("/list", new ProjectListAction());
        // actionMap.put("/write", new ProjectWriteAction());
        // actionMap.put("/add", new ProjectAddAction());
        actionMap.put("/employ", new ProjectEmployAction());
        actionMap.put("/doCreate", new ProjectDoCreateAction());
        actionMap.put("/getOngoingProjects", new ProjectGetOngoingAction());
        actionMap.put("/updateprogress", new ProjectUpdateProgressAction());
        actionMap.put("/updatetaskdesign", new ProjectUpdateTaskDesignAction());
        actionMap.put("/notification", new ProjectNotificationAction());
        
    }
}
