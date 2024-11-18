package com.hta2405.unite.action.project;

import java.io.IOException;
import java.util.List;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.dto.ProjectTask;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 상세. main에서 누르고 들어오는 첫 창
public class ProjectModifyProcessAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = req.getParameter("userid");
        int projectid = (Integer) req.getSession().getAttribute("projectId");
        int task_num = Integer.parseInt(req.getParameter("num"));
        System.out.println(userid);
        System.out.println(projectid);
        System.out.println(task_num);

        ActionForward forward = new ActionForward();
        ProjectbDao task_modify = new ProjectbDao();
        ProjectTask modify = new ProjectTask();
        
        modify.setProjectTitle(req.getParameter("board_subject"));
        modify.setProjectContent(req.getParameter("board_content"));
        modify.setTaskNum(task_num);
        modify.setProjectId(projectid);
        modify.setMemberId(userid);
        
        boolean result = task_modify.modify(modify);
        
        System.out.println(result);
        if(!result) {
			System.out.println("게시판 수정 실패");
			forward.setRedirect(false);
			req.setAttribute("message", "게시판 수정 오류입니다");
			forward.setPath("/WEB-INF/views/error/error.jsp");
		}else {
			//수정 성공의 경우
			System.out.println("게시판 수정 완료");
			forward.setRedirect(true);
			//수정한 글 내용을 보여주기 위해 글 내용 보기 페이지로 이동하기 위해 경로설정
			forward.setPath("comm?num="+ modify.getTaskNum()+"&userid="+modify.getMemberId());
		}
        return forward;
    }


}
