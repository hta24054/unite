package com.hta2405.unite.action.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dto.ProjectComplete;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//완료 프로젝트
public class ProjectCompleteAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String userid = (String) req.getSession().getAttribute("id");
    	
    	ProjectDAO projectDAO = new ProjectDAO();
        List<ProjectComplete> completedProjects = projectDAO.getCompletedProjects(userid);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8"); 
        req.setAttribute("completedProjects", completedProjects); //프로젝트 완료 셋팅
        
        ActionForward forward = new ActionForward();
		forward.setPath("/WEB-INF/views/project/project_complete.jsp"); 
		forward.setRedirect(false);
		return forward;
        
        //return null; // AJAX 호출이므로 포워딩 필요 없음
    }
}
