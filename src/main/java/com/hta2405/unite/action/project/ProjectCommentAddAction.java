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
public class ProjectCommentAddAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = (String) req.getSession().getAttribute("id");
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        int task_num = Integer.parseInt(req.getParameter("comment_board_num"));
        String content = req.getParameter("content");
        int parentCommentId = task_num;  // 부모 댓글 ID
        
        // 부모 댓글의 lev와 seq를 가져옴
        ProjectbDao dao = new ProjectbDao();
        int comment_re_lev = 0;
        int comment_re_seq = 0;
        
        if (parentCommentId > 0) {
            // 부모 댓글이 있는 경우, 부모 댓글의 lev와 seq 값을 가져옴
            comment_re_lev = dao.getParentLev(task_num, parentCommentId) + 1;
            comment_re_seq = dao.getNextReSeq(task_num, parentCommentId);
        } else {
            // 본문 댓글인 경우, lev와 seq는 0으로 설정
            comment_re_lev = 0;
            comment_re_seq = 0;
        }
        
        // 새로운 댓글 객체 생성
        ProjectTask task_comment = new ProjectTask();
        task_comment.setMemberId(userid);
        task_comment.setProjectId(projectid);
        task_comment.setTaskNum(task_num);
        task_comment.setTaskContent(content);
        task_comment.setBoard_re_lev(comment_re_lev);
        task_comment.setBoard_re_seq(comment_re_seq);
        
        // 댓글 추가 DAO 호출
        int ok = dao.commentsInsert(task_comment);
        
        // 결과 출력
        resp.getWriter().print(ok);
        
        return null;
    }

}
