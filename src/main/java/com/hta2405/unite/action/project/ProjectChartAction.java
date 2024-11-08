package com.hta2405.unite.action.project;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;

//조직도 팝업창 임시
public class ProjectChartAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	ActionForward forward = new ActionForward();
		forward.setPath("/WEB-INF/views/project/orgChart.jsp"); 
		forward.setRedirect(false);
		return forward;
    }
}
