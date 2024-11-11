package com.hta2405.unite.controller;

import com.hta2405.unite.action.project.*;
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
        // actionMap.put("/write", new ProjectWriteAction());
        // actionMap.put("/add", new ProjectAddAction());
        actionMap.put("/employ", new ProjectEmployAction());
        actionMap.put("/doCreate", new ProjectDoCreateAction());
        actionMap.put("/getOngoingProjects", new ProjectGetOngoingAction());
        actionMap.put("/updateprogress", new ProjectUpdateProgressAction());
        actionMap.put("/updatetaskdesign", new ProjectUpdateTaskDesignAction());
        
    }
}
