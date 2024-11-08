package com.hta2405.unite.action.project;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
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
		String userid = (String) req.getSession().getAttribute("id");
        System.out.println("projectId: " + projectId + "\nuserid : " + userid);
        req.getSession().setAttribute("projectId", projectId);
        //프로젝트 detail이동시 leftbar바에 추가 
        ProjectDAO projectDAO1 = new ProjectDAO();
        String left = projectDAO1.getProjectName(projectId);
        req.setAttribute("left", left);
        System.out.println("left : " + left); //값 확인
        
        //진행률
        ProjectDAO projectDAO2 = new ProjectDAO();
        List<ProjectDetail> projectDetail1 = projectDAO2.getProjectDetail1(projectId, userid); //db값 가져와 진행률 반영
        req.setAttribute("project", projectDetail1); 
        System.out.println("진행률 : " + projectDetail1);//값 확인
        
        //진행과정 추가
        ProjectDAO projectDAO3 = new ProjectDAO();
        List<ProjectDetail> projectDetail2 = projectDAO3.getProjectDetail2(projectId); 
        req.setAttribute("project2", projectDetail2);
        System.out.println("진행 과정 : " + projectDetail2);//값 확인
        
        ActionForward forward = new ActionForward();
        forward.setPath("/WEB-INF/views/project/project_detail.jsp");
        forward.setRedirect(false); // JSP로 포워딩할 경우 false 설정

        return forward;
    }
	
}
