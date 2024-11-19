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
        // 세션에서 사용자 및 프로젝트 정보 가져오기
        String userid = (String) req.getSession().getAttribute("id");
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        int taskNum = Integer.parseInt(req.getParameter("comment_board_num")); // 댓글이 달릴 글 번호
        String content = req.getParameter("content");

        // 부모 댓글 ID 파라미터 가져오기 (없으면 0으로 설정)
        int parentCommentId = req.getParameter("parent_comment_id") != null
                ? Integer.parseInt(req.getParameter("parent_comment_id"))
                : 0;

        // DAO 객체 생성
        ProjectbDao dao = new ProjectbDao();

        // 새로운 댓글 객체 생성
        ProjectTask taskComment = new ProjectTask();
        taskComment.setMemberId(userid);
        taskComment.setProjectId(projectid);
        taskComment.setTaskNum(taskNum);
        taskComment.setTaskContent(content);

        if (parentCommentId == 0) {
            // 원문 댓글인 경우
            int taskCommentId = dao.getTaskCommentId(taskNum); // task_comment_id를 가져오는 메서드 호출
            taskComment.setBoard_re_ref(taskCommentId); // task_comment_id를 task_comment_re_seq로 설정
            taskComment.setBoard_re_lev(0); // lev = 0
            taskComment.setBoard_re_seq(0); // seq는 트리거에서 자동으로 처리됨
        } else {
            // 대댓글인 경우, 부모 댓글의 task_comment_id를 가져오기 위해 select 쿼리로 조회
            int parentSeq = dao.getParentSeq(taskNum, parentCommentId); // 부모 댓글의 task_comment_id 가져오기
            taskComment.setBoard_re_ref(parentSeq); // 부모 댓글의 task_comment_id를 ref로 설정
            taskComment.setBoard_re_lev(1); // 대댓글이므로 lev = 1
            taskComment.setBoard_re_seq(dao.getMaxSeq(taskNum, parentSeq) + 1); // 부모 댓글의 seq 값을 기준으로 seq 설정
        }

        // 댓글 추가 DAO 호출
        int ok = dao.commentsInsert(taskComment);

        // 결과 출력
        resp.getWriter().print(ok);

        return null;
    }


}
