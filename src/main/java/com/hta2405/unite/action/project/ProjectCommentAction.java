package com.hta2405.unite.action.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectDAO;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.dto.ProjectTask;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 상세. main에서 누르고 들어오는 첫 창
public class ProjectCommentAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = req.getParameter("userid");
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        int task_num = Integer.parseInt(req.getParameter("num"));
        System.out.println(userid);
        System.out.println(projectid);
        System.out.println(task_num);

        ProjectbDao user_task = new ProjectbDao();
        List<ProjectTask> task = user_task.getUserTaskDetail(userid, projectid, task_num);
        
        ActionForward forward = new ActionForward();
        req.setAttribute("task", task.get(0));
        
		forward.setPath("/WEB-INF/views/project/project_comm.jsp");
		return forward;
    }


}
