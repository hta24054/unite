package com.hta2405.unite.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dto.ProjectComplete;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 취소
public class ProjectCancelAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProjectDAO projectDAO = new ProjectDAO(); 
        List<ProjectComplete> cancelProjects = projectDAO.getCancelProjects(); //프로젝트 취소 셋팅 

        req.setAttribute("cancelProjects", cancelProjects);
        
        ActionForward forward = new ActionForward();
        forward.setPath("/WEB-INF/views/project/project_cancel.jsp");
        forward.setRedirect(false);
        return forward;
    }
}
