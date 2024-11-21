package com.hta2405.unite.action.project;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.dto.ProjectTask;
import com.hta2405.unite.util.ProjectUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 상세. main에서 누르고 들어오는 첫 창
public class ProjectCommentAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = req.getParameter("userid");
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        //int task_num = (Integer) req.getSession().getAttribute("taskNum");
        int task_num = Integer.parseInt(req.getParameter("num"));
        req.getSession().setAttribute("taskNum", task_num);
        
        System.out.println(userid);   // 출력: 전달된 memberId
        System.out.println(projectid);  // 출력: 세션에서 가져온 projectId
        System.out.println(task_num);   // 출력: 전달된 taskNum

        String left = ProjectUtil.getProjectName(projectid);
        req.setAttribute("left", left);
        
        ProjectbDao user_task = new ProjectbDao();
        List<ProjectTask> task = user_task.getUserTaskDetail(userid, projectid, task_num);
        
        ActionForward forward = new ActionForward();
        req.setAttribute("task", task.get(0));
        
        forward.setPath("/WEB-INF/views/project/project_comm.jsp");
        return forward;
    }



}
