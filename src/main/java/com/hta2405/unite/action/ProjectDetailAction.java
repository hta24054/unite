package com.hta2405.unite.action;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dto.ProjectDetail;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 상세. main에서 누르고 들어오는 첫 창
public class ProjectDetailAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    		int projectId = Integer.parseInt(req.getParameter("projectId"));
	        System.out.println("projectId: " + projectId);
	        
	        ProjectDAO projectDAO = new ProjectDAO();
	        List<ProjectDetail> projectDetail = projectDAO.getProjectDetails(projectId); //db값 가져와 진행률 반영
	        
	        //진행과정 추가
	        
	        
	        
	        req.setAttribute("project", projectDetail); 
	        System.out.print(projectDetail);
	        ActionForward forward = new ActionForward();
	        forward.setPath("/WEB-INF/views/project/project_detail.jsp");
	        forward.setRedirect(false); // JSP로 포워딩할 경우 false 설정
	
	        return forward;
	    }
	
}
