package com.hta2405.unite.action.project;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;

//project_create와 동일. 현재 사용중 임시
public class ProjectCreateAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	ActionForward forward = new ActionForward();
		forward.setPath("/WEB-INF/views/project/project_organization.jsp"); //글 내용 보기 페이지로 이동하기 위해 경로지정
		forward.setRedirect(false);
		return forward;
    }
}
