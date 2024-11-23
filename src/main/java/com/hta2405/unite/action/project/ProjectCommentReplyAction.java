package com.hta2405.unite.action.project;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.dto.ProjectTask;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 상세. main에서 누르고 들어오는 첫 창
public class ProjectCommentReplyAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 세션에서 사용자 및 프로젝트 정보 가져오기
        String userid = (String) req.getSession().getAttribute("id");
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        int taskNum = Integer.parseInt(req.getParameter("comment_board_num")); // 댓글이 달릴 글 번호
        String content = req.getParameter("content");

        ProjectbDao dao = new ProjectbDao();
        ProjectTask t = new ProjectTask();
        
		t.setMemberId(userid);
		t.setProjectContent(req.getParameter("content"));
		t.setBoard_re_lev(Integer.parseInt(req.getParameter("comment_re_lev")));
		t.setBoard_re_ref(Integer.parseInt(req.getParameter("comment_re_ref")));
		t.setTaskNum(Integer.parseInt(req.getParameter("comment_board_num")));
		t.setBoard_re_seq(Integer.parseInt(req.getParameter("comment_re_seq")));
		t.setTaskNum(taskNum);
		
		int ok = dao.commentsReply(t);
		resp.getWriter().print(ok);
		
		return null;
    }

}
