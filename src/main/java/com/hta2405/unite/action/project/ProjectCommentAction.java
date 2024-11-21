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
        // list에서 전달받은 num과 userid
        String userid = req.getParameter("userid");
        String numStr = req.getParameter("num");
        int task_num = (numStr != null) ? Integer.parseInt(numStr) : 0;

        // 전달받은 값이 null이 아닐 때만 세션에 저장
        if (userid != null && task_num != 0) {
            req.getSession().setAttribute("userid", userid);
            req.getSession().setAttribute("taskNum", task_num);
        }

        // 세션에서 값 가져오기
        userid = (String) req.getSession().getAttribute("userid");
        task_num = (Integer) req.getSession().getAttribute("taskNum");

        // 프로젝트 및 작업 정보 처리
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        System.out.println("comm: userid=" + userid + ", projectid=" + projectid + ", taskNum=" + task_num);

        String left = ProjectUtil.getProjectName(projectid);
        req.setAttribute("left", left);

        ProjectbDao user_task = new ProjectbDao();
        List<ProjectTask> task = user_task.getUserTaskDetail(userid, projectid, task_num);

        req.setAttribute("task", task.get(0));

        ActionForward forward = new ActionForward();
        forward.setPath("/WEB-INF/views/project/project_comm.jsp");
        return forward;
    }




}
