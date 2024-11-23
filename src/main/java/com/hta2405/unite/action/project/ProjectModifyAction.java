package com.hta2405.unite.action.project;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.ProjectTask;
import com.hta2405.unite.util.EmpUtil;
import com.hta2405.unite.util.ProjectUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 상세. main에서 누르고 들어오는 첫 창
public class ProjectModifyAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = req.getParameter("memberId");
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        int task_num = Integer.parseInt(req.getParameter("taskNum"));
        System.out.println("userid" + userid);
        System.out.println("projectid" + projectid);
        System.out.println("task_num" + task_num);

        String left = ProjectUtil.getProjectName(projectid);
        req.setAttribute("left", left);
        
        
        ProjectbDao user_task = new ProjectbDao();
        List<ProjectTask> task = user_task.getUserTaskDetail(userid, projectid, task_num);
        
        System.out.println(task);
        
        ActionForward forward = new ActionForward();
        req.setAttribute("task", task.get(0));
        
		forward.setPath("/WEB-INF/views/project/project_modify.jsp");
		return forward;
    }


}
