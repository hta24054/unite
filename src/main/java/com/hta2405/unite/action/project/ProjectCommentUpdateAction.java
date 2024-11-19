package com.hta2405.unite.action.project;

import java.io.IOException;
import java.io.PrintWriter;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.CommentDAO;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.dto.ProjectTask;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 상세. main에서 누르고 들어오는 첫 창
public class ProjectCommentUpdateAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 세션에서 사용자 및 프로젝트 정보 가져오기
        String userid = (String) req.getSession().getAttribute("id");
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        String content = req.getParameter("content");

        int num = Integer.parseInt(req.getParameter("num"));
        System.out.println("num : " + num);
		ProjectbDao dao = new ProjectbDao();
		ProjectTask comment = new ProjectTask();
		
		comment.setTaskContent(req.getParameter("content"));
		comment.setProjectId(Integer.parseInt(req.getParameter("num")));
		
		int ok = dao.commentsUpdate(comment);
		resp.getWriter().print(ok);
		
		return null;
    }

}
