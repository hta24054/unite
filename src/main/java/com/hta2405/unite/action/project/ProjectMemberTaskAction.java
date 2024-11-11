package com.hta2405.unite.action.project;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.dto.ProjectDetail;
import com.hta2405.unite.dto.ProjectTask;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//project_create와 동일. 현재 사용중 임시
public class ProjectMemberTaskAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userid = req.getParameter("memberId");
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        System.out.println(userid);
        System.out.println(projectid);
        
        //프르젝트 이름
        ProjectbDao project_name = new ProjectbDao();
        String left = project_name.getProjectName(projectid);
        req.setAttribute("left", left);
        System.out.println("left : " + left); //값 확인
        
        //project_member 이름
        ProjectbDao user_name = new ProjectbDao();
        String user = user_name.getUserName(projectid, userid);
        req.setAttribute("user", user);
        System.out.println("user : " + user); //값 확인

        //출력
        ProjectbDao user_task = new ProjectbDao();
        List<ProjectTask> task = user_task.getUserTask(projectid, userid);
        req.setAttribute("task", task);
    	
    	
    	ActionForward forward = new ActionForward();
		forward.setPath("/WEB-INF/views/project/project_member_task.jsp"); //글 내용 보기 페이지로 이동하기 위해 경로지정
		forward.setRedirect(false);
		return forward;
    }
}
